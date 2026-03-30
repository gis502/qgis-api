package com.ruoyi.system.domain.dto.dzxx;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.locationtech.jts.geom.Geometry;

import java.time.LocalDateTime;

/**
 * @ClassName DZXXDistanceDTO
 * @Description
 * @Author Huang Yx
 * @Date 2026/2/10 14:58
 */
@Data
public class DZXXDistanceDTO {

    private Geometry geom;
    private String eqQueueId;
    private Integer distanceId; // 市1、县2、镇3
    private String pId;     // 行政id
    private String pname;   // 省市县镇名称
    private String bgmc;    // 行政区划等级
    private Double distance;    // 距离
    private String remark;

}
