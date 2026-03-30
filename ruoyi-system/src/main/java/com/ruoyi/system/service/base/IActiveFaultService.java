package com.ruoyi.system.service.base;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.dto.base.ActiveFaultDTO;
import com.ruoyi.system.domain.entity.base.ActiveFault;

/**
 * @ClassName IActiveFaultService
 * @Description 活动断层服务
 * @Author Huang Yx
 * @Date 2026/1/30 0:55
 */
public interface IActiveFaultService extends IService<ActiveFault> {

    // 查找距离震中最近的一条断层数据
    public ActiveFaultDTO getShortlyFault(double lon, double lat);


}
