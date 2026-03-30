package com.ruoyi.system.domain.entity.slave;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author: xiaodemos
 * @date: 2025-01-27
 * @description: 西安影响区域实体类
 */

@Data
@TableName("xian_impact_in_area")
public class ImpactInArea {

    @TableId(type = IdType.AUTO)
    private Integer id;
    
    @TableField("disaster_id")
    private Integer disasterId;
    
    @TableField("second_disaster_id")
    private Integer secondDisasterId;
    
    @TableField("people")
    private Long people;
    
    @TableField("national_road")
    private Integer nationalRoad;
    
    @TableField("heightway")
    private Integer heightway;
    
    @TableField("street")
    private Integer street;
    
    @TableField("dangerous_point")
    private Integer dangerousPoint;
    
    @TableField("station")
    private Integer station;
    
    @TableField("dangerous_point_pos")
    private String dangerousPointPos;
    
    @TableField("station_pos")
    private String stationPos;
    
    @TableField("district")
    private String district;

    // 注释掉原来的Map类型字段
    //@TableField("impact_json")
    //private Map<String, List<Object>> impactJson;
    
    // 添加字符串类型字段来存储JSON字符串
    @TableField("impact_json")
    private String impactJsonStr;
}