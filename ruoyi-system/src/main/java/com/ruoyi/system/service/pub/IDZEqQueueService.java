package com.ruoyi.system.service.pub;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.dto.pub.EqAssessmentDTO;
import com.ruoyi.system.domain.dto.pub.RAssessmentDTO;
import com.ruoyi.system.domain.entity.pub.DZEqQueue;

/**
 * @ClassName DZEqQueueMapper
 * @Description 地震评估
 * @Author Huang Yx
 * @Date 2026/2/11 9:57
 */

public interface IDZEqQueueService extends IService<DZEqQueue> {

    // 地震评估
    public void assess(EqAssessmentDTO assess);

    // 暴雨评估
    public void assess(RAssessmentDTO assess);

    // 更新评估进度、状态
    public void updated(String event, String queueId, double progress, int state);

}
