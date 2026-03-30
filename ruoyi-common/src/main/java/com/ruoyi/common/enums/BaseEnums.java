package com.ruoyi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @ClassName BaseEnums
 * @Description 通用枚举
 * @Author Huang Yx
 * @Date 2026/2/12 10:01
 */
@Getter
@AllArgsConstructor
public enum BaseEnums {
    // 评估枚举
    NOT_STARTED(0, "未开始"),
    CALCULATING(1, "正在计算"),
    NORMAL_COMPLETED(2, "正常完成"),
    MANUAL_STOPPED(3, "人工停止"),
    TIMEOUT_OR_EXCEPTION(4, "超时或异常结束"),
    NOT_CALCULATE(5, "不计算"),

    // 制图枚举
    NO(10, "不缩放"),
    PAN(11, "平移"),
    LAYER(12, "单图层"),
    M_LAYER(13, "多图层"),
    DISTANCE(14, "距离"), //  需要数值。
    M_LAYER2(15, "按图层合并缩放");


    private final Integer code;
    private final String desc;
}
