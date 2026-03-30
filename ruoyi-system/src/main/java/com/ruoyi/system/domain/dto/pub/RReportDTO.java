package com.ruoyi.system.domain.dto.pub;

import com.ruoyi.system.domain.vo.pub.SecondaryDisasterVO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName RainstormReportDTO
 * @Description 暴雨文档
 * @Author Huang Yx
 * @Date 2026/3/18 13:12
 */
@Data
public class RReportDTO {

    private String rainQueueId;
    private String rainId;

    // 降雨概述
    private String rainTime;                                // 降雨时间
    private List<String> rainAreaPosition = new ArrayList<>();     // 降雨区域
    private List<String> rainAreaQuantity = new ArrayList<>();     // 区域降雨量
    private String concentratedAreaPosition;                // 降雨集中区域
    private String concentratedAreaQuantity;                // 降雨集中区域雨量
    private String concentratedAreaAverageQuantity;         // 降雨集中区域平均雨量
    private List<String> concentratedAreaDetailStreet = new ArrayList<>();     // 降雨集中区域街道
    private List<String> concentratedAreaDetailQuantity = new ArrayList<>();// 降雨集中街道雨量
    private List<String> concentratedAreaDetailGrade = new ArrayList<>();     // 降雨集中街道等级
    private List<String> extremelyHeavyRainstormStreet = new ArrayList<>();     // 特大暴雨监测街道
    private List<String> extremelyHeavyRainQuantity = new ArrayList<>();     // 特大暴雨监测雨量
    private List<String> rainstormStreet = new ArrayList<>();     // 暴雨或大暴雨街道
    private List<String> rainstormQuantity = new ArrayList<>();     // 暴雨或大暴雨雨量


    private List<String> riskArea = new ArrayList<>();     // 风险地区
    private Integer riskAreaQuantity;                       // 风险区数量
    private List<String> hideArea = new ArrayList<>();     // 隐患地区
    private Integer hideAreaQuantity;                       // 隐患点数量
    private List<String> hazards = new ArrayList<>();     // 致灾因子
    private String disasterChain;                           // 灾害链
    private String significantIncreaseArea;                 // 风险显著上升区域
    private List<String> significantIncreaseAreaStreet = new ArrayList<>();     // 显著上升街道
    private Integer significantIncreaseAreaRiskQuantity;    // 显著上升区域中重点关注风险点
    private Integer significantIncreaseAreaHideQuantity;    // 显著上升区域中重点关注隐患点

    // 次生灾害
    private List<SecondaryDisasterVO> secondaryDisasterReport = new ArrayList<>();

    // 应急处置建议
    private List<String> workScheduleArea = new ArrayList<>();     // 工作安排部署区域
    private List<String> evacuateTheCrowdArea = new ArrayList<>();     // 山洪、泥石流人员疏散区域
    private List<String> focusArea = new ArrayList<>();     // 内涝关注区域


}
