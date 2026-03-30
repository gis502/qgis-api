package com.ruoyi.system.service.base.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.BaseConstants;
import com.ruoyi.common.exception.ParmaException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.handler.GeoDistanceHandler;
import com.ruoyi.system.domain.dto.base.ActiveFaultDTO;
import com.ruoyi.system.domain.entity.base.ActiveFault;
import com.ruoyi.system.mapper.base.ActiveFaultMapper;
import com.ruoyi.system.service.base.IActiveFaultService;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTWriter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName ActiveFaultServiceImpl
 * @Description 活动断层业务层
 * @Author Huang Yx
 * @Date 2026/1/30 0:56
 */
@Slf4j
@Service
public class ActiveFaultServiceImpl extends ServiceImpl<ActiveFaultMapper, ActiveFault> implements IActiveFaultService {
    private static final WKTWriter WKT_WRITER = new WKTWriter();

    /**
     * 根据震中点获取距离最近的断层数据
     *
     * @param lon 震中经度
     * @param lat 震中纬度
     * @return 返回距离震中点最近的断层数据
     */
    @Override
    public ActiveFaultDTO getShortlyFault(double lon, double lat) {
        if (lon < -180 || lon > 180 || lat < -90 || lat > 90) {
            throw new ParmaException(BaseConstants.PARAMS_ERROR);
        }
        // 获取所有断层数据
        List<ActiveFault> faults = this.baseMapper.selectList(null);
        if (faults == null || faults.isEmpty()) {
            throw new ParmaException(BaseConstants.RESULT_ERROR);
        }
        // 查找最近的一条
        ActiveFaultDTO fault = handle(faults, lon, lat);

        if (fault == null) {
            throw new ParmaException(BaseConstants.RESULT_ERROR);
        }
        // 避难断层走向为空
        if (fault.getStrike() == null) {
            fault.setStrike(0);
        }

        // 返回
        return fault;
    }

    /**
     * 处理断层数据
     *
     * @param lon 震中经度
     * @param lat 震中纬度
     * @return 返回实DTO断层实体
     */
    private ActiveFaultDTO handle(List<ActiveFault> faults, double lon, double lat) {

        // 记录最近断层、最小距离
        ActiveFault nearest = null;
        // 所有断层中的最小距离
        double minTotalDistance = Double.MAX_VALUE;
        // 计算距离并筛选最近
        for (ActiveFault fault : faults) {
            try {
                // 获取Geometry对象，并转换为标准WKT字符串
                Geometry geometry = fault.getGeometry();
                if (geometry == null) {
                    log.warn("断层名称：{} 无有效Geometry对象，跳过", fault.getName());
                    continue;
                }

                // 使用WKTWriter将Geometry对象转换为标准WKT字符串
                String lineStringWkt = WKT_WRITER.write(geometry);

                if (StringUtils.isBlank(lineStringWkt)) {
                    log.warn("断层名称：{} 转换后无有效WKT数据，跳过", fault.getName());
                    continue;
                }

                // 解析LINESTRING为端点坐标数组
                double[][] coordinates = GeoDistanceHandler.parseLineStringCoordinates(lineStringWkt);
                if (coordinates.length < 2) {
                    log.warn("断层名称：{} 的LINESTRING无有效线段（端点数量<2），跳过", fault.getName());
                    continue;
                }

                // 计算该断层所有子线段的最短距离
                double minFaultDistance = Double.MAX_VALUE; // 单个断层内的最小子线段距离
                for (int i = 0; i < coordinates.length - 1; i++) {
                    // 提取子线段的两个端点（A和B）
                    double lonA = coordinates[i][0];
                    double latA = coordinates[i][1];
                    double lonB = coordinates[i + 1][0];
                    double latB = coordinates[i + 1][1];

                    // 计算震中到该子线段的最短距离
                    double segmentDistance = GeoDistanceHandler.calculateDistanceFromPointToLineString(
                            lon, lat, lonA, latA, lonB, latB
                    );

                    // 更新单个断层内的最小距离
                    if (segmentDistance < minFaultDistance) {
                        minFaultDistance = segmentDistance;
                    }
                }

                // 更新所有断层中的最近断层
                if (minFaultDistance < minTotalDistance) {
                    minTotalDistance = minFaultDistance;
                    nearest = fault;
                }
            } catch (Exception e) {
                log.warn("处理断层名称：{} 失败，异常信息：{}", fault.getName(), e.getMessage(), e);
                continue;
            }
        }

        // 转换
        ActiveFaultDTO activeFaultDTO = new ActiveFaultDTO();
        activeFaultDTO.setName(nearest.getName());
        activeFaultDTO.setDirection(nearest.getDirection());
        activeFaultDTO.setClination(nearest.getClination());
        activeFaultDTO.setShapeLen(nearest.getShape_Leng());
        activeFaultDTO.setGeometry(nearest.getGeometry());
        activeFaultDTO.setDistance(minTotalDistance);

        return activeFaultDTO;
    }


}
