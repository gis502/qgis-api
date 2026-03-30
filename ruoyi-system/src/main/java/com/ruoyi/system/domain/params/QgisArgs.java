package com.ruoyi.system.domain.params;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName PyQgisArgs
 * @Description Qgis出图参数
 * @Author Huang Yx
 * @Date 2026/2/12 17:54
 */
@Data
public class QgisArgs implements Serializable {

    private Integer Id;         // 记录进度
    private double centerX;
    private double centerY;
    private String info;      // 三要素
    private String event;
    private String mapLayout;   // 画幅（A3、A4）
    private String mapTime;     // 制图时间
    private String mapTitle;    // 专题图标题
    private String mapUnit;     // 制图单位
    private String name;        // 专题图文件名称
    private String outFile;     // 专题图输出路径
    private String path;        // qgis模板路径
    private String queueId;   // 批次编码
    private String zoomRule;    // 缩放规则
    private String zoomValue;   // 缩放值
    private String disaster;   // 灾害类型

}
