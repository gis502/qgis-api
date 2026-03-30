package com.ruoyi.system.service.pub.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.BaseConstants;
import com.ruoyi.common.exception.ParmaException;
import com.ruoyi.common.exception.ServeException;
import com.ruoyi.common.utils.BaseUtils;
import com.ruoyi.system.domain.dto.dzxx.DZXXCenterDTO;
import com.ruoyi.system.domain.dto.pub.EqAssessmentDTO;
import com.ruoyi.system.domain.dto.pub.EqTriggerDTO;
import com.ruoyi.system.domain.entity.pub.DZEqEvent;
import com.ruoyi.system.domain.query.EqQuery;
import com.ruoyi.system.mapper.pub.DZEqEventMapper;
import com.ruoyi.system.service.dzxx.IDZXXCenterService;
import com.ruoyi.system.service.pub.IDZEqEventService;
import com.ruoyi.system.service.pub.IDZEqQueueService;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.WindowRange;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName DZEqEventServiceImpl
 * @Description 地震事件实现
 * @Author Huang Yx
 * @Date 2026/2/11 10:03
 */
@Slf4j
@Service
public class DZEqEventServiceImpl extends ServiceImpl<DZEqEventMapper, DZEqEvent> implements IDZEqEventService {

    @Autowired
    private IDZXXCenterService idzxxCenterService;
    @Autowired
    private IDZEqQueueService idzEqQueueService;

    // 地震业务触发
    @Transactional
    @Override
    public EqQuery trigger(EqTriggerDTO trigger) {
        log.info("地震参数：{}", trigger);
        // 异常值
        if (trigger == null) {
            throw new ParmaException(BaseConstants.PARAMS_ERROR);
        }
        // 专题图命名代码
        String code = BaseUtils.generationCode(trigger.getEqTime());
        // 地震业务
        try {
            DZXXCenterDTO dzxx = new DZXXCenterDTO();
            BeanUtils.copyProperties(trigger, dzxx);
            dzxx.setEvent(code);
            log.info("地震专题图编码：{}", code);
            // 震中位置存储
            idzxxCenterService.handle(dzxx);
            // 地震基本信息存储
            DZEqEvent dzeq = new DZEqEvent();
            BeanUtils.copyProperties(dzxx, dzeq);
            save(dzeq);
            log.info("地震基本信息已存库...");
        } catch (Exception ex) {
            log.error(ex.getMessage());
            ex.printStackTrace();
            throw new ServeException(BaseConstants.EQ_SERVER_ERROR);
        }

        String batch = BaseUtils.generationBatchCode(code);
        // 评估业务
        try {
            EqAssessmentDTO assess = new EqAssessmentDTO();
            BeanUtils.copyProperties(trigger, assess);
            assess.setEvent(code);
            assess.setEqQueueId(batch);

            // 开始评估
            idzEqQueueService.assess(assess);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            ex.printStackTrace();
            throw new ServeException(BaseConstants.ASSESS_SERVER_ERROR);
        }
        // 地震编码
        return new EqQuery(code, batch);
    }

    // 删除地震
    @Override
    public Boolean deletedById(Long Id) {
        // 空值
        if (Id == null) {
            throw new ParmaException(BaseConstants.PARAMS_ERROR);
        }
        // 条件构造
        LambdaQueryWrapper<DZEqEvent> lambdaQuery = Wrappers.lambdaQuery(DZEqEvent.class);
        lambdaQuery.eq(DZEqEvent::getId, Id);
        int flag = this.baseMapper.delete(lambdaQuery);
        return flag > 0 ? true : false;
    }

}
