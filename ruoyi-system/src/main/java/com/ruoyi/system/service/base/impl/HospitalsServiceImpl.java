package com.ruoyi.system.service.base.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.system.domain.dto.base.HospitalDTO;
import com.ruoyi.system.domain.entity.base.Hospitals;
import com.ruoyi.system.mapper.base.HospitalsMapper;
import com.ruoyi.system.service.base.IHospitalsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName HospitalsServiceImpl
 * @Description
 * @Author Huang Yx
 * @Date 2026/3/13 10:20
 */
@Slf4j
@Service
public class HospitalsServiceImpl extends ServiceImpl<HospitalsMapper, Hospitals> implements IHospitalsService {

    // 获取所有医院
    @Override
    public List<HospitalDTO> getHospitalByXA() {
        List<HospitalDTO> templist = new ArrayList<>();
        List<Hospitals> hospitals = this.baseMapper.selectList(null);

        if (hospitals == null || hospitals.size() == 0) {
            return null;
        }

        for (Hospitals hospital : hospitals) {

            HospitalDTO dto = new HospitalDTO();
            dto.setHospitalAddress(hospital.getAddress());
            dto.setHospitalBeds(hospital.getBeds().toString());
            dto.setHospitalLevel(hospital.getLevel());
            dto.setHospitalName(hospital.getName());
            dto.setGeom(hospital.getGeom());
            templist.add(dto);
        }
        return templist;
    }
}
