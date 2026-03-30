package com.ruoyi.system.domain.dto.pub;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName DZEqQueue
 * @Description 地震评估表
 * @Author Huang Yx
 * @Date 2026/2/11 9:18
 */

@Data
@AllArgsConstructor
public class DZEqQueueDTO {

    private String Id;
    private String event;
    private Integer batch;  // 批次
    private Integer type;   // 0：手动 1：自动
    private Integer state;  //评估状态（0未开始，1正在计算，2正常完成，3人工停止，4超时或异常结束, 5不计算）
    private Integer mode;   // 执行方式（0地震参数 1影响场）
    private LocalDateTime beginTime;    // 开始时间
    private LocalDateTime endTime;      // 结束时间
    private Double progress;        // 进度
    private String remark;  // 异常信息

}
