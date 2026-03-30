package com.ruoyi.system.service.dzxx.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.BaseConstants;
import com.ruoyi.common.exception.ParmaException;
import com.ruoyi.system.domain.dto.base.ShanXiCitiesDTO;
import com.ruoyi.system.domain.dto.base.ShanXiCountyDTO;
import com.ruoyi.system.domain.dto.base.ShanXiTownsDTO;
import com.ruoyi.system.domain.dto.dzxx.DZXXDistanceDTO;
import com.ruoyi.system.domain.entity.dzxx.DZXXDistance;
import com.ruoyi.system.mapper.dzxx.DZXXDistanceMapper;
import com.ruoyi.system.service.base.IShanXiCitiesService;
import com.ruoyi.system.service.base.IShanXiCountyService;
import com.ruoyi.system.service.base.IShanXiTownsService;
import com.ruoyi.system.service.dzxx.IDZXXDistanceService;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName DZXXDistanceServiceImpl
 * @Description 震中到省市县乡距离
 * @Author Huang Yx
 * @Date 2026/2/10 12:08
 */
@Slf4j
@Service
public class DZXXDistanceServiceImpl extends ServiceImpl<DZXXDistanceMapper, DZXXDistance> implements IDZXXDistanceService {

    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326);

    @Autowired
    private IShanXiCitiesService iShanXiCitiesService;
    @Autowired
    private IShanXiCountyService iShanXiCountyService;
    @Autowired
    private IShanXiTownsService iShanXiTownsService;

    // 处理所有乡镇表
    @Override
    public void handle(double lon, double lat, String eqQueueId) {

        // 查询距震中最近市州
        List<ShanXiCitiesDTO> cities = iShanXiCitiesService.getMostIntensityAreaCities(lon, lat);
        // 查询距震中50km县区
        List<ShanXiCountyDTO> counties50km = iShanXiCountyService.getMostIntensityAreaCounty(50, lon, lat);
        // 查询距震中20km乡镇
        List<ShanXiTownsDTO> towns20km = iShanXiTownsService.getMostIntensityAreaTowns(20, lon, lat);
        // 所有行政区划数
        List<DZXXDistanceDTO> combined = combine(cities, counties50km, towns20km, eqQueueId, lon, lat);
        // 存库
        asyncSave(combined);
    }

    // 将行政区划组合
    private List<DZXXDistanceDTO> combine(List<ShanXiCitiesDTO> cities,
                                          List<ShanXiCountyDTO> counties20km,
                                          List<ShanXiTownsDTO> towns10km,
                                          String eqQueueId, double lon, double lat) {
        // 震中到行政区划距离
        List<DZXXDistanceDTO> distancedtos = new ArrayList<>();

        // 处理市州
        for (ShanXiCitiesDTO city : cities) {
            DZXXDistanceDTO dzxx = new DZXXDistanceDTO();
            // 处理震中到市州的线段
            dzxx.setGeom(p2pLines(lon, lat, city.getX(), city.getY()));

            dzxx.setEqQueueId(eqQueueId);
            dzxx.setDistanceId(1);
            dzxx.setPId("");
            dzxx.setPname(city.getNAME());
            dzxx.setBgmc("市州");
            dzxx.setDistance(city.getDistance());

            distancedtos.add(dzxx);
        }

        // 处理县区
        for (ShanXiCountyDTO county : counties20km) {
            DZXXDistanceDTO dzxx = new DZXXDistanceDTO();
            // 处理震中到县区的线段
            dzxx.setGeom(p2pLines(lon, lat, county.getX(), county.getY()));

            dzxx.setEqQueueId(eqQueueId);
            dzxx.setDistanceId(2);
            dzxx.setPId("");
            dzxx.setPname(county.getNAME());
            dzxx.setBgmc("县区");
            dzxx.setDistance(county.getDistance());

            distancedtos.add(dzxx);
        }

        // 处理乡镇
        for (ShanXiTownsDTO town : towns10km) {
            DZXXDistanceDTO dzxx = new DZXXDistanceDTO();
            // 处理震中到乡镇的线段
            dzxx.setGeom(p2pLines(lon, lat, town.getX(), town.getY()));

            dzxx.setEqQueueId(eqQueueId);
            dzxx.setDistanceId(3);
            dzxx.setPId("");
            dzxx.setPname(town.getNAME());
            dzxx.setBgmc("乡镇");
            dzxx.setDistance(town.getDistance());

            distancedtos.add(dzxx);
        }

        // 各级别行政区划距离
        return distancedtos;
    }

    // 将震中点与区划点进行组合 形成一条牵引线
    private Geometry p2pLines(double x1, double y1, double x2, double y2) {
        // 边界值判断
        if (x1 < -180 || x1 > 180 || x2 < -180 || x2 > 180) {
            throw new ParmaException(BaseConstants.POM_ERROR);
        }

        if (y1 < -90 || y1 > 90 || y2 < -90 || y2 > 90) {
            throw new ParmaException(BaseConstants.POM_ERROR);
        }

        // 创建坐标点
        Coordinate startPoint = new Coordinate(x1, y1);
        Coordinate endPoint = new Coordinate(x2, y2);
        Coordinate[] coordinates = new Coordinate[]{startPoint, endPoint};
        // 牵引线
        LineString lineString = GEOMETRY_FACTORY.createLineString(coordinates);

        return lineString;
    }

    @Async("taskExecutor")
    protected void asyncSave(List<DZXXDistanceDTO> data) {
        // 批量存储
        List<DZXXDistance> dzxxs = new ArrayList<>();
        //处理数据
        for (DZXXDistanceDTO datum : data) {
            DZXXDistance dzxx = new DZXXDistance();
            BeanUtils.copyProperties(datum, dzxx);
            dzxx.getGeom().setSRID(4490);
            dzxxs.add(dzxx);
        }
        // 批量保存
        saveBatch(dzxxs);
        log.info("震中到行政区划距离表已计算保存!");
    }

}
