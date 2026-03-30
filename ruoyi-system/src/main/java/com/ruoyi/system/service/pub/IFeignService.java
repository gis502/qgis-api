package com.ruoyi.system.service.pub;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.params.QgisArgs;

import java.util.List;

/**
 * @ClassName IFeignService
 * @Description 三方服务
 * @Author Huang Yx
 * @Date 2026/2/12 20:29
 */
public interface IFeignService {


    // 调用专题图
    public void invoke(List<QgisArgs> args);

}
