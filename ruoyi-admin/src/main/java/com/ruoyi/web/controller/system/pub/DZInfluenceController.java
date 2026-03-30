package com.ruoyi.web.controller.system.pub;

import com.ruoyi.common.constant.BaseConstants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.dto.pub.EqAssessmentDTO;
import com.ruoyi.system.domain.query.EqQuery;
import com.ruoyi.system.service.dzxx.IDZXXInfluenceService;
import com.ruoyi.system.service.pub.IDZInfluenceService;
import com.ruoyi.system.service.pub.IDZProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @ClassName DZInfluenceController
 * @Description
 * @Author Huang Yx
 * @Date 2026/3/23 17:48
 */
@Slf4j
@RestController
@Api(tags = "影响场控制类")
@RequestMapping("/api/open")
public class DZInfluenceController {

    @Autowired
    private IDZInfluenceService idzInfluenceService;
    @Autowired
    private IDZXXInfluenceService idzxxInfluenceService;

    @ApiOperation("获取影响场文件")
    @PostMapping("/influence")
    public AjaxResult getInfluence(@RequestBody @Validated EqQuery query) {
        Map<String, String> influence = idzInfluenceService.getInfluence(query);
        if (influence == null) {
            return AjaxResult.error(BaseConstants.RESULT_ERROR);
        }
        return AjaxResult.success(influence);
    }

    @ApiOperation("生成影响场文件")
    @PostMapping("/generate/influence")
    public AjaxResult generateInfluence(@RequestBody @Validated EqAssessmentDTO assess) {
        idzxxInfluenceService.handle(assess);
        return AjaxResult.success("地震影响场已生成!");
    }


}
