package com.ruoyi.system.domain.dto.pub;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName DZProductDTO
 * @Description
 * @Author Huang Yx
 * @Date 2026/3/12 16:40
 */
@Data
public class DZProductDTO {


    private String eqQueueId;
    private Integer templetId;  // 模板编码id
    private String code;    // A3 A4
    private LocalDateTime proTime;
    private String fileType;    // 图片 文档
    private String fileName;
    private String filePath;    // 模板路径 查询时设置为空
    private String fileExtension;
    private Double fileSize;
    private String proType;     // 暴雨/地震 灾害专题图 暴雨/地震 灾害文档
    private String sourceFile;  // 存储位置


}
