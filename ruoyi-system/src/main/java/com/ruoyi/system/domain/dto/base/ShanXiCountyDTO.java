package com.ruoyi.system.domain.dto.base;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.locationtech.jts.geom.Geometry;

/**
 * @ClassName ShanXiCounty
 * @Description 陕西省区县
 * @Author Huang Yx
 * @Date 2026/2/9 12:31
 */
@Data
public class ShanXiCountyDTO {

    private String NAME;
    private Double X;
    private Double Y;
    @JsonSerialize(using = ToStringSerializer.class)
    private Geometry geometry;
    private Double distance;
}
