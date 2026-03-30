package com.ruoyi.system.domain.dto.base;

import lombok.Data;
import org.locationtech.jts.geom.Geometry;

/**
 * @ClassName StorePointDTO
 * @Description
 * @Author Huang Yx
 * @Date 2026/3/13 10:30
 */
@Data
public class RescueMaterialsDTO {
    private String storePointName;
    private String storePointAddress;
    private String storePointDep;
    private String storePointNum;
    private Geometry geom;
    private int km;
}
