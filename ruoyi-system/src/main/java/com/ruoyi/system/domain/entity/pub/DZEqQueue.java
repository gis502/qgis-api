package com.ruoyi.system.domain.entity.pub;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.File;
import java.time.LocalDateTime;

/**
 * @ClassName DZEqQueue
 * @Description 地震评估表
 * @Author Huang Yx
 * @Date 2026/2/11 9:18
 */

@Data
@TableName("public.dz_eqqueue")
public class DZEqQueue {

    @TableField(value = "id")
    private String id;  // eqqueueid
    @TableField("event")
    private String event;
    @TableField("batch")
    private Integer batch;
    @TableField("type")
    private Integer type;
    @TableField("state")
    private Integer state;  //评估状态（0未开始，1正在计算，2正常完成，3人工停止，4超时或异常结束, 5不计算）
    @TableField("mode")
    private Integer mode = 1;   // 执行方式（0地震参数 1影响场）
    @TableField("begin_time")
    private LocalDateTime beginTime;
    @TableField("end_time")
    private LocalDateTime endTime;
    @TableField("progress")
    private Double progress;
    @TableLogic
    @TableField(value = "del_flag", fill = FieldFill.INSERT_UPDATE)
    private Integer delFlag;
    @TableField(value = "create_by", fill = FieldFill.INSERT_UPDATE)
    private String createBy;
    @TableField(value = "create_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime createTime;
    @TableField(value = "create_dept", fill = FieldFill.INSERT_UPDATE)
    private Integer createDept;
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @Version
    @TableField(value = "version", fill = FieldFill.INSERT_UPDATE)
    private Integer version;    // 版本号
    @TableField("remark")
    private String remark;

}
