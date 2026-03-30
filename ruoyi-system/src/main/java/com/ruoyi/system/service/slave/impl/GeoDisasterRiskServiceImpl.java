package com.ruoyi.system.service.slave.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.constant.BaseConstants;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.common.exception.ParmaException;
import com.ruoyi.system.domain.entity.slave.GeoDisasterRisk;
import com.ruoyi.system.mapper.slave.GeoDisasterRiskMapper;
import com.ruoyi.system.service.slave.IGeoDisasterRiskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName GeoDisasterRiskServiceImpl
 * @Description
 * @Author Huang Yx
 * @Date 2026/3/18 18:58
 */
@Slf4j
@Service
@DataSource(value = DataSourceType.SLAVE)
public class GeoDisasterRiskServiceImpl extends ServiceImpl<GeoDisasterRiskMapper, GeoDisasterRisk> implements IGeoDisasterRiskService {

    @Autowired
    private GeoDisasterRiskMapper mapper;

    @Override
    public Integer getHideNumByCounty(String county) {
        LambdaQueryWrapper<GeoDisasterRisk> lambdaQuery = Wrappers.lambdaQuery(GeoDisasterRisk.class);
        lambdaQuery.eq(GeoDisasterRisk::getCounty, county);
        List<GeoDisasterRisk> risks = mapper.selectList(lambdaQuery);
        if (risks == null) throw new ParmaException(BaseConstants.RESULT_ERROR);
        return risks.size();
    }
}
