package com.ruoyi.system.service.base;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.dto.base.RescueTeamsDTO;
import com.ruoyi.system.domain.entity.base.RescueTeams;

import java.util.List;

/**
 * @ClassName IRescueTeamsService
 * @Description
 * @Author Huang Yx
 * @Date 2026/3/12 14:51
 */
public interface IRescueTeamsService extends IService<RescueTeams> {

    // 获取西安所有救援物资
    List<RescueTeamsDTO> getRescueTeamsByXA();

}
