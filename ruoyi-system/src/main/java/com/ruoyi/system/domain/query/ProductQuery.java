package com.ruoyi.system.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName ProductQuery
 * @Description 产品参数
 * @Author Huang Yx
 * @Date 2026/3/23 11:16
 */
@Data
@ApiModel("获取产品实体")
public class ProductQuery {

    @ApiModelProperty("产品Id,必填项")
    private String queueId;
    @ApiModelProperty("专题图尺寸,选填项")
    private String code;    // A3 A4
    @ApiModelProperty("产品类型（图片/文档）,选填")
    private String fileType;    // 图片 文档
    @ApiModelProperty("产品名称（模糊查询）,选填")
    private String fileName;
    @ApiModelProperty("灾害类型（暴雨/地震）,选填")
    private String proType;     // 暴雨/地震 灾害专题图 暴雨/地震 灾害文档
}
