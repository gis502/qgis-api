package com.ruoyi.web.controller.system.base;

import com.ruoyi.common.constant.BaseConstants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.system.domain.dto.base.ShanXiCitiesDTO;
import com.ruoyi.system.domain.dto.base.ShanXiCountyDTO;
import com.ruoyi.system.domain.dto.base.ShanXiTownsDTO;
import com.ruoyi.system.domain.dto.pub.*;
import com.ruoyi.system.domain.query.EqQuery;
import com.ruoyi.system.domain.query.ProductQuery;
import com.ruoyi.system.service.base.IShanXiCitiesService;
import com.ruoyi.system.service.base.IShanXiCountyService;
import com.ruoyi.system.service.base.IShanXiTownsService;
import com.ruoyi.system.service.dzxx.IDZXXInfluenceService;
import com.ruoyi.system.service.pub.IDZEqEventService;
import com.ruoyi.system.service.pub.IDZInfluenceService;
import com.ruoyi.system.service.pub.IDZProductService;
import com.ruoyi.system.service.pub.IREventService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @ClassName TESTController
 * @Description
 * @Author Huang Yx
 * @Date 2026/1/30 1:56
 */

@RestController
@RequestMapping("/test")
public class TESTController {

    @Autowired
    private IDZXXInfluenceService iActiveFaultService;
    @Autowired
    private IShanXiCitiesService iShanXiCitiesService;
    @Autowired
    private IShanXiCountyService iShanXiCountyService;
    @Autowired
    private IShanXiTownsService iShanXiTownsService;
    @Autowired
    private IDZEqEventService idzEqEventService;
    @Autowired
    private IDZXXInfluenceService idzxxInfluenceService;
    @Autowired
    private IDZProductService idzProductService;
    @Autowired
    private IREventService irEventService;
    @Autowired
    private IDZInfluenceService idzInfluenceService;
    @PostMapping("/1")
    public AjaxResult test(@RequestBody EqAssessmentDTO dto) {

        iActiveFaultService.handle(dto);

        return AjaxResult.success("地震影响场已生成!");
    }

    @PostMapping("/2")
    public AjaxResult test1(@RequestParam double lon, @RequestParam double lat) {
        List<ShanXiCitiesDTO> mostIntensityAreaCities = iShanXiCitiesService.getMostIntensityAreaCities(lon, lat);
        return AjaxResult.success(mostIntensityAreaCities);
    }

    @PostMapping("/3")
    public AjaxResult test2(@RequestParam double dis, @RequestParam double lon, @RequestParam double lat) {
        List<ShanXiCountyDTO> mostIntensityAreaCounty = iShanXiCountyService.getMostIntensityAreaCounty(dis, lon, lat);
        return AjaxResult.success(mostIntensityAreaCounty);
    }

    @PostMapping("/4")
    public AjaxResult test3(@RequestParam double dis, @RequestParam double lon, @RequestParam double lat) {
        List<ShanXiTownsDTO> mostIntensityAreaTowns = iShanXiTownsService.getMostIntensityAreaTowns(dis, lon, lat);
        return AjaxResult.success(mostIntensityAreaTowns);
    }

    @PostMapping("/5")
    public AjaxResult test4(@RequestBody EqTriggerDTO trigger) {
        idzEqEventService.trigger(trigger);
        return AjaxResult.success("地震触发成功");
    }

    @PostMapping("/6")
    public AjaxResult test5(@RequestBody EqTriggerDTO trigger) {
        idzEqEventService.trigger(trigger);
        return AjaxResult.success("地震触发成功");
    }

    @PostMapping("/7")
    public AjaxResult test6(@RequestBody EqAssessmentDTO assess) {
        idzProductService.makeEarthquakeReport(assess);
        return AjaxResult.success("地震触发成功");
    }

    @PostMapping("/8")
    public AjaxResult test8(@RequestBody RTriggerDTO trigger) {
        irEventService.trigger(trigger);
        return AjaxResult.success("暴雨触发成功");
    }

    @PostMapping("/9")
    public AjaxResult test9(@RequestBody ProductQuery query) {
        List<DZProductDTO> products = idzProductService.getProducts(query);
        if (products == null || products.size() == 0) {
            return AjaxResult.error(BaseConstants.RESULT_ERROR);
        }
        return AjaxResult.success(products);
    }
    @PostMapping("/10")
    public AjaxResult getInfluence(@RequestBody @Validated EqQuery query) {
        Map<String, String> influence = idzInfluenceService.getInfluence(query);
        if (influence == null) {
            return AjaxResult.error(BaseConstants.RESULT_ERROR);
        }
        return AjaxResult.success(influence);
    }

}
