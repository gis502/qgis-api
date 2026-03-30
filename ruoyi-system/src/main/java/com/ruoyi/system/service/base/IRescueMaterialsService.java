package com.ruoyi.system.service.base;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.dto.base.RescueMaterialsDTO;
import com.ruoyi.system.domain.dto.base.RescueTeamsDTO;
import com.ruoyi.system.domain.entity.base.RescueMaterials;

import java.util.List;

/**
 * @ClassName IRescueMaterialsService
 * @Description
 * @Author Huang Yx
 * @Date 2026/3/13 10:53
 */
public interface IRescueMaterialsService extends IService<RescueMaterials> {

    // 获取西安所有救援物资
    List<RescueMaterialsDTO> getRescueMaterialsByXA();

}
