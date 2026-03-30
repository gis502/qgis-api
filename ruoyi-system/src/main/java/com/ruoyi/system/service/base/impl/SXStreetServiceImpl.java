package com.ruoyi.system.service.base.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.BaseConstants;
import com.ruoyi.common.exception.ParmaException;
import com.ruoyi.system.domain.entity.base.SXStreet;
import com.ruoyi.system.mapper.base.SXStreetMapper;
import com.ruoyi.system.service.base.ISXStreetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName SXStreetServiceImpl
 * @Description
 * @Author Huang Yx
 * @Date 2026/3/18 18:07
 */
@Slf4j
@Service
public class SXStreetServiceImpl extends ServiceImpl<SXStreetMapper, SXStreet> implements ISXStreetService {

    // 获取街道名称
    @Override
    public String inStreet(float lat, float lon) {
        LambdaQueryWrapper<SXStreet> queryWrapper = Wrappers.lambdaQuery(SXStreet.class);
        queryWrapper
                // street字段
                .select(SXStreet::getStreet)
                // PostGIS空间函数
                .apply("ST_Contains(geometry_wkt, ST_SetSRID(ST_MakePoint({0}, {1}), 4490))", lon, lat);
        SXStreet street = this.baseMapper.selectOne(queryWrapper);
        if (street == null) throw new ParmaException(BaseConstants.RESULT_ERROR);

        return street.getStreet();

    }
}
