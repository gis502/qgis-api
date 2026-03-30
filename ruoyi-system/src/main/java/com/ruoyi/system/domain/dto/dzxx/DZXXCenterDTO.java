package com.ruoyi.system.domain.dto.dzxx;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName DZXXCenterDTO
 * @Description 地震信息触发类
 * @Author Huang Yx
 * @Date 2026/2/10 17:25
 */
@Data
public class DZXXCenterDTO {

    private String event;
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
