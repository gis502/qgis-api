package com.ruoyi.system.domain.bo;

import com.ruoyi.system.domain.params.QgisArgs;
import lombok.Builder;
import lombok.Data;

/**
 * @ClassName DlqMessage
 * @Description 死信队列参数
 * @Author Huang Yx
 * @Date 2026/3/23 19:34
 */
@Data
@Builder
public class DlqMessage {

    private QgisArgs qgisArgs;       // 原始参数
    private String failReason;      // 失败原因
    private Long failTime;          // 失败时间戳
    private Integer retryCount;     // 已重试次数


}
