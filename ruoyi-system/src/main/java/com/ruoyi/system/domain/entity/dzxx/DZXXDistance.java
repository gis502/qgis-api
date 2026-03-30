package com.ruoyi.system.domain.entity.dzxx;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.locationtech.jts.geom.Geometry;

import java.time.LocalDateTime;

/**
 * @ClassName DZXXDistance
 * @Description 震中距省市县乡镇距离
 * @Author Huang Yx
 * @Date 2026/2/9 11:51
 */
@Data
@TableName("dzxx.dz_gis_distance")
public class DZXXDistance {

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long Id;
    @TableField("geom")
    private Geometry geom;
    @TableField("eqqueue_id")
    private String eqQueueId;
    @TableField("distance_id")
    private Integer distanceId;
    @TableField("pid")
    private String pId;
    @TableField("pname")
    private String pname;
    @TableField("bgmc")
    private String bgmc;
    @TableField("distance")
    private Double distance;
    @TableLogic
    @TableField("del_flag")
    private Integer delFlag = 0;
    @TableField(value = "create_by",fill = FieldFill.INSERT_UPDATE)
    private String createBy;
    @TableField(value = "create_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime createTime;
    @TableField("remark")
    private String remark;

}
