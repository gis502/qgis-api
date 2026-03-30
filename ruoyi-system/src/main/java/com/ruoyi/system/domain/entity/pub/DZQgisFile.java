package com.ruoyi.system.domain.entity.pub;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @ClassName DZQgisFile
 * @Description 地震专题图模板
 * @Author Huang Yx
 * @Date 2026/2/11 9:49
 */
@Data
@TableName("t_file")
public class DZQgisFile {

    @TableField("id")
    private String Id;
    @TableField("file_name")
    private String fileName;
    @TableField("original_name")
    private String originalName;
    @TableField("path")
    private String path;
    @TableField("suffix")
    private String suffix;
    @TableField("file_type")
    private String fileType;
    @TableField("file_url")
    private String fileUrl;
    @TableField("size")
    private Integer size;
    @TableField("type")
    private String type;
    @TableLogic
    @TableField("del_flag")
    private Integer delFlag = 0;
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
