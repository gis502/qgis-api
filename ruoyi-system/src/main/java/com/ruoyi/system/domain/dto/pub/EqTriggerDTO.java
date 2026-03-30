package com.ruoyi.system.domain.dto.pub;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author: xiaodemos
 * @date: 2025-04-05 11:20
 * @description: 地震触发DTO类
 */

@Data
public class EqTriggerDTO {

    private String eqName;
    private String eqAddr;
    private LocalDateTime eqTime;
    private Double longitude;
    private Double latitude;
    private Double eqDepth;
    private Double eqMagnitude;
    private String eqFullName;
    private String eqType;
}
