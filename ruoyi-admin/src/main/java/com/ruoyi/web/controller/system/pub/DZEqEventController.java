package com.ruoyi.web.controller.system.pub;

import com.ruoyi.common.constant.BaseConstants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.dto.pub.EqTriggerDTO;
import com.ruoyi.system.domain.dto.pub.RTriggerDTO;
import com.ruoyi.system.domain.query.EqQuery;
import com.ruoyi.system.domain.query.RQuery;
import com.ruoyi.system.service.pub.IDZEqEventService;
import com.ruoyi.system.service.pub.IREventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName DZEqEventController
 * @Description
 * @Author Huang Yx
 * @Date 2026/3/23 20:08
 */
@Slf4j
@RestController
@Api(tags = "地震事件控制类")
@RequestMapping("/api/open")
public class DZEqEventController {
    @Autowired
    private IDZEqEventService idzEqEventService;

    @ApiOperation("地震事件触发接口")
    @PostMapping("/eq/trigger")
    public AjaxResult trigger(@RequestBody @Validated EqTriggerDTO trigger) {
        EqQuery query = idzEqEventService.trigger(trigger);
        if (query == null) {
            return AjaxResult.error(BaseConstants.RESULT_ERROR);
        }
        return AjaxResult.success(query);
    }


    @ApiOperation("地震事件删除接口")
    @PostMapping("/eq/delete/{Id}")
    public AjaxResult delete(@PathVariable Long Id) {
        Boolean deleted = idzEqEventService.deletedById(Id);
        if (!deleted) {
            return AjaxResult.error("删除失败！");
        }
        return AjaxResult.success("删除成功！");
    }

}
