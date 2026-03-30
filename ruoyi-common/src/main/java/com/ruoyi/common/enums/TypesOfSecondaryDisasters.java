package com.ruoyi.common.enums;

/**
 * @ClassName TypesOfSecondaryDisasters
 * @Description
 * @Author Huang Yx
 * @Date 2026/3/18 13:17
 */
public enum TypesOfSecondaryDisasters {


    LANDSLIDE("滑坡"),                        // 滑坡
    DEBRIS_FLOW("泥石流"),                     // 泥石流
    TORRENTIAL_FLOOD("山洪"),                 // 山洪
    WATER_LOGGING("内涝");                    // 内涝

    private final String disasterName;

    TypesOfSecondaryDisasters(String disasterName) {
        this.disasterName = disasterName;
    }

    public String getDisasterName() {
        return disasterName;
    }



}
