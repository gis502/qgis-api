package com.ruoyi.system.domain.dto.pub;

import lombok.Data;

/**
 * @ClassName SecondaryDisasterDTO
 * @Description 次生灾害表数据
 * @Author Huang Yx
 * @Date 2026/3/18 13:19
 */
@Data
public class RSecondaryDisasterDTO {

    private String position;                        // 位置
    private String probability;                     // 概率
    private String grade;

}
