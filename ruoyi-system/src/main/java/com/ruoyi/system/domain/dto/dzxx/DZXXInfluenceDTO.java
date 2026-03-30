package com.ruoyi.system.domain.dto.dzxx;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.locationtech.jts.geom.Geometry;

import java.time.LocalDateTime;

/**
 * @ClassName DZXXInfluenceDTO
 * @Description 地震影响场DTO
 * @Author Huang Yx
 * @Date 2026/1/30 14:31
 */
@Data
public class DZXXInfluenceDTO {

    private Geometry geom;
    private String eqQueueId;
    private String event;
    private String eqName;
    private Integer inty;   // 烈度值
    private String sInty;
    private double area;
    private double longUranium;
    private double shortUranium;
    private double direction;
    private double longitude;
    private double latitude;
}
