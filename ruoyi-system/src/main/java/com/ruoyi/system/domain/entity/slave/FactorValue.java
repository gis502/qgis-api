package com.ruoyi.system.domain.entity.slave;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author: xiaodemos
 * @date: 2025-07-23 13:05
 * @description: 致灾因子值表
 */


@Data
@TableName("xian_factor_value")
public class FactorValue {

    @TableField("value_id")
    private Integer valueId;
    @TableField("hide_id")
    private Integer hideId;
    @TableField("attribute_id")
    private Integer attributeId;
    @TableField("eq_disaster_id")
    private Integer eqDisasterId;
    @TableField("rain_disaster_id")
    private String rainDisasterId;
    @TableField("factor_value")
    private String factorValue;
    @TableField("create_time")
    private LocalDateTime createTime;//创建时间
    @TableField("update_time")
    private LocalDateTime updateTime;//更新时间
    @TableField("is_deleted")
    private Integer isDeleted;//逻辑删除

}
