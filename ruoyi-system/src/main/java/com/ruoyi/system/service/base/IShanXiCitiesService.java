package com.ruoyi.system.service.base;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.dto.base.ShanXiCitiesDTO;
import com.ruoyi.system.domain.entity.base.SXCities;

import java.util.List;

/**
 * @ClassName IShanXiCitiesService
 * @Description
 * @Author Huang Yx
 * @Date 2026/2/9 15:08
 */
public interface IShanXiCitiesService extends IService<SXCities> {


    // 查询距离震中最近的市州
    public List<ShanXiCitiesDTO> getMostIntensityAreaCities(double lon, double lat);

}
