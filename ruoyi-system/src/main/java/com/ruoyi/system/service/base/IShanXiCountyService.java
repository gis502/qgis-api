package com.ruoyi.system.service.base;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.dto.base.ShanXiCountyDTO;
import com.ruoyi.system.domain.entity.base.SXCounty;

import java.util.List;

/**
 * @ClassName IShanXiCountyService
 * @Description
 * @Author Huang Yx
 * @Date 2026/2/9 15:08
 */
public interface IShanXiCountyService extends IService<SXCounty> {


    /**
     * 根据距离来查询附近公里的承载体数量
     *
     * @param dis 距离（km）
     * @return 返回极震区县区
     */
    public List<ShanXiCountyDTO> getMostIntensityAreaCounty(double dis, double lon, double lat);

}
