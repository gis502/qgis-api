package com.ruoyi.system.service.dzxx.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.BaseConstants;
import com.ruoyi.common.exception.ParmaException;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.dto.dzxx.DZXXCenterDTO;
import com.ruoyi.system.domain.entity.dzxx.DZXXCenter;
import com.ruoyi.system.mapper.dzxx.DZXXCenterMapper;
import com.ruoyi.system.service.dzxx.IDZXXCenterService;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @ClassName DZXXCenterServiceImpl
 * @Description 地震事件
 * @Author Huang Yx
 * @Date 2026/2/10 17:13
 */

@Slf4j
@Service
public class DZXXCenterServiceImpl extends ServiceImpl<DZXXCenterMapper, DZXXCenter> implements IDZXXCenterService {

    // 地震触发
    @Override
    public void handle(DZXXCenterDTO dzxxdto) {
        // 抛出异常
        if (dzxxdto == null) {
            throw new ParmaException(BaseConstants.PARAMS_ERROR);
        }

        try {
            DZXXCenter dzxx = new DZXXCenter();
            BeanUtils.copyProperties(dzxxdto, dzxx);
            // 处理空间数据
            GeometryFactory geometryFactory = new GeometryFactory();
            Point point = geometryFactory.createPoint(new Coordinate(
                    dzxxdto.getLongitude(), dzxxdto.getLatitude()
            ));
            dzxx.setGeom(point);
            dzxx.getGeom().setSRID(4490);
            // 存库
            save(dzxx);
            log.info("震中位置信息已存库...");
        } catch (Exception ex) {
            log.error("地震触发：震中位置信息保存失败!", ex.getMessage());
            ex.printStackTrace();
            throw new ServiceException(BaseConstants.EQ_SERVER_ERROR);
        }
    }
}
