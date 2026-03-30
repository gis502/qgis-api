package com.ruoyi.system.service.base;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.dto.base.ShanXiTownsDTO;
import com.ruoyi.system.domain.entity.base.SXTowns;

import java.util.List;

/**
 * @ClassName IShanXiTownsService
 * @Description
 * @Author Huang Yx
 * @Date 2026/2/9 15:09
 */
public interface IShanXiTownsService extends IService<SXTowns> {

    /**
     * 根据来查询附近公里的承载体数量
     * @param dis 距离（km）
     * @return 返回极震区乡镇
     */
    public List<ShanXiTownsDTO> getMostIntensityAreaTowns(double dis, double lon, double lat);

}
