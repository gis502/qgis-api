package com.ruoyi.system.domain.dto.slave;

import com.ruoyi.system.domain.entity.slave.GeoDisasterHide;
import lombok.Data;

/**
 * @ClassName DisasterAnalysisDTO
 * @Description
 * @Author Huang Yx
 * @Date 2026/3/18 18:39
 */
@Data
public class DisasterAnalysisDTO {

    // 继承DisasterHide的所有字段
    private GeoDisasterHide disasterHide;
    // 因子分析核心字段
    private Long hideId;
    private String probability;
    private String level;
    // 格式化后的概率
    private Double disasterProbability;

}
