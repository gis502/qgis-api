package com.ruoyi.system.domain.entity.base;

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
@TableName("base.sx_zb_county")
public class SXCounty {


    @TableField("\"SmUserID\"")
    private Integer SmUserID;
    @TableField("\"Field_SmUserID\"")
    private Integer Field_SmUserID;
    @TableField("\"Field_Field_SmUserID\"")
    private Integer Field_Field_SmUserID;
    @TableField("\"CLASS\"")
    private String CLASS;
    @TableField("\"NAME\"")
    private String NAME;
    @TableField("\"PINYIN\"")
    private String PINYIN;
    @TableField("\"GNID\"")
    private Integer GNID;
    @TableField("\"XZNAME\"")
    private String XZNAME;
    @TableField("\"X\"")
    private Double X;
    @TableField("\"Y\"")
    private Double Y;
    @TableField("\"Geometry\"")
    @JsonSerialize(using = ToStringSerializer.class)
    private Geometry geometry;



}
