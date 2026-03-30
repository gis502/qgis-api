package com.ruoyi.system.domain.vo.pub;

import com.ruoyi.common.enums.TypesOfSecondaryDisasters;
import com.ruoyi.system.domain.dto.pub.RSecondaryDisasterDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName SecondaryDisasterVO
 * @Description 次生灾害风险报告
 * @Author Huang Yx
 * @Date 2026/3/18 13:16
 */
@Data
public class SecondaryDisasterVO {

    private TypesOfSecondaryDisasters disasterType;     // 灾害类型（滑坡、泥石流等）
    private String riskStreet;                          // 风险集中的街道
    private String riskPointName;                       // 风险集中点位
    private String riskPointProbability;                // 风险集中点位概率
    private Long influencePeopleQuantity;               // 影响人数
    private List<String> seriousArea = new ArrayList<>();                   // 中大型区域

    // 表格数据
    private List<RSecondaryDisasterDTO> disasterTableData = new ArrayList<>();

    private String extraLargeArea;                      // 特大型区域地址
    private String extraLargeAreaPoint;                 // 特大型风险区或隐患区
    private Integer extraLargeAreaRiskQuantity;         // 特大型风险区数量
    private Integer extraLargeAreaRiskPeopleQuantity;   // 特大型风险区影响人数
    private String smallArea;                           // 中小大型区域地址
    private String smallAreaPoint;                      // 中小型风险区或隐患区
    private Integer smallAreaRiskPeopleQuantity;        // 中小型风险区影响人数

}
