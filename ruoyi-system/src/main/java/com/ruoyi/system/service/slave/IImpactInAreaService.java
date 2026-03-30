package com.ruoyi.system.service.slave;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.system.domain.entity.slave.ImpactInArea;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @ClassName IImpactInAreaService
 * @Description
 * @Author Huang Yx
 * @Date 2026/3/18 19:23
 */
@DataSource(value = DataSourceType.SLAVE)
public interface IImpactInAreaService extends IService<ImpactInArea> {

    Long getPeopleByLatLon(String lat, String lon);


}
