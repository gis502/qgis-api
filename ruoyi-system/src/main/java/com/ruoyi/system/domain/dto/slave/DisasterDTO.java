package com.ruoyi.system.domain.dto.slave;

import lombok.Data;

/**
 * @ClassName DisasterDTO
 * @Description
 * @Author Huang Yx
 * @Date 2026/3/20 15:58
 */
@Data
public class DisasterDTO {

    private String position;          // 隐患点位置
    private String disasterType;      // 灾害类型
    private Double lon;               // 经度
    private Double lat;               // 纬度
    private Double disasterProbability; // 格式化后的灾害概率
    private String level;             // 风险等级
    private String city;              // 城市
    private String village;           // 村庄

}
