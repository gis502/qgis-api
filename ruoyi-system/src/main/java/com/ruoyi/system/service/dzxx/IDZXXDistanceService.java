package com.ruoyi.system.service.dzxx;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.entity.dzxx.DZXXDistance;

/**
 * @ClassName IDZXXDistanceService
 * @Description 震中到省市县乡距离
 * @Author Huang Yx
 * @Date 2026/2/10 12:06
 */
public interface IDZXXDistanceService extends IService<DZXXDistance> {

    // 处理震中到省市区镇
    public void handle(double lon, double lat, String eqQueueId);

}
