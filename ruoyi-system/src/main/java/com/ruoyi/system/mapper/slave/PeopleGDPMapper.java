package com.ruoyi.system.mapper.slave;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.system.domain.entity.base.PeopleGDP;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.locationtech.jts.geom.Geometry;

import java.util.List;
@DataSource(value = DataSourceType.SLAVE)
@Mapper
public interface PeopleGDPMapper extends BaseMapper<PeopleGDP> {
    /**
     * 查询椭圆范围内的人口数和GDP数量
     * @param geom 人口伤亡烈度计算位置
     */
    List<PeopleGDP> findInsideCircle(@Param("Geometry") Geometry geom);
}
