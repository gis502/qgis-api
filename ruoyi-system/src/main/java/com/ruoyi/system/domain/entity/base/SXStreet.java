package com.ruoyi.system.domain.entity.base;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 西安街道实体类
 * 对应数据库表：xian_street
 */
@Data
@TableName("base.sx_street")
public class SXStreet {

    /**
     * 主键ID
     */
    @TableField("id")
    private Integer id;

    /**
     * 几何信息
     */
    @TableField("dgeom")
    private String dgeom;

    /**
     * 区县
     */
    @TableField("county")
    private String county;

    /**
     * 街道
     */
    @TableField("street")
    private String street;

    /**
     * 几何信息WKT格式
     */
    @TableField("geometry_wkt")
    private String geometryWkt;
}
