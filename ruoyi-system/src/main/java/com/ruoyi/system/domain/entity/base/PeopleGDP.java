package com.ruoyi.system.domain.entity.base;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;
import lombok.Data;

/**
 * @author zzw
 * @description: 地震影响人口+GDP
 * @date 2026/4/1 上午10:31
 */

@DataSource(value = DataSourceType.SLAVE)
@Data
@TableName("xian_gdp_people")
public class PeopleGDP {

    /**主键ID*/
    @TableId("id")
    private Integer id;

    /**人均GDP*/
    @TableField("gdp")
    private Float gdp;

    /**人口数量*/
    @TableField("people_num")
    private Integer peopleNum;

    /**省份*/
    @TableField("province")
    private String province;

    /**城市*/
    @TableField("city")
    private String city;

    /**区县*/
    @TableField("county")
    private String county;

    /**村庄（街道）*/
    @TableField("country")
    private String country;

    /**区县编码*/
    @TableField("country_code")
    private Integer countryCode;

    /**位置（几何类型）*/
    @TableField("polygon")
    private String polygon;

    private String pointGeom;
}
