package com.ruoyi.system.domain.dto.pub;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author: xiaodemos
 * @date: 2025-04-05 11:20
 * @description: 地震触发DTO类
 */
@ApiModel("地震事件触发参数")
@Data
public class EqTriggerDTO {
    @ApiModelProperty("地震名称")
    private String eqName;
    @ApiModelProperty("震发地点（四川省雅安市芦山县快乐村）")
    private String eqAddr;
    @ApiModelProperty("震发时间")
    private LocalDateTime eqTime;
    @ApiModelProperty("东经")
    private Double longitude;
    @ApiModelProperty("北纬")
    private Double latitude;
    @ApiModelProperty("震源深度")
    private Double eqDepth;
    @ApiModelProperty("震级")
    private Double eqMagnitude;
    @ApiModelProperty("地震全称（四川雅安芦山发生6.2级地震）")
    private String eqFullName;
    @ApiModelProperty("地震类型（T:测试，Z:正式，Y:演练）")
    private String eqType;
}
