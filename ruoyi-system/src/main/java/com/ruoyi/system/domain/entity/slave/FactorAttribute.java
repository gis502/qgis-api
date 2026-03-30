package com.ruoyi.system.domain.entity.slave;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author: xiaodemos
 * @date: 2025-07-23 13:02
 * @description: 致灾因子表
 */

@Data
@TableName("xian_factor_attribute")
public class FactorAttribute {


    @TableField("attribute_id")
    private Integer attributeId;    //因子属性ID
    @TableField("attribute_name")
    private String attributeName;    //因子名称（如 “高程”“坡度”)
    @TableField("unit")
    private String unit;    //因子单位
    @TableField("attribute_name_alias")
    private String attributeNameAlias;
    @TableField("create_time")
    private LocalDateTime createTime;//创建时间
    @TableField("update_time")
    private LocalDateTime updateTime;//更新时间
    @TableField("is_deleted")
    private Integer isDeleted;//逻辑删除

}
