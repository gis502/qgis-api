package com.ruoyi.system.domain.entity.pub;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName DZProduct
 * @Description 地震产品产出
 * @Author Huang Yx
 * @Date 2026/2/11 9:36
 */
@Data
@TableName("public.dz_product")
public class DZProduct {

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long Id;
    @TableField("eqqueue_id")
    private String eqQueueId;
    @TableField("templet_id")
    private Integer templetId;  // 模板编码id
    @TableField("code")
    private String code;
    @TableField("pro_time")
    private LocalDateTime proTime;
    @TableField("file_type")
    private String fileType;
    @TableField("file_name")
    private String fileName;
    @TableField("file_path")
    private String filePath;
    @TableField("file_extension")
    private String fileExtension;
    @TableField("file_size")
    private Double fileSize;
    @TableField("pro_type")
    private String proType;
    @TableField("source_file")
    private String sourceFile;
    @TableField(value = "create_by",fill = FieldFill.INSERT_UPDATE)
    private String createBy;
    @TableField(value = "create_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime createTime;
    @TableField(value = "create_dept",fill = FieldFill.INSERT_UPDATE)
    private Integer createDept;
    @TableField(value = "update_by",fill = FieldFill.INSERT_UPDATE)
    private String updateBy;
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableField("remark")
    private String remark;

}
