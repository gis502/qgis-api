package com.ruoyi.system.service.slave.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.system.domain.entity.slave.RAnalysis;
import com.ruoyi.system.mapper.slave.RAnalysisMapper;
import com.ruoyi.system.service.slave.IRAnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName RAnalysisServiceImpl
 * @Description
 * @Author Huang Yx
 * @Date 2026/3/18 17:49
 */
@Slf4j
@Service
@DataSource(value = DataSourceType.SLAVE)
public class RAnalysisServiceImpl extends ServiceImpl<RAnalysisMapper, RAnalysis> implements IRAnalysisService {

    @Autowired
    private RAnalysisMapper mapper;

    @Override
    public List<RAnalysis> getRainStation(String adminCode) {

        // 查询指定行政区下最新的12个不同时间点
        LambdaQueryWrapper<RAnalysis> subQueryWrapper = Wrappers.lambdaQuery(RAnalysis.class);
        subQueryWrapper
                .eq(RAnalysis::getAdminCodeChn, adminCode)
                // 按时间降序排列
                .orderByDesc(RAnalysis::getDatetime)
                // 去重并只取前12个时间点
                .select(RAnalysis::getDatetime)
                .groupBy(RAnalysis::getDatetime)
                .last("LIMIT 12");

        // 获取最新的12个时间点列表
        List<String> latestDatetimeList = mapper.selectObjs(subQueryWrapper)
                .stream()
                .map(obj -> obj == null ? "" : obj.toString())
                .collect(Collectors.toList());

        // 查询时间点对应的所有数据，并按站点名称降序排列
        LambdaQueryWrapper<RAnalysis> mainQueryWrapper = Wrappers.lambdaQuery(RAnalysis.class);
        mainQueryWrapper
                .eq(RAnalysis::getAdminCodeChn, adminCode)
                // 筛选出最新12个时间点的数据
                .in(RAnalysis::getDatetime, latestDatetimeList)
                // 按站点名称降序排列
                .orderByDesc(RAnalysis::getStationName);

        return mapper.selectList(mainQueryWrapper);
    }
}
