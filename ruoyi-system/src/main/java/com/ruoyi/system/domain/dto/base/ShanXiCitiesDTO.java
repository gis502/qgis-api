package com.ruoyi.system.domain.dto.base;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.locationtech.jts.geom.Geometry;

/**
 * @ClassName ShanXiCitiesDTO
 * @Description 陕西市州
 * @Author Huang Yx
 * @Date 2026/2/9 14:49
 */
@Data
public class ShanXiCitiesDTO {

    private String NAME;
    private Double X;
    private Double Y;
    @JsonSerialize(using = ToStringSerializer.class)
    private Geometry geometry;
    private Double distance;
}
