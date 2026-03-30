package com.ruoyi.system.domain.query;

import com.ruoyi.common.annotation.DataSource;
import lombok.Data;

/**
 * @ClassName IntensityQuery
 * @Description 烈度参数
 * @Author Huang Yx
 * @Date 2026/1/29 20:02
 */
@Data
public class IntensityQuery {

    private double centerLon;   // 中心点经度
    private double centerLat;   // 中心点纬度
    private double semiMajor;   // 长半轴（米）
    private double semiMinor;   // 短半轴（米）
    private double rotation;    // 旋转角度（度，顺时针为正，以北为0度）
    private int numVertices;    // 生成的顶点数，越多越接近椭圆

}