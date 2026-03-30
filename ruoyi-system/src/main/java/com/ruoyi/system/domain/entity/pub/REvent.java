package com.ruoyi.system.domain.entity.pub;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.system.handler.GeometryTypeHandler;
import lombok.Data;
import org.locationtech.jts.geom.Geometry;

import java.time.LocalDateTime;

/**
 * @ClassName REvent
 * @Description 暴雨表
 * @Author Huang Yx
 * @Date 2026/3/18 20:46
 */
@Data
@TableName("public.r_event")
public class REvent {

    @TableId
    @TableField("id")
    private Long Id;
    @TableField("rain_id")
    private String rainId;
    @TableField("rain_queue_id")
    private String rainQueueId;
    @TableField("disaster_name")
    private String disasterName;
    @TableField("position")
    private String position;
    @TableField("occurrence_time")
    private LocalDateTime occurrenceTime;
    @TableField("rainfall")
    private String rainfall;
    @TableField("duration")
    private String duration;
    @TableField(value = "geom", typeHandler = GeometryTypeHandler.class)
    private Geometry geom; //经纬度
    @TableField("longitude")
    private Double longitude;
    @TableField("latitude")
    private Double latitude;
    @TableField("rain_type")
    private String rainType;
    @TableField(value = "create_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime createTime;
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableField("is_deleted")
    private Integer delFlag = 0;

}
