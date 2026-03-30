package com.ruoyi.system.domain.dto.pub;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @ClassName DZInfluenceDTO
 * @Description 影响场文件
 * @Author Huang Yx
 * @Date 2026/2/11 18:27
 */
@Data
public class DZInfluenceDTO {

    private String eqQueueId;
    private String event;
    private String name;
    private String file;
    private String path;
    private String content;
    private String intensityColumn;
    private String source;
    private Integer isEdit;
    private Integer isPg;

}
