package com.ruoyi.system.domain.dto.base;

import lombok.Data;
import org.locationtech.jts.geom.Geometry;

/**
 * @ClassName FireFighterDTO
 * @Description
 * @Author Huang Yx
 * @Date 2026/3/13 10:30
 */
@Data
public class RescueTeamsDTO {

    private String fireFighterName;
    private String fireFighterType;
    private String fireFighterAddress;
    private String fireFighterNum;
    private Geometry geom;
    private int km;
}
