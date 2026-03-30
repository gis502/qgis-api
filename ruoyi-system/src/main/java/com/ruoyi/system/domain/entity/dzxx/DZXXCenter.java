package com.ruoyi.system.domain.entity.dzxx;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.locationtech.jts.geom.Geometry;

import java.time.LocalDateTime;

/**
 * @ClassName DZXXCenter
 * @Description 地震信息震中点
 * @Author Huang Yx
 * @Date 2026/2/9 11:36
 */
@Data
@TableName("dzxx.dz_gis_center")
public class DZXXCenter {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long Id;

    @TableField("geom")
    private Geometry geom;

    @TableField("event")
    private String event;

    @TableField("eq_time")
    private LocalDateTime eqTime;

    @TableField("eq_addr")
    private String eqAddr;

    @TableField("longitude")
    private Double longitude;

    @TableField("latitude")
    private Double latitude;

    @TableField("eq_magnitude")
    private Double eqMagnitude;

    @TableField("eq_depth")
    private Double eqDepth;

    @TableField("eq_full_name")
    private String eqFullName;

    @TableField("eq_name")
    private String eqName;

    @TableField("eq_type")
    private String eqType;

    @TableField("eq_addr_code")
    private String eqAddrCode;

    @TableField("town_code")
    private String townCode;

    @TableLogic
    @TableField("del_flag")
    private Integer delFlag = 0;

    @TableField(value = "create_by", fill = FieldFill.INSERT_UPDATE)
    private String createBy;

    @TableField(value = "create_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime createTime;

    @TableField("remark")
    private String remark;


}
