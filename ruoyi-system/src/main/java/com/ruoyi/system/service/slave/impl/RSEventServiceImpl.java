package com.ruoyi.system.service.slave.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.constant.BaseConstants;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.common.exception.base.BaseException;
import com.ruoyi.system.domain.entity.slave.RSEvent;
import com.ruoyi.system.mapper.slave.RSEventMapper;
import com.ruoyi.system.service.slave.IRSEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @ClassName REventServiceImpl
 * @Description
 * @Author Huang Yx
 * @Date 2026/3/18 16:05
 */
@Slf4j
@Service
@DataSource(value = DataSourceType.SLAVE)
public class RSEventServiceImpl extends ServiceImpl<RSEventMapper, RSEvent> implements IRSEventService {

    @Autowired
    private RSEventMapper mapper;

    // 获取最新暴雨事件
    @Override
    public RSEvent getLatestRainDisaster() {
        LambdaQueryWrapper<RSEvent> lambdaQuery = Wrappers.lambdaQuery(RSEvent.class);
        lambdaQuery
                .orderByDesc(RSEvent::getCreateTime)
                .last("limit 1");
        RSEvent event = mapper.selectOne(lambdaQuery);
        if (event == null) throw new BaseException(BaseConstants.RESULT_ERROR);
        return event;
    }

    // 获取暴雨事件时间
    @Override
    public LocalDateTime getRainTime(Long disasterId) {
        LambdaQueryWrapper<RSEvent> lambdaQuery = Wrappers.lambdaQuery(RSEvent.class);
        lambdaQuery.eq(RSEvent::getDisasterId, disasterId);
        RSEvent event = mapper.selectOne(lambdaQuery);
        if (event == null) throw new BaseException(BaseConstants.RESULT_ERROR);
        return event.getOccurrenceTime();
    }

    // 获取暴雨区域位置
    @Override
    public String getRainAreaPosition(Long disasterId) {
        LambdaQueryWrapper<RSEvent> lambdaQuery = Wrappers.lambdaQuery(RSEvent.class);
        lambdaQuery.eq(RSEvent::getDisasterId, disasterId);
        RSEvent event = mapper.selectOne(lambdaQuery);
        if (event == null) throw new BaseException(BaseConstants.RESULT_ERROR);
        return event.getPosition();
    }

    // 获取暴雨区域数量
    @Override
    public String getRainAreaQuantity(Long disasterId) {
        LambdaQueryWrapper<RSEvent> lambdaQuery = Wrappers.lambdaQuery(RSEvent.class);
        lambdaQuery.eq(RSEvent::getDisasterId, disasterId);
        RSEvent event = mapper.selectOne(lambdaQuery);
        if (event == null) throw new BaseException(BaseConstants.RESULT_ERROR);
        return event.getRainfall();
    }
}
