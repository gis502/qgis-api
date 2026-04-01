package com.ruoyi.system.domain.dto.pub;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author: xiaodemos
 * @date: 2025-04-05 22:27
 * @description: 评估DTO类
 */

@Data
public class EqAssessmentDTO {

    private String event;   // 地震编码
    private String eqQueueId;   // 评估编码
    private LocalDateTime eqTime; // 地震发生时间
    private Double longitude; // 经度
    private Double latitude; // 纬度
    private String eqAddr; // 地址
    private Double eqMagnitude; // 震级
    private Double eqDepth; // 深度
    private String eqFullName; // 震源名称
    private String eqName; // 震源名称
    private String eqType; // 震源类型

}
