package com.ruoyi.system.service.slave;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.system.domain.entity.slave.GeoDisasterHide;

/**
 * @ClassName IGeoDisasterHideService
 * @Description
 * @Author Huang Yx
 * @Date 2026/3/18 19:03
 */
@DataSource(value = DataSourceType.SLAVE)
public interface IGeoDisasterHideService extends IService<GeoDisasterHide> {

    Integer getRiskNumByCounty(String county);

}
