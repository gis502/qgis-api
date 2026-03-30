package com.ruoyi.system.service.dzxx;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.dto.dzxx.DZXXInfluenceDTO;
import com.ruoyi.system.domain.dto.pub.EqAssessmentDTO;
import com.ruoyi.system.domain.entity.dzxx.DZXXInfluence;
import com.ruoyi.system.domain.query.EqQuery;

import java.util.List;

/**
 * @ClassName IDZXXInfluenceService
 * @Description 地震影响场接口
 * @Author Huang Yx
 * @Date 2026/1/30 14:34
 */
public interface IDZXXInfluenceService extends IService<DZXXInfluence> {

    // 处理地震影响场数据
    public void handle(EqAssessmentDTO trigger);

    // 根据地震编码查询影响场范围
    public List<DZXXInfluenceDTO> findInfluenceById(EqQuery query);

    // 获取最大烈度影响场
    public DZXXInfluenceDTO findInfluenceMaxIntyById(EqQuery query);

}
