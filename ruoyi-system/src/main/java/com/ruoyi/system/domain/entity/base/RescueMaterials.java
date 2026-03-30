package com.ruoyi.system.domain.entity.base;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.locationtech.jts.geom.Geometry;

/**
 * @author zzw
 * @description: 救援物资实体
 * @date 2025/10/21 上午9:17
 */
@Data
@TableName("base.rescue_materials")
public class RescueMaterials {
    @TableId
    private Integer id;
    @TableField("name")
    private String name;
    @TableField("address")
    private String address;
    @TableField("type")
    private String type;
    @TableField("level")
    private String level;
    @TableField("volume")
    private Integer volume;
    @TableField("department")
    private String department;
    @TableField("tent")
    private Integer tent;
    @TableField("rubber_boat")
    private Integer rubberBoat;
    @TableField("generator")
    private Integer generator;
    @TableField("emergency_light")
    private Integer emergencyLight;
    @TableField("save_tool")
    private Integer saveTools;
    @TableField("province")
    private String province;
    @TableField("city")
    private String city;
    @TableField("county")
    private String county;
    @TableField("country")
    private String country;
    @TableField("unit_head")
    private String unitHead;
    @TableField("telephone")
    private String telephone;
    @TableField("lon")
    private Float longitude;
    @TableField("lat")
    private Float latitude;
    @TableField("point")
    private Geometry geom;
}
