package com.ruoyi.system.service.slave;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.system.domain.entity.slave.RSEvent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @ClassName IREventService
 * @Description
 * @Author Huang Yx
 * @Date 2026/3/18 16:00
 */
@DataSource(value = DataSourceType.SLAVE)
public interface IRSEventService extends IService<RSEvent> {


    // 获取最新暴雨事件
    public RSEvent getLatestRainDisaster();

    // 暴雨时间
    public LocalDateTime getRainTime(Long disasterId);

    // 暴雨区域位置
    public String getRainAreaPosition(Long disasterId);

    // 暴雨区域数量
    public String getRainAreaQuantity(Long disasterId);

}
