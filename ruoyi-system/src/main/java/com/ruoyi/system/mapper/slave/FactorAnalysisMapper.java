package com.ruoyi.system.mapper.slave;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.system.domain.dto.slave.FactorAnalysisSubDTO;
import com.ruoyi.system.domain.entity.slave.FactorAnalysis;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @ClassName FactorAnalysisMapper
 * @Description
 * @Author Huang Yx
 * @Date 2026/3/18 18:42
 */
@Mapper
public interface FactorAnalysisMapper extends BaseMapper<FactorAnalysis> {


    // 按hide_id去重查询暴雨灾害的因子分析数据
    @Select("${subSql}")
    List<FactorAnalysisSubDTO> factorAnalysisBySubSql(@Param("subSql") String subSql,
                                                      @Param("disasterId") Long disasterId);


}
