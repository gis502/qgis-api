package com.ruoyi.system.service.base.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.system.domain.dto.base.HospitalDTO;
import com.ruoyi.system.domain.dto.base.RescueTeamsDTO;
import com.ruoyi.system.domain.entity.base.Hospitals;
import com.ruoyi.system.domain.entity.base.RescueTeams;
import com.ruoyi.system.mapper.base.RescueTeamsMapper;
import com.ruoyi.system.service.base.IRescueTeamsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName RescueTeamsServiceImpl
 * @Description
 * @Author Huang Yx
 * @Date 2026/3/12 14:51
 */
@Slf4j
@Service
public class RescueTeamsServiceImpl extends ServiceImpl<RescueTeamsMapper, RescueTeams> implements IRescueTeamsService {

    @Override
    public List<RescueTeamsDTO> getRescueTeamsByXA() {
        List<RescueTeamsDTO> templist = new ArrayList<>();
        List<RescueTeams> rescueTeams = this.baseMapper.selectList(null);

        if (rescueTeams == null || rescueTeams.size() == 0) {
            return null;
        }

        for (RescueTeams team : rescueTeams) {

            RescueTeamsDTO dto = new RescueTeamsDTO();
            dto.setFireFighterName(team.getTeamName());
            dto.setFireFighterType(team.getTeamType());
            dto.setFireFighterAddress(team.getAddress());
            dto.setFireFighterNum(team.getTeamNum().toString());
            dto.setGeom(team.getGeom());
            templist.add(dto);
        }
        return templist;
    }
}
