package com.ruoyi.system.service.pub.impl;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.constant.BaseConstants;
import com.ruoyi.common.enums.BaseEnums;
import com.ruoyi.common.exception.ParmaException;
import com.ruoyi.common.utils.BaseUtils;
import com.ruoyi.common.utils.http.HttpRestClient;
import com.ruoyi.system.core.rabbitmq.DlqOperate;
import com.ruoyi.system.domain.params.QgisArgs;
import com.ruoyi.system.service.pub.IDZEqQueueService;
import com.ruoyi.system.service.pub.IFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName FeignServiceImpl
 * @Description 三方服务接口
 * @Author Huang Yx
 * @Date 2026/2/12 20:30
 */
@Slf4j
@Service
public class FeignServiceImpl implements IFeignService {

    @Resource
    private HttpRestClient restclient;
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private IDZEqQueueService idzEqQueueService;
    @Autowired
    private DlqOperate dlqOperate;

    // 调用制图服务
    @Override
    public void invoke(List<QgisArgs> args) {
        // 进度
        double p = 0;
        // 异常参数
        if (args == null || args.size() == 0) {
            throw new ParmaException(BaseConstants.PARAMS_ERROR);
        }
        // 保证尽可能的多产, 这里不要进行任何异常抛出, 只能记录失败的图层
        try {
            log.info("开始调用pyqgis服务");
            for (QgisArgs arg : args) {
                try {   // 处理单个图件
                    // 返回数据格式
                    ParameterizedTypeReference<String> res = new ParameterizedTypeReference<String>() {
                    };
                    // 制图
                    String mapName = restclient.post(BaseConstants.HTTP_QGIS_THEMATIC, JSON.toJSON(arg), res);
                    // 专题图名称
                    if (mapName == null || mapName.equals("")) {
                        log.error("产出图件失败!");
                        // 进入死信队列
                        dlqOperate.sendToDlq(arg, "产出图件返回空", null);
                    }
                    log.info("图件产品：{}-产出成功", mapName);
                    // 这里可能会出现线程抢占, 需要加入乐观锁
                    if (arg.getDisaster() == BaseConstants.EQ_DISASTER_MAP) {
                        // 更新状态 专题图是第二部分产品 保证进度同步 所以需要 num+1 处理
                        p = BaseUtils.compute(arg.getId() + 1, 0);
                    }
                    if (arg.getDisaster() == BaseConstants.RAIN_DISASTER_MAP) {
                        p = BaseUtils.compute(arg.getId() + 1, 1);
                    }
                    idzEqQueueService.updated(arg.getEvent(), arg.getQueueId(), p, BaseEnums.CALCULATING.getCode());
                    // 推送消息
                    rabbitTemplate.convertAndSend(BaseConstants.ASSESS_EXCHANGE, BaseConstants.MAPS_QUEUE, arg);
                } catch (Exception ex) {
                    log.error("制图服务出现错误,请检查服务问题! {}", ex.getMessage());
                    // 进入死信队列
                    dlqOperate.sendToDlq(arg, "图层处理异常", ex);
                }
            }
        } catch (Exception e) {
            log.error("制图服务出现错误,请检查服务问题! {}", e.getMessage(), e);
            // 批量失败时，将所有未处理的参数投递到死信
            for (QgisArgs arg : args) {
                // 进入死信队列
                dlqOperate.sendToDlq(arg, "制图服务整体异常", e);
            }
        }
    }
}