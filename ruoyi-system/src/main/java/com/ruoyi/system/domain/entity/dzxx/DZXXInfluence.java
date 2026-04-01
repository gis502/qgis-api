package com.ruoyi.system.domain.entity.dzxx;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.locationtech.jts.geom.Geometry;

import java.time.LocalDateTime;

/**
 * @ClassName DzxxInfluence
 * @Description 地震烈度影响场
 * @Author Huang Yx
 * @Date 2026/1/30 14:04
 */

@Data
@TableName("dzxx.dz_gis_influence")
public class DZXXInfluence {

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long Id;
    @TableField("geom")
    private Geometry geom; // 椭圆面积
    @TableField("eqqueue_id")
    private String eqQueueId;
    @TableField("event")
    private String event;
    @TableField("eqname")
    private String eqName;
    @TableField("inty")
    private Integer inty;   // 烈度值
    @TableField("sinty")    // 字符烈度值
    private String sInty;
    @TableField("type")
    private String type;
    @TableField("area")
    private Double area;   // 烈度区面积
    @TableField("long_uranium")
    private Double longUranium;
    @TableField("short_uranium")
    private Double shortUranium;
    @TableField("direction")
    private Double direction; // 偏转角度
    @TableLogic
    @TableField("del_flag")
    private Integer delFlag = 0;
    @TableField(value = "create_by",fill = FieldFill.INSERT_UPDATE)
    private String createBy;
    @TableField(value = "create_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime createTime;
    @TableField("remark")
    private String remark;
    @TableField("longitude")
    private Double longitude;
    @TableField("latitude")
    private Double latitude;

}
