package com.ruoyi.system.service.pub;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.entity.pub.DZEqQueueTask;

/**
 * @ClassName DZEqQueueTaskMapper
 * @Description
 * @Author Huang Yx
 * @Date 2026/2/11 9:57
 */

public interface IDZEqQueueTaskService extends IService<DZEqQueueTask> {

    // 初始化队列任务
    public void initTaskQueue(int disaster);  // 0:地震，1:暴雨

    // 添加任务
    public void startTaskQueue();

    // 修改任务情况
    public void updateTaskQueue();

}
