package com.ruoyi.system.service.pub;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.dto.pub.DZProductDTO;
import com.ruoyi.system.domain.dto.pub.EqAssessmentDTO;
import com.ruoyi.system.domain.dto.pub.RAssessmentDTO;
import com.ruoyi.system.domain.entity.pub.DZProduct;
import com.ruoyi.system.domain.query.ProductQuery;

import java.util.List;

/**
 * @ClassName DZProductMapper
 * @Description
 * @Author Huang Yx
 * @Date 2026/2/11 9:58
 */

public interface IDZProductService extends IService<DZProduct> {


    // qgis 地震制图服务
    public void makeEarthquakeMaps(EqAssessmentDTO assess);

    // qgis 暴雨制图服务
    public void makeRainstormMaps(RAssessmentDTO assess);

    // 地震灾情报告
    public void makeEarthquakeReport(EqAssessmentDTO assess);

    // 暴雨灾情报告
    public void makeRainstormReport(RAssessmentDTO assess);

    // 获取产品
    public List<DZProductDTO> getProducts(ProductQuery query);


}
