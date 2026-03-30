package com.ruoyi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName EqMapsEnums
 * @Description 暴雨专题图集枚举
 * @Author Huang Yx
 * @Date 2026/2/12 22:35
 */
@Getter
@AllArgsConstructor
public enum RainMapsEnums {

    // 制图枚举
    EARTHQUAKE_PEOPLE(1, "暴雨滑坡潜在隐患点及人口分布图"),
    EARTHQUAKE_HOSPITAL(2, "暴雨山洪潜在隐患点及人口分布图"),
    EARTHQUAKE_HIDE_POINT(3, "暴雨内涝潜在隐患点及人口分布图"),
    EARTHQUAKE_VILLAGES_DISTANCE(4, "暴雨避难场所分布图"),
    EARTHQUAKE_RESCUE_TEAM(5, "暴雨泥石流潜在隐患点及人口分布图"),
    EARTHQUAKE_CROPS(6, "暴雨地灾风险区分布图"),
    EARTHQUAKE_DX(7, "暴雨防汛物资分布图"),
    EARTHQUAKE_RESERVOIR(8, "暴雨救援队伍分布图"),
    EARTHQUAKE_DISTRIBUTION(9, "暴雨城市生命线工程分布图"),
    EARTHQUAKE_PUBLIC_PLACE(10, "暴雨附近水库分布图"),
    EARTHQUAKE_RESCUE_MATERIAL(11, "暴雨附近医院分布图"),
    EARTHQUAKE_IMPORTANT_OBJECTS(12, "暴雨滑坡潜在隐患点及农作物分布图"),
    EARTHQUAKE_RISK_AREA(13, "暴雨山洪潜在隐患点及农作物分布图"),
    EARTHQUAKE_SAFE_PLACE(14, "暴雨内涝潜在隐患点及农作物分布图"),
    EARTHQUAKE_TOURIST_SPOT(15, "暴雨泥石流潜在隐患点及农作物分布图");

    private final Integer num;
    private final String name;


}
