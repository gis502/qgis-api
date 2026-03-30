package com.ruoyi.system.domain.entity.slave;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author: xiaodemos
 * @date: 2025-07-22 16:57
 * @description: 地质灾害风险区
 */

@Data
@TableName("xian_disaster_risk")
public class GeoDisasterRisk {

    @TableId
    private Integer id;
    @TableField("disaster_name")
    private String disasterName;
    @TableField("unit_code")
    private String unitCode;
    @TableField("risk_level")
    private String riskLevel;
    @TableField("area")
    private Double area;
    @TableField("province")
    private String province;
    @TableField("city")
    private String city;
    @TableField("county")
    private String county;
    @TableField("country")
    private String country;
    @TableField("village")
    private String village;
    @TableField("position")
    private String position;
    @TableField("resident_counts")
    private Integer residentCounts;
    @TableField("address_population")
    private Integer addressPopulation;
    @TableField("risk_property")
    private Integer riskProperty;
    @TableField("permanent_population")
    private Integer permanentPopulation;
    @TableField("housing")
    private Integer housing;
    @TableField("inspector_name")
    private String inspectorName;
    @TableField("inspector_tele")
    private String inspectorTele;
    @TableField("lon")
    private Double lon;
    @TableField("lat")
    private Double lat;


}
