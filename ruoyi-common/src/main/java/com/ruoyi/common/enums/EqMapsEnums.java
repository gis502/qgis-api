package com.ruoyi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @ClassName EqMapsEnums
 * @Description 地震专题图集枚举
 * @Author Huang Yx
 * @Date 2026/2/12 22:35
 */
@Getter
@AllArgsConstructor
public enum EqMapsEnums {

    // 制图枚举
    EARTHQUAKE_DISTRIBUTION(1, "地震影响估计范围分布图"),
    EARTHQUAKE_RUPTURE(2, "震中附近活动断裂分布图"),
    EARTHQUAKE_HOSPITAL(3, "震区医院分布图"),
    EARTHQUAKE_RESCUE_TEAM(4, "震中附近救援队伍分布图"),
    EARTHQUAKE_RESCUE_MATERIAL(5, "震区附近救援物资分布图"),
    EARTHQUAKE_VILLAGES_DISTANCE(6, "震中与乡镇距离图"),
    EARTHQUAKE_TRAFFIC(7, "震区交通图"),
    EARTHQUAKE_SCHOOL(8, "震区学校分布图"),
    EARTHQUAKE_PEOPLE(9, "震区附近人口分布图"),
    EARTHQUAKE_SAFE_PLACE(10, "震区附近疏散场地分布图"),
    EARTHQUAKE_PUBLIC_PLACE(11, "震区附近公共场所分布图"),
    EARTHQUAKE_DANGER_SOURCE(12, "震区危险源分布图"),
    EARTHQUAKE_RISK_AREA(13, "震中附近风险区分布图"),
    EARTHQUAKE_HIDE_POINT(14, "震区潜在地质灾害分布图"),
    EARTHQUAKE_IMPORTANT_OBJECTS(15, "震区附近重要目标分布图"),
    EARTHQUAKE_TOURIST_SPOT(16, "震区附近景区分布图"),
    EARTHQUAKE_RESERVOIR(17, "震区附近水库分布图"),
    EARTHQUAKE_DX(18, "震中地形图"),
    EARTHQUAKE_CROPS(19, "震区附近农作物分布图");

    private final Integer num;
    private final String name;


}
