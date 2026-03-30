package com.ruoyi.system.core.rabbitmq;

import com.ruoyi.common.constant.BaseConstants;
import com.ruoyi.system.domain.bo.DlqMessage;
import com.ruoyi.system.domain.params.QgisArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @ClassName RabbitMQDLQ
 * @Description 死信队列
 * @Author Huang Yx
 * @Date 2026/3/23 19:36
 */
@Slf4j
@Component
public class DlqOperate {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息到死信队列
     *
     * @param arg    失败的参数
     * @param reason 失败原因
     * @param e      异常信息
     */
    public void sendToDlq(QgisArgs arg, String reason, Exception e) {
        try {
            // 封装死信消息体，包含原始参数、失败原因、异常信息、重试次数等
            DlqMessage dlqMessage = DlqMessage.builder()
                    .qgisArgs(arg)
                    .failReason(reason + (e != null ? " | " + e.getMessage() : ""))
                    .failTime(System.currentTimeMillis())
                    .retryCount(0) // 初始重试次数为0
                    .build();
            // 发送到死信队列
            rabbitTemplate.convertAndSend(
                    BaseConstants.DLX_EXCHANGE,
                    BaseConstants.DLQ_QUEUE,
                    dlqMessage
            );
            log.info("消息已发送到死信队列! 参数ID:{}", arg.getId());
        } catch (Exception ex) {
            log.error("发送死信队列失败! 参数ID:{}", arg.getId(), ex);
            // 记录到数据库，避免消息丢失
            // saveToDb(arg, reason, e);
        }
    }


}
