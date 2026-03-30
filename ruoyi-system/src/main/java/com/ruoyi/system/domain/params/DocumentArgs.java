package com.ruoyi.system.domain.params;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName DocArgs
 * @Description 文档参数
 * @Author Huang Yx
 * @Date 2026/3/20 11:47
 */

@Data
public class DocumentArgs implements Serializable {

    private String name;        // 专题图文件名称
    private String outFile;     // 专题图输出路径
    private String path;        // qgis模板路径
    private String queueId;   // 批次编码
    private String disaster;   // 灾害类型



}
