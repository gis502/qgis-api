package com.ruoyi.system.domain.query;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName ProductQuery
 * @Description 产品参数
 * @Author Huang Yx
 * @Date 2026/3/23 11:16
 */
@Data
public class ProductQuery {

    private String queueId;
    private String code;    // A3 A4
    private String fileType;    // 图片 文档
    private String fileName;
    private String proType;     // 暴雨/地震 灾害专题图 暴雨/地震 灾害文档
}
