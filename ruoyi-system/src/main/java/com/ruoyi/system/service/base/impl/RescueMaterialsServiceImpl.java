package com.ruoyi.system.service.base.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.system.domain.dto.base.HospitalDTO;
import com.ruoyi.system.domain.dto.base.RescueMaterialsDTO;
import com.ruoyi.system.domain.entity.base.Hospitals;
import com.ruoyi.system.domain.entity.base.RescueMaterials;
import com.ruoyi.system.mapper.base.RescueMaterialsMapper;
import com.ruoyi.system.service.base.IRescueMaterialsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName RescueMaterialsServiceImpl
 * @Description
 * @Author Huang Yx
 * @Date 2026/3/13 10:53
 */
@Slf4j
@Service
public class RescueMaterialsServiceImpl extends ServiceImpl<RescueMaterialsMapper, RescueMaterials> implements IRescueMaterialsService {

    @Autowired
    private RescueMaterialsMapper mapper;

    @Override
    public List<RescueMaterialsDTO> getRescueMaterialsByXA() {
        List<RescueMaterialsDTO> templist = new ArrayList<>();
        List<RescueMaterials> rescueMaterials = mapper.selectList(null);

        if (rescueMaterials == null || rescueMaterials.size() == 0) {
            return null;
        }

        for (RescueMaterials materials : rescueMaterials) {
            RescueMaterialsDTO dto = new RescueMaterialsDTO();
            dto.setStorePointName(materials.getName());
            dto.setStorePointAddress(materials.getAddress());
            dto.setStorePointNum(materials.getVolume().toString());
            dto.setStorePointDep(materials.getDepartment());
            dto.setGeom(materials.getGeom());
            templist.add(dto);
        }
        return templist;
    }
}
