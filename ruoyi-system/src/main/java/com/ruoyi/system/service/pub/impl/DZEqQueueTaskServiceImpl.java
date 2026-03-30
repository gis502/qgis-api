package com.ruoyi.system.service.pub.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.enums.EqMapsEnums;
import com.ruoyi.system.domain.entity.pub.DZEqQueueTask;
import com.ruoyi.system.domain.entity.pub.DZEqQueueTaskDTO;
import com.ruoyi.system.mapper.pub.DZEqQueueTaskMapper;
import com.ruoyi.system.service.pub.IDZEqQueueTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName DZEqQueueTaskServiceImpl
 * @Description
 * @Author Huang Yx
 * @Date 2026/3/23 18:38
 */
@Slf4j
@Service
public class DZEqQueueTaskServiceImpl extends ServiceImpl<DZEqQueueTaskMapper, DZEqQueueTask> implements IDZEqQueueTaskService {

    // 初始化任务队列
    @Async("taskExecutor")
    @Override
    public void initTaskQueue(int disaster) {   // 0:地震，1:暴雨
        log.info("正在初始化队列...");
        List<DZEqQueueTask> tasks = new ArrayList<>();
        // 地震
        if (disaster == 0) {
            tasks = addTaskByEarthquake();
        } else {
            // 暴雨
            tasks = addTaskByRainstorm();
        }
        // 批量保存
        saveBatch(tasks);
        log.info("初始化任务队列完成！");
    }

    // 添加任务队列
    @Override
    public void startTaskQueue() {

    }

    // 修改任务
    @Override
    public void updateTaskQueue() {

    }


    /**
     * 专题图所需：
     * taskId、taskName、templatePath、outputPath、
     *
     *
     *
     */



    private List<DZEqQueueTask> addTaskByEarthquake() {
        // 任务队
        List<DZEqQueueTask> tasks = new ArrayList<>();
        // 添加影响场任务
        DZEqQueueTask influenceTask = new DZEqQueueTask();
        tasks.add(influenceTask);
        // 添加专题图任务
        List<EqMapsEnums> maps = Arrays.asList(EqMapsEnums.values());
        for (EqMapsEnums map : maps) {
            DZEqQueueTask mapTask = new DZEqQueueTask();
            tasks.add(mapTask);
        }
        // 添加灾情报告任务
        DZEqQueueTask reportTask = new DZEqQueueTask();
        tasks.add(reportTask);

        return null;
    }

    private List<DZEqQueueTask> addTaskByRainstorm() {

        // 添加专题图任务
        // 添加灾情报告任务

        return null;
    }
}
