package com.ruoyi.system.domain.entity.base;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.locationtech.jts.geom.Geometry;

/**
 * @ClassName RescueTeams
 * @Description 救援队伍
 * @Author Huang Yx
 * @Date 2026/3/12 14:49
 */
@Data
@TableName("base.rescue_teams")
public class RescueTeams {

    @TableId
    private Integer id;
    @TableField("team_name")
    private String teamName;
    @TableField("team_type")
    private String teamType;
    @TableField("fire_type")
    private String fireType;
    @TableField("address")
    private String address;
    @TableField("team_num")
    private Integer teamNum;
    @TableField("cars")
    private Integer fireCars;
    @TableField("devices")
    private Integer fireDevices;
    @TableField("unit_head")
    private String unitHead;
    @TableField("telephone")
    private String telephone;
    @TableField("province")
    private String province;
    @TableField("city")
    private String city;
    @TableField("county")
    private String county;
    @TableField("country")
    private String country;
    @TableField("lon")
    private Double longitude;
    @TableField("lat")
    private Double latitude;
    @TableField("point")
    private Geometry geom;

}
