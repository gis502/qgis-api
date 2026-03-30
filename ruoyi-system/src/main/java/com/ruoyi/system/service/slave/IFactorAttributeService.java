package com.ruoyi.system.service.slave;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.system.domain.entity.slave.FactorAttribute;

import java.util.List;

/**
 * @ClassName IFactorAttributeService
 * @Description
 * @Author Huang Yx
 * @Date 2026/3/18 19:08
 */
@DataSource(value = DataSourceType.SLAVE)
public interface IFactorAttributeService extends IService<FactorAttribute> {

    List<String> getFactorAttributeName();

}
