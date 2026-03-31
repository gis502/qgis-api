package com.ruoyi.web.controller.system.pub;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.constant.BaseConstants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.dto.pub.DZProductDTO;
import com.ruoyi.system.domain.query.ProductQuery;
import com.ruoyi.system.service.pub.IDZProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.transform.Result;
import java.util.List;

/**
 * @ClassName DZProductController
 * @Description 产出结果
 * @Author Huang Yx
 * @Date 2026/3/23 10:53
 */
@Slf4j
@RestController
@Api(tags = "产出结果控制类")
@RequestMapping("/api/open")
public class DZProductController {

    @Autowired
    private IDZProductService idzProductService;

    @ApiOperation("获取产品接口")
    @PostMapping("/product")
    public AjaxResult getProducts(@RequestBody ProductQuery query) {
        List<DZProductDTO> products = idzProductService.getProducts(query);
        if (products == null || products.size() == 0) {
            return AjaxResult.error(BaseConstants.RESULT_ERROR);
        }
        return AjaxResult.success(products);
    }

}
