package com.ruoyi.system.domain.entity.pub;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName DZEqQueueTaskDTO
 * @Description
 * @Author Huang Yx
 * @Date 2026/3/23 18:48
 */
@Data
public class DZEqQueueTaskDTO {

    private String queueId;     // 任务ID
    private String module;      // 任务模块(base基础计算、books书签、map专题图、report报告、info信息发布)
    private String tagId;       // 任务标识(如模型编码产品编码)
    private Integer state;      // 任务状态（0未开始，1正在计算，2正常完成，3人工停止，4超时或异常结束）
    private Double progress;    // 任务进度
    private Integer lev;        // 任务等级优先(从小到大)
    private String remark;      // 备注
    private Integer type;       // 启动类型（0自动,1手动）
    private LocalDateTime beginTime;
    private LocalDateTime endTime;


}
