package com.ruoyi.system.domain.dto.base;

import lombok.Data;
import org.locationtech.jts.geom.Geometry;

/**
 * @ClassName HospitalDTO
 * @Description
 * @Author Huang Yx
 * @Date 2026/3/13 10:30
 */

@Data
public class HospitalDTO {

    private String hospitalName;
    private String hospitalBeds;
    private String hospitalAddress;
    private String hospitalLevel;
    private Geometry geom;
    private int km;

}
