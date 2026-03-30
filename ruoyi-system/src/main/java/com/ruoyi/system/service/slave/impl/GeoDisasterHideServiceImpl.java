package com.ruoyi.system.service.slave.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.constant.BaseConstants;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.common.exception.ParmaException;
import com.ruoyi.system.domain.entity.slave.GeoDisasterHide;
import com.ruoyi.system.mapper.slave.GeoDisasterHideMapper;
import com.ruoyi.system.service.slave.IGeoDisasterHideService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName GeoDisasterHideServiceImpl
 * @Description
 * @Author Huang Yx
 * @Date 2026/3/18 19:04
 */
@Slf4j
@Service
@DataSource(value = DataSourceType.SLAVE)
public class GeoDisasterHideServiceImpl extends ServiceImpl<GeoDisasterHideMapper, GeoDisasterHide> implements IGeoDisasterHideService {

    @Autowired
    private GeoDisasterHideMapper mapper;

    @Override
    public Integer getRiskNumByCounty(String county) {
        LambdaQueryWrapper<GeoDisasterHide> lambdaQuery = Wrappers.lambdaQuery(GeoDisasterHide.class);
        lambdaQuery.eq(GeoDisasterHide::getCounty, county);
        List<GeoDisasterHide> hides = mapper.selectList(lambdaQuery);
        if (hides == null) throw new ParmaException(BaseConstants.RESULT_ERROR);
        return hides.size();
    }
}
