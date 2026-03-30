package com.ruoyi.system.service.slave;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.system.domain.entity.slave.RAnalysis;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

/**
 * @ClassName IRAnalysisService
 * @Description
 * @Author Huang Yx
 * @Date 2026/3/18 17:47
 */
@DataSource(value = DataSourceType.SLAVE)
public interface IRAnalysisService extends IService<RAnalysis> {

    List<RAnalysis> getRainStation(String adminCode);

}
