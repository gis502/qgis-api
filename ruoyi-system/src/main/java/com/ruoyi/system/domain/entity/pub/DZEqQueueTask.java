package com.ruoyi.system.domain.entity.pub;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName DZEqQueueTask
 * @Description 地震评估任务列表
 * @Author Huang Yx
 * @Date 2026/2/11 9:32
 */
@Data
@TableName("public.dz_eqqueue_task")
public class DZEqQueueTask {

    @TableField("id")
    private String Id;
    @TableField("eqqueue_id")
    private String queueId;
    @TableField("module")
    private String module;
    @TableField("tagid")
    private String tagId;
    @TableField("state")
    private Integer state;
    @TableField("progress")
    private Double progress;
    @TableField("begin_time")
    private LocalDateTime beginTime;
    @TableField("end_time")
    private LocalDateTime endTime;
    @TableField("lev")
    private Integer lev;
    @TableLogic
    @TableField("del_flag")
    private Integer delFlag = 0;
    @TableField(value = "create_by",fill = FieldFill.INSERT_UPDATE)
    private String createBy;
    @TableField(value = "create_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime createTime;
    @TableField(value = "create_dept",fill = FieldFill.INSERT_UPDATE)
    private Integer createDept;
    @TableField(value = "update_by",fill = FieldFill.INSERT_UPDATE)
    private String updateBy;
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableField("remark")
    private String remark;
    @TableField("type")
    private Integer type;


}
