package com.ruoyi.system.domain.dto.pub;

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
 * @Description 暴雨信息
 * @Author Huang Yx
 * @Date 2026/3/18 20:46
 */
@Data
public class REventDTO {

    private String rainId;
    private String rainQueueId;
    private String disasterName;
    private String position;
    private LocalDateTime occurrenceTime;
    private String rainfall;
    private String duration;
    private Geometry geom;   //经纬度
    private Double longitude;
    private Double latitude;
    private String rainType;
}
