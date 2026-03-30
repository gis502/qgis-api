package com.ruoyi.system.service.pub;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.dto.dzxx.DZXXCenterDTO;
import com.ruoyi.system.domain.dto.pub.REventDTO;
import com.ruoyi.system.domain.dto.pub.RTriggerDTO;
import com.ruoyi.system.domain.entity.pub.REvent;
import com.ruoyi.system.domain.query.RQuery;

/**
 * @ClassName IREventService
 * @Description
 * @Author Huang Yx
 * @Date 2026/3/18 20:53
 */
public interface IREventService extends IService<REvent> {

    // 暴雨触发
    public RQuery trigger(RTriggerDTO trigger);

    // 删除暴雨事件
    public Boolean deletedById(Long Id);
}
