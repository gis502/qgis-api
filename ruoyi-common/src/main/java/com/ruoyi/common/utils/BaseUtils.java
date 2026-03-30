package com.ruoyi.common.utils;

import com.ruoyi.common.constant.BaseConstants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author: xiaodemos
 * @date: 2025-04-05 18:38
 * @description: 基本工具类
 */


public class BaseUtils {

    // 生成一个带时间戳的地震编码
    public static String generationCode(LocalDateTime time) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = time.format(formatter);
        String code = "T" + timestamp + BaseConstants.CITY_CODE;

        return code;
    }

    // 格式化时间
    public static String formatTime(LocalDateTime time, boolean top) {
        // true 三要素 false 落款)
        if (top) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH时 mm分");
            String timestamp = time.format(formatter);
            return timestamp;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
        String timestamp = time.format(formatter);
        return timestamp;

    }

    // 首次生成批次编码
    public static String generationBatchCode(String code) {
        return code + "01";
    }

    // 修改地震批次编码
    public static String editBatchCode(String code) {

        String prefix = code.substring(0, code.length() - 2);
        String last = code.substring(code.length() - 2);
        return prefix + Integer.parseInt(last) + 1;
    }

    // 生成一个带时间戳的暴雨编码
    public static String generationRainCode(LocalDateTime time) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = time.format(formatter);
        String code = "R" + timestamp + BaseConstants.CITY_CODE;

        return code;
    }

    // 计算评估进度 0：地震、1：暴雨
    public static double compute(int finish, int type) {
        if (type == 0) {
            return (double) finish / BaseConstants.EQ_TOTAL_FILES * 100;
        }
        return (double) finish / BaseConstants.RAIN_TOTAL_FILES * 100;
    }

}
