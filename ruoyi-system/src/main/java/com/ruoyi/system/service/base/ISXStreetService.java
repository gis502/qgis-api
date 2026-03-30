package com.ruoyi.system.service.base;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.entity.base.SXStreet;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName ISXStreetService
 * @Description
 * @Author Huang Yx
 * @Date 2026/3/18 18:07
 */
public interface ISXStreetService extends IService<SXStreet> {

    public String inStreet(float lat,  float lon);

}
