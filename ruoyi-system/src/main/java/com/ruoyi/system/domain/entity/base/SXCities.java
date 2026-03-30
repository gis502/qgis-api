package com.ruoyi.system.domain.entity.base;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.locationtech.jts.geom.Geometry;

/**
 * @ClassName ShanXiCities
 * @Description 陕西省市区
 * @Author Huang Yx
 * @Date 2026/2/9 12:28
 */
@Data
@TableName("base.sx_zb_city")
public class SXCities {

    @TableField("\"SmUserID\"")
    private Integer SmUserID;
    @TableField("\"Field_SmUserID\"")
    private Integer Field_SmUserID;
    @TableField("\"CLASS\"")
    private String CLASS;
    @TableField("\"NAME\"")
    private String NAME;
    @TableField("\"NAMEABBR\"")
    private String NAMEABBR;
    @TableField("\"X\"")
    private Double X;
    @TableField("\"Y\"")
    private Double Y;
    @TableField("\"Geometry\"")
    @JsonSerialize(using = ToStringSerializer.class)
    private Geometry geometry;


}
