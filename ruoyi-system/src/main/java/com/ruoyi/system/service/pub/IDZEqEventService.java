package com.ruoyi.system.service.pub;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.dto.pub.EqTriggerDTO;
import com.ruoyi.system.domain.entity.pub.DZEqEvent;
import com.ruoyi.system.domain.query.EqQuery;

/**
 * @ClassName DZEqEventMapper
 * @Description 地震事件表
 * @Author Huang Yx
 * @Date 2026/2/11 9:56
 */

public interface IDZEqEventService extends IService<DZEqEvent> {

    // 地震触发
    public EqQuery trigger(EqTriggerDTO trigger);

    // 删除地震事件
    public Boolean deletedById(Long Id);
}
