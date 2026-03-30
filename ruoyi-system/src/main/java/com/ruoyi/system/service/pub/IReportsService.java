package com.ruoyi.system.service.pub;

import com.ruoyi.system.domain.dto.pub.EqReportDTO;
import com.ruoyi.system.domain.dto.pub.RReportDTO;

/**
 * @ClassName IReportsService
 * @Description 报告服务
 * @Author Huang Yx
 * @Date 2026/3/12 15:22
 */
public interface IReportsService {


    // 地震灾害文档
    public void earthquakeDisasterDocument(EqReportDTO args, String queueId);

    // 暴雨灾害文档
    public void rainstormDisasterDocument(RReportDTO args, String queueId);

}
