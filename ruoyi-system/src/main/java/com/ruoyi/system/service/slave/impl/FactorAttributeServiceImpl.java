package com.ruoyi.system.service.slave.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.constant.BaseConstants;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.common.exception.ParmaException;
import com.ruoyi.system.domain.entity.slave.FactorAttribute;
import com.ruoyi.system.mapper.slave.FactorAttributeMapper;
import com.ruoyi.system.service.slave.IFactorAttributeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName FactorAttributeServiceImpl
 * @Description
 * @Author Huang Yx
 * @Date 2026/3/18 19:09
 */
@Slf4j
@Service
@DataSource(value = DataSourceType.SLAVE)
public class FactorAttributeServiceImpl extends ServiceImpl<FactorAttributeMapper, FactorAttribute> implements IFactorAttributeService {

    @Autowired
    private FactorAttributeMapper mapper;

    @Override
    public List<String> getFactorAttributeName() {
        LambdaQueryWrapper<FactorAttribute> lambdaQuery = Wrappers.lambdaQuery(FactorAttribute.class);
        lambdaQuery.select(FactorAttribute::getAttributeName);
        // 获取名称
        List<String> list = mapper.selectObjs(lambdaQuery)
                .stream()
                .map(obj -> obj == null ? "" : obj.toString())
                .collect(Collectors.toList());
        if (list == null) throw new ParmaException(BaseConstants.RESULT_ERROR);

        return list;
    }
}
