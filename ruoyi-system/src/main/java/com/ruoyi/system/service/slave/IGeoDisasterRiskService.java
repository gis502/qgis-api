package com.ruoyi.system.service.slave;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.system.domain.entity.slave.GeoDisasterRisk;

/**
 * @ClassName IGeoDisasterRiskService
 * @Description
 * @Author Huang Yx
 * @Date 2026/3/18 18:58
 */
@DataSource(value = DataSourceType.SLAVE)
public interface IGeoDisasterRiskService extends IService<GeoDisasterRisk> {

    Integer getHideNumByCounty(String county);


}
