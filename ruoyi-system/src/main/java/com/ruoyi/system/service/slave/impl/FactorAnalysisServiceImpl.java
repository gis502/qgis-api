package com.ruoyi.system.service.slave.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.constant.BaseConstants;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.common.exception.ParmaException;
import com.ruoyi.system.domain.dto.slave.DisasterDTO;
import com.ruoyi.system.domain.dto.slave.FactorAnalysisSubDTO;
import com.ruoyi.system.domain.entity.slave.FactorAnalysis;
import com.ruoyi.system.domain.entity.slave.GeoDisasterHide;
import com.ruoyi.system.mapper.slave.FactorAnalysisMapper;
import com.ruoyi.system.mapper.slave.GeoDisasterHideMapper;
import com.ruoyi.system.service.slave.IFactorAnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName FactorAnalysisServiceImpl
 * @Description
 * @Author Huang Yx
 * @Date 2026/3/18 18:42
 */
@Slf4j
@Service
@DataSource(value = DataSourceType.SLAVE)
public class FactorAnalysisServiceImpl extends ServiceImpl<FactorAnalysisMapper, FactorAnalysis> implements IFactorAnalysisService {

    @Autowired
    private GeoDisasterHideMapper disasterHideMapper;

    @Override
    public List<Map<String, Object>> queryDisasterEstimation(Long disasterId) {
        // 去重子查询
        String subSql = "SELECT DISTINCT ON (fv.hide_id) " +
                "fv.hide_id, fa.probability, fa.level " +
                "FROM xian_factor_value fv " +
                "INNER JOIN xian_factor_analysis fa ON fv.value_id = fa.value_id " +
                "INNER JOIN xian_disaster_hide dh_sub ON fv.hide_id = dh_sub.id " +
                "WHERE fa.rain_disaster_id = #{disasterId} " +
                "ORDER BY fv.hide_id, fa.value_id";

        // 获取按hide_id去重的因子分析数据
        List<FactorAnalysisSubDTO> dtos = this.baseMapper.factorAnalysisBySubSql(subSql, disasterId);

        // 筛选高/中风险的记录
        List<FactorAnalysisSubDTO> highMidFaList = dtos.stream()
                .filter(fa -> "[高]".equals(fa.getLevel()) || "[中]".equals(fa.getLevel()))
                .collect(Collectors.toList());

        // 抛出异常
        if (highMidFaList == null || highMidFaList.isEmpty()) {
            throw new ParmaException(BaseConstants.RESULT_ERROR);
        }

        // 查询 hide_id 的记录
        List<Long> hideIdList = new ArrayList<>();
        for (FactorAnalysisSubDTO fa : highMidFaList) {
            Long hideId = fa.getHideId();
            if (hideId != null && hideId > 0) {
                hideIdList.add(hideId);
            }
        }

        //抛异常
        if (hideIdList.isEmpty()) {
            throw new ParmaException(BaseConstants.RESULT_ERROR);
        }

        LambdaQueryWrapper<GeoDisasterHide> dhWrapper = Wrappers.lambdaQuery(GeoDisasterHide.class);
        dhWrapper.in(GeoDisasterHide::getId, hideIdList);
        List<GeoDisasterHide> disasterHideList = disasterHideMapper.selectList(dhWrapper);

        // 匹配因子数据
        List<DisasterDTO> dtoList = new ArrayList<>();
        if (disasterHideList != null && !disasterHideList.isEmpty()) {
            for (GeoDisasterHide dh : disasterHideList) {
                // 匹配当前隐患点对应的因子分析数据
                FactorAnalysisSubDTO faSub = null;
                for (FactorAnalysisSubDTO fa : highMidFaList) {
                    if (fa.getHideId() != null && fa.getHideId().equals(dh.getId())) {
                        faSub = fa;
                        break;
                    }
                }
                // 跳过无匹配数据的记录
                if (faSub == null) {
                    continue;
                }
                DisasterDTO dto = getDisasterDTO(dh, faSub);
                dtoList.add(dto);
            }
        }

        // 按概率高低排序
        Collections.sort(dtoList, new Comparator<DisasterDTO>() {
            @Override
            public int compare(DisasterDTO d1, DisasterDTO d2) {
                if (d1.getDisasterProbability() == null) {
                    return 1;
                }
                if (d2.getDisasterProbability() == null) {
                    return -1;
                }
                // 降序排列：d2 - d1
                return d2.getDisasterProbability().compareTo(d1.getDisasterProbability());
            }
        });

        return getMaps(dtoList);
    }

    // 转换 map
    private List<Map<String, Object>> getMaps(List<DisasterDTO> dtoList) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        if (dtoList != null && !dtoList.isEmpty()) {
            for (DisasterDTO dto : dtoList) {
                Map<String, Object> map = new HashMap<>();
                map.put("position", dto.getPosition());
                map.put("disasterType", dto.getDisasterType());
                map.put("lon", dto.getLon());
                map.put("lat", dto.getLat());
                map.put("disasterProbability", dto.getDisasterProbability());
                map.put("level", dto.getLevel());
                map.put("city", dto.getCity());
                map.put("village", dto.getVillage());
                resultList.add(map);
            }
        }
        return resultList;
    }

    // 去除[]并保留2位小数转数字
    private DisasterDTO getDisasterDTO(GeoDisasterHide dh, FactorAnalysisSubDTO faSub) {
        Double disasterProbability = null;
        if (faSub.getProbability() != null && !faSub.getProbability().isEmpty()) {
            // 去除首尾的[]
            String cleanProb = faSub.getProbability().trim().replaceAll("^\\[|\\]$", "");
            // 空字符串处理
            if (!cleanProb.isEmpty()) {
                BigDecimal probBigDecimal = new BigDecimal(cleanProb);
                // 四舍五入保留2位小数
                disasterProbability = probBigDecimal.setScale(2, RoundingMode.HALF_UP).doubleValue();
            }
        }
        DisasterDTO dto = new DisasterDTO();
        dto.setPosition(dh.getPosition());
        dto.setDisasterType(dh.getDisasterType());
        dto.setLon(dh.getLon());
        dto.setLat(dh.getLat());
        dto.setDisasterProbability(disasterProbability);
        dto.setLevel(faSub.getLevel());
        dto.setCity(dh.getCity());
        dto.setVillage(dh.getVillage());
        return dto;
    }

}
