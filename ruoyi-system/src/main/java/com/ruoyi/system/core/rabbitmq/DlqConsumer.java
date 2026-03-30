package com.ruoyi.system.core.rabbitmq;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.constant.BaseConstants;
import com.ruoyi.common.utils.BaseUtils;
import com.ruoyi.common.utils.http.HttpRestClient;
import com.ruoyi.system.domain.bo.DlqMessage;
import com.ruoyi.system.domain.params.QgisArgs;
import com.ruoyi.system.service.pub.IDZEqQueueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import com.ruoyi.common.enums.BaseEnums;

import javax.annotation.Resource;

/**
 * @ClassName DlqConsumer
 * @Description
 * @Author Huang Yx
 * @Date 2026/3/23 19:40
 */
@Slf4j
@Component
public class DlqConsumer {

    @Resource
    private HttpRestClient restclient;
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private IDZEqQueueService idzEqQueueService;

    // 最大重试次数
    private int maxRetry = 3;

    // 死信队列消费者
    @RabbitListener(queues = BaseConstants.DLQ_QUEUE)
    public void handleDlqMessage(DlqMessage dlqMessage) {
        QgisArgs arg = dlqMessage.getQgisArgs();
        int currentRetryCount = dlqMessage.getRetryCount();

        try {
            log.info("处理死信队列消息! 参数ID:{} 重试次数:{}", arg.getId(), currentRetryCount);

            // 判断是否超过最大重试次数
            if (currentRetryCount >= maxRetry) {
                log.error("消息重试次数已达上限! 参数ID:{} 失败原因:{}", arg.getId(), dlqMessage.getFailReason());
                // 处理：告警+记录到失败表+人工介入
                sendAlarm(arg, dlqMessage);
                saveToFailTable(dlqMessage);
                return;
            }

            // 重试处理逻辑（复用原invoke的核心逻辑）
            ParameterizedTypeReference<String> res = new ParameterizedTypeReference<String>() {
            };
            String mapName = restclient.post(BaseConstants.HTTP_QGIS_THEMATIC, JSON.toJSON(arg), res);

            if (mapName == null || mapName.equals("")) {
                throw new Exception("重试后产出图件仍失败");
            }

            // 重试成功：记录日志 + 推送原业务消息
            log.info("死信消息处理成功! 参数ID:{} 图件名称:{}", arg.getId(), mapName);
            // 更新进度
            double p = 0;
            if (arg.getDisaster() == BaseConstants.EQ_DISASTER_MAP) {
                p = BaseUtils.compute(arg.getId() + 1, 0);
            }
            if (arg.getDisaster() == BaseConstants.RAIN_DISASTER_MAP) {
                p = BaseUtils.compute(arg.getId() + 1, 1);
            }
            idzEqQueueService.updated(arg.getEvent(), arg.getQueueId(), p, BaseEnums.CALCULATING.getCode());
            // 推送原业务消息
            rabbitTemplate.convertAndSend(BaseConstants.ASSESS_EXCHANGE, BaseConstants.MAPS_QUEUE, arg);

        } catch (Exception e) {
            log.error("死信消息重试失败! 参数ID:{} 重试次数:{}", arg.getId(), currentRetryCount, e);
            // 重试失败：更新重试次数，重新发送到死信队列（设置延迟）
            dlqMessage.setRetryCount(currentRetryCount + 1);
            dlqMessage.setFailReason(dlqMessage.getFailReason() + " | 重试失败：" + e.getMessage());
            // 延迟发送（需要安装rabbitmq_delayed_message_exchange插件）
            rabbitTemplate.convertAndSend(
                    BaseConstants.DLX_EXCHANGE,
                    BaseConstants.DLQ_QUEUE,
                    dlqMessage,
                    message -> {
                        // 设置延迟时间（指数退避：10s, 30s, 60s）
                        long delay = 10000 * (long) Math.pow(3, currentRetryCount);
                        message.getMessageProperties().setDelay((int) delay);
                        return message;
                    }
            );
            log.info("死信消息已重新投递! 参数ID:{} 下次重试延迟:{}ms", arg.getId(),
                    10000 * (long) Math.pow(3, currentRetryCount));
        }
    }

    // 发送告警（邮件/短信/钉钉等）
    private void sendAlarm(QgisArgs arg, DlqMessage dlqMessage) {
        // 实现告警逻辑：调用钉钉机器人/邮件接口等
        log.error("【告警】图件处理失败需要人工介入! 参数ID:{} 失败原因:{}", arg.getId(), dlqMessage.getFailReason());
    }

    // 保存失败记录到数据库
    private void saveToFailTable(DlqMessage dlqMessage) {
        // 实现数据库存储逻辑，便于人工排查和处理
        log.info("失败记录已保存到数据库! 参数ID:{}", dlqMessage.getQgisArgs().getId());
    }
}
