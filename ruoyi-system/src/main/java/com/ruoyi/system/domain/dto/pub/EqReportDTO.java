package com.ruoyi.system.domain.dto.pub;

import com.ruoyi.system.domain.dto.base.HospitalDTO;
import com.ruoyi.system.domain.dto.base.RescueMaterialsDTO;
import com.ruoyi.system.domain.dto.base.RescueTeamsDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 暴雨分析报告实体类
 */
@Data
public class EqReportDTO {

    private String event;   // 地震编码
    private String eqQueueId;   // 评估编码

    // 地震概况部分
    private LocalDateTime eqTime;
    private String eqAddr;
    private double longitude;
    private double latitude;
    private double eqMagnitude;
    private double eqDepth;

    // 风险评估部分
    private int intensity;   // 重灾区烈度
    private double area;  // 重灾区面积(km2)
    private int influencePopulationMax;    // 地震影响人口最大值
    private int influencePopulationMin;    // 地震影响人口最小值
    private int deathMax;  // 地震预计伤亡人数最大值
    private int deathMin;  // 地震预计伤亡人数最小值
    private String fault; // 震中最近断裂带

    private List<HospitalDTO> hospital100km;  // 震中附近100km内的医院列表(名称/总床位)
    private List<RescueTeamsDTO> fireTeams100km;    // 震中附近100km内的消防队信息(名称/人数)
    private List<RescueMaterialsDTO> storePoint100km;  // 震中附近100km内的救援物资信息

    // 应急处置建议
    private String earthQuakeEmergencyLevel;    //地震响应等级

}