package com.ruoyi.system.domain.entity.slave;

import com.baomidou.mybatisplus.annotation.*;
import com.ruoyi.system.handler.GeometryTypeHandler;
import lombok.Data;
import org.locationtech.jts.geom.Geometry;

import java.time.LocalDateTime;

/**
 * @ClassName REvent
 * @Description 暴雨事件
 * @Author Huang Yx
 * @Date 2026/3/18 15:42
 */
@Data
@TableName("xian_disaster_rain")
public class RSEvent {

    @TableId(value = "disaster_id", type = IdType.AUTO) // 只有写上这个才能从数据库里读出来正确的id
    @TableField("disaster_id")
    private Long disasterId;
    @TableField("disaster_name")
    private String disasterName;
    @TableField("occurrence_time")
    private LocalDateTime occurrenceTime;
    @TableField(value = "geom", typeHandler = GeometryTypeHandler.class)
    private Geometry geom; //经纬度
    @TableField("longitude")
    private Double longitude;
    @TableField("latitude")
    private Double latitude;
    @TableField("rainfall")
    private String rainfall;
    @TableField("duration")
    private String duration;
    @TableField("position")
    private String position;
    @TableField("rain_type")
    private String rainType;
    @TableField(value = "create_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime createTime;
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableField("is_deleted")
    private Integer delFlag = 0;


}
