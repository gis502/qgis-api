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
    private LocalDateTime eqTime;
    private Double longitude;
    private Double latitude;
    private String eqAddr;
    private Double eqMagnitude;
    private Double eqDepth;
    private String eqFullName;
    private String eqName;
    private String eqType;

}
