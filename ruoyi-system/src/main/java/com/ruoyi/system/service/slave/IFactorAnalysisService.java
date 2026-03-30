package com.ruoyi.system.service.slave;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.system.domain.entity.slave.FactorAnalysis;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @ClassName IFactorAnalysisService
 * @Description
 * @Author Huang Yx
 * @Date 2026/3/18 18:42
 */
@DataSource(value = DataSourceType.SLAVE)
public interface IFactorAnalysisService extends IService<FactorAnalysis> {

    List<Map<String, Object>> queryDisasterEstimation(Long disasterId);

}
