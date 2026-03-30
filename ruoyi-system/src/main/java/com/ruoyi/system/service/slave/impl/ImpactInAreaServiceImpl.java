package com.ruoyi.system.service.slave.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.constant.BaseConstants;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.common.exception.ParmaException;
import com.ruoyi.system.domain.entity.slave.ImpactInArea;
import com.ruoyi.system.mapper.slave.ImpactInAreaMapper;
import com.ruoyi.system.service.slave.IImpactInAreaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName ImpactInAreaServiceImpl
 * @Description
 * @Author Huang Yx
 * @Date 2026/3/18 19:23
 */
@Slf4j
@Service
@DataSource(value = DataSourceType.SLAVE)
public class ImpactInAreaServiceImpl extends ServiceImpl<ImpactInAreaMapper, ImpactInArea> implements IImpactInAreaService {

    @Autowired
    private ImpactInAreaMapper mapper;

    @Override
    public Long getPeopleByLatLon(String lat, String lon) {
        LambdaQueryWrapper<ImpactInArea> queryWrapper = Wrappers.lambdaQuery(ImpactInArea.class);
        queryWrapper
                .select(ImpactInArea::getPeople)
                .apply("ST_Contains(point, ST_SetSRID(ST_MakePoint({0}::DOUBLE PRECISION, {1}::DOUBLE PRECISION), 4490))",
                        lon, lat);
        ImpactInArea impact = mapper.selectOne(queryWrapper);
        if (impact == null) throw new ParmaException(BaseConstants.RESULT_ERROR);
        return impact.getPeople();
    }
}
