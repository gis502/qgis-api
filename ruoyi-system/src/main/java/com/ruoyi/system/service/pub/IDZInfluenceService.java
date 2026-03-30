package com.ruoyi.system.service.pub;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.dto.dzxx.DZXXInfluenceDTO;
import com.ruoyi.system.domain.dto.pub.DZProductDTO;
import com.ruoyi.system.domain.entity.dzxx.DZXXInfluence;
import com.ruoyi.system.domain.entity.pub.DZInfluence;
import com.ruoyi.system.domain.query.EqQuery;
import com.ruoyi.system.domain.query.ProductQuery;

import java.util.List;
import java.util.Map;

/**
 * @ClassName IDZInfluenceService
 * @Description 地震影响场
 * @Author Huang Yx
 * @Date 2026/2/11 15:03
 */
public interface IDZInfluenceService extends IService<DZInfluence> {


    // 以文件形式 保存影响场
    public void handle(List<DZXXInfluenceDTO> dzxx);

    // 获取影响场文件
    public Map<String, String> getInfluence(EqQuery query);

}
