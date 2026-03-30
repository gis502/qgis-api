package com.ruoyi.system.service.dzxx;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.dto.dzxx.DZXXCenterDTO;
import com.ruoyi.system.domain.entity.dzxx.DZXXCenter;

/**
 * @ClassName IDZXXCenterService
 * @Description
 * @Author Huang Yx
 * @Date 2026/2/10 17:11
 */
public interface IDZXXCenterService extends IService<DZXXCenter> {



    // 地震触发
    public void handle(DZXXCenterDTO trigger);


}
