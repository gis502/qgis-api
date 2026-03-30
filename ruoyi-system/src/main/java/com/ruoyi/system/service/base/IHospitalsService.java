package com.ruoyi.system.service.base;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.dto.base.HospitalDTO;
import com.ruoyi.system.domain.entity.base.Hospitals;

import java.util.List;

/**
 * @ClassName IHospitalsService
 * @Description
 * @Author Huang Yx
 * @Date 2026/3/13 10:19
 */
public interface IHospitalsService extends IService<Hospitals> {

    // 获取西安所有医院
    List<HospitalDTO> getHospitalByXA();

}
