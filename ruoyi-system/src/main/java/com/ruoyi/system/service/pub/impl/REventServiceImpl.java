package com.ruoyi.system.service.pub.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.BaseConstants;
import com.ruoyi.common.exception.ParmaException;
import com.ruoyi.common.exception.ServeException;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.BaseUtils;
import com.ruoyi.system.domain.dto.dzxx.DZXXCenterDTO;
import com.ruoyi.system.domain.dto.pub.EqAssessmentDTO;
import com.ruoyi.system.domain.dto.pub.RAssessmentDTO;
import com.ruoyi.system.domain.dto.pub.REventDTO;
import com.ruoyi.system.domain.dto.pub.RTriggerDTO;
import com.ruoyi.system.domain.entity.dzxx.DZXXCenter;
import com.ruoyi.system.domain.entity.pub.DZEqEvent;
import com.ruoyi.system.domain.entity.pub.REvent;
import com.ruoyi.system.domain.query.EqQuery;
import com.ruoyi.system.domain.query.RQuery;
import com.ruoyi.system.mapper.pub.REventMapper;
import com.ruoyi.system.service.pub.IDZEqQueueService;
import com.ruoyi.system.service.pub.IREventService;
import com.ruoyi.system.service.slave.IRSEventService;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName REventServiceImpl
 * @Description
 * @Author Huang Yx
 * @Date 2026/3/18 20:54
 */
@Slf4j
@Service
public class REventServiceImpl extends ServiceImpl<REventMapper, REvent> implements IREventService {

    @Autowired
    private IDZEqQueueService idzEqQueueService;
    // 暴雨触发
    @Transactional
    @Override
    public RQuery trigger(RTriggerDTO trigger) {
        log.info("暴雨参数：{}", trigger);
        // 异常值
        if (trigger == null) {
            throw new ParmaException(BaseConstants.PARAMS_ERROR);
        }
        // 专题图命名代码
        String code = BaseUtils.generationRainCode(trigger.getOccurrenceTime());
        // 暴雨业务
        try {
            // 暴雨信息存储
            REventDTO eventdto = new REventDTO();
            BeanUtils.copyProperties(trigger, eventdto);
            eventdto.setRainId(code);
            log.info("暴雨专题图编码：{}", code);
            handle(eventdto);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            ex.printStackTrace();
            throw new ServeException(BaseConstants.RAIN_SERVER_ERROR);
        }
        String batch = BaseUtils.generationBatchCode(code);
        // 评估业务
        try {
            RAssessmentDTO assess = new RAssessmentDTO();
            BeanUtils.copyProperties(trigger, assess);
            assess.setRainId(code);
            assess.setRainQueueId(batch);
            // 开始评估
            idzEqQueueService.assess(assess);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            ex.printStackTrace();
            throw new ServeException(BaseConstants.ASSESS_SERVER_ERROR);
        }
        // 地震编码
        return new RQuery(code, batch);
    }


    // 删除地震
    @Override
    public Boolean deletedById(Long Id) {
        // 空值
        if (Id == null) {
            throw new ParmaException(BaseConstants.PARAMS_ERROR);
        }
        // 条件构造
        LambdaQueryWrapper<REvent> lambdaQuery = Wrappers.lambdaQuery(REvent.class);
        lambdaQuery.eq(REvent::getId, Id);
        int flag = this.baseMapper.delete(lambdaQuery);
        return flag > 0 ? true : false;
    }


    // 处理暴雨数据
    private void handle(REventDTO eventdto) {
        // 抛出异常
        if (eventdto == null) {
            throw new ParmaException(BaseConstants.PARAMS_ERROR);
        }

        try {
            REvent revent = new REvent();
            BeanUtils.copyProperties(eventdto, revent);
            // 处理空间数据
            GeometryFactory geometryFactory = new GeometryFactory();
            Point point = geometryFactory.createPoint(new Coordinate(
                    eventdto.getLongitude(), eventdto.getLatitude()
            ));
            revent.setGeom(point);
            revent.getGeom().setSRID(4490);
            // 存库
            save(revent);
            log.info("暴雨位基本信息已存库...");
        } catch (Exception ex) {
            log.error("暴雨触发：暴雨基本信息保存失败!", ex.getMessage());
            ex.printStackTrace();
            throw new ServiceException(BaseConstants.RAIN_SERVER_ERROR);
        }


    }

}
