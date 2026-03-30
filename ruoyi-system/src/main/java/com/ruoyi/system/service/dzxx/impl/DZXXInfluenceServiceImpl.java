package com.ruoyi.system.service.dzxx.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.BaseConstants;
import com.ruoyi.common.exception.ParmaException;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.handler.EarthquakeHandler;
import com.ruoyi.system.handler.EllipseToWktHandler;
import com.ruoyi.system.domain.dto.base.ActiveFaultDTO;
import com.ruoyi.system.domain.dto.dzxx.DZXXInfluenceDTO;
import com.ruoyi.system.domain.dto.pub.EqAssessmentDTO;
import com.ruoyi.system.domain.entity.dzxx.DZXXInfluence;
import com.ruoyi.system.domain.query.EqQuery;
import com.ruoyi.system.domain.query.IntensityQuery;
import com.ruoyi.system.mapper.dzxx.DZXXInfluenceMapper;
import com.ruoyi.system.service.base.IActiveFaultService;
import com.ruoyi.system.service.dzxx.IDZXXInfluenceService;
import com.ruoyi.system.service.pub.IDZInfluenceService;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Polygon;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName DZXXInfluenceServiceImpl
 * @Description 地震影响场业务
 * @Author Huang Yx
 * @Date 2026/1/30 14:36
 */
@Slf4j
@Service
public class DZXXInfluenceServiceImpl extends ServiceImpl<DZXXInfluenceMapper, DZXXInfluence> implements IDZXXInfluenceService {

    @Autowired
    private IActiveFaultService faultService;
    @Autowired
    private EllipseToWktHandler ellipseToWktHandler;
    @Autowired
    private IDZInfluenceService idzInfluenceService;

    // 处理地震影响场数据
    @Override
    public void handle(EqAssessmentDTO assess) {
        // 异常处理
        if (assess == null) {
            throw new ParmaException(BaseConstants.PARAMS_ERROR);
        }
        // 处理地震影响场数据
        try {
            // 获取烈度值
            int[] intensities = gainIntensityLevels(assess.getEqMagnitude());
            // 根据烈度值和震级生成影响场
            for (int intensity : intensities) {
                // 根据烈度和震级计算长轴
                double longUranium = EarthquakeHandler.calculateRa(assess.getEqMagnitude(), intensity);
                // 根据烈度和震级计算短轴
                double shortUranium = EarthquakeHandler.calculateRb(assess.getEqMagnitude(), intensity);
                // 根据长短轴计算面积
                double area = EarthquakeHandler.calculateArea(longUranium, shortUranium);

                // 获取断层走向 角度
                ActiveFaultDTO shortlyFault = faultService.getShortlyFault(assess.getLongitude(), assess.getLatitude());
                // 根据烈度长短轴、走向、面积计算生成影响场
                IntensityQuery query = compute(longUranium, shortUranium, shortlyFault, assess);
                // 影响场范围
                Polygon polygon = ellipseToWktHandler.ellipseToPolygonWkt(query);

                // 转换存库
                convert(longUranium, shortUranium, area, intensity, polygon, shortlyFault, assess);
                log.info("地震影响场已生成!");
            }

        } catch (Exception ex) {
            log.error("处理影响场数据时出错：" + ex.getMessage());
            ex.printStackTrace();
            throw new ServiceException(BaseConstants.EQ_SERVER_ERROR);
        }

        // 获取地震影响场数据
        List<DZXXInfluenceDTO> influences = findInfluenceById(new EqQuery(assess.getEvent(), assess.getEqQueueId()));
        // 输出geojson文件
        idzInfluenceService.handle(influences);

    }

    // 查询地震影响场
    @Override
    public List<DZXXInfluenceDTO> findInfluenceById(EqQuery query) {
        // 查询条件
        LambdaQueryWrapper<DZXXInfluence> lambdaQuery = Wrappers.lambdaQuery(DZXXInfluence.class);
        lambdaQuery.eq(DZXXInfluence::getEvent, query.getEvent());
        lambdaQuery.eq(DZXXInfluence::getEqQueueId, query.getEqQueueId());

        // 该场地震影响场
        List<DZXXInfluence> influences = this.baseMapper.selectList(lambdaQuery);

        if (influences == null || influences.size() == 0) {
            log.error("查询地震影响场结果为空！");
            throw new ParmaException(BaseConstants.RESULT_ERROR);
        }
        // 处理
        List<DZXXInfluenceDTO> dzxxs = new ArrayList<>();
        for (DZXXInfluence influence : influences) {
            DZXXInfluenceDTO dzxx = new DZXXInfluenceDTO();
            BeanUtils.copyProperties(influence, dzxx);
            dzxxs.add(dzxx);
        }

        return dzxxs;
    }


    // 获取最大烈度影响场
    @Override
    public DZXXInfluenceDTO findInfluenceMaxIntyById(EqQuery query) {

        LambdaQueryWrapper<DZXXInfluence> lambdaQuery = Wrappers.lambdaQuery(DZXXInfluence.class);
        lambdaQuery
                .eq(DZXXInfluence::getEvent, query.getEvent())
                .eq(DZXXInfluence::getEqQueueId, query.getEqQueueId())
                .orderByDesc(DZXXInfluence::getInty)
                .last("limit 1");   // 最大烈度

        // 获取单条数据
        DZXXInfluence influence = this.baseMapper.selectOne(lambdaQuery);

        if (influence == null) {
            log.error("查询地震影响场结果为空！");
            throw new ParmaException(BaseConstants.RESULT_ERROR);
        }
        DZXXInfluenceDTO dzxx = new DZXXInfluenceDTO();
        BeanUtils.copyProperties(influence, dzxx);

        return dzxx;
    }


    // 根据震级获取对应的烈度值数组
    private int[] gainIntensityLevels(double magnitude) {
        // 对震级进行四舍五入处理，取整数级别
        int magLevel = (int) Math.round(magnitude);
        // 根据震级范围返回对应的烈度值数组
        if (magLevel >= 5.5 && magLevel < 7.0) {
            return new int[]{6, 7, 8};
        } else if (magLevel >= 7.0 && magLevel < 8.0) {
            return new int[]{6, 7, 8, 9};
        } else if (magLevel >= 8.0 && magLevel < 9.0) {
            return new int[]{6, 7, 8, 9, 10};
        } else if (magLevel >= 9.0) {
            // 9级及以上都返回9-12级烈度
            return new int[]{6, 7, 8, 9, 10, 11, 12};
        } else {
            // 对于6级以下的震级，默认返回低烈度值
            return new int[]{6};
        }
    }

    // 根据烈度长短轴、走向、面积计算生成影响场
    private IntensityQuery compute(double longUranium, double shortUranium,
                                   ActiveFaultDTO shortlyFault, EqAssessmentDTO assess) {

        IntensityQuery query = new IntensityQuery();
        query.setCenterLon(assess.getLongitude());
        query.setCenterLat(assess.getLatitude());
        query.setSemiMajor(longUranium);
        query.setSemiMinor(shortUranium);
        query.setRotation(shortlyFault.getStrike());
        query.setNumVertices(BaseConstants.NUM_VERTICES);

        return query;
    }

    // 转换存库
    private void convert(double longUranium, double shortUranium, double area, int intensity,
                         Polygon polygon, ActiveFaultDTO shortlyFault, EqAssessmentDTO assess) {
        DZXXInfluenceDTO dzxxDto = new DZXXInfluenceDTO();
        dzxxDto.setArea(area);
        dzxxDto.setGeom(polygon);
        dzxxDto.setDirection(Double.valueOf(shortlyFault.getDirection()));
        dzxxDto.setLongitude(assess.getLongitude());
        dzxxDto.setLatitude(assess.getLatitude());
        dzxxDto.setEqName(assess.getEqName());
        dzxxDto.setEqQueueId(assess.getEqQueueId());
        dzxxDto.setEvent(assess.getEvent());
        dzxxDto.setInty(intensity);
        dzxxDto.setSInty(BaseConstants.SEISMIC_INTENSITY_MAPPING.get(intensity));
        dzxxDto.setLongUranium(longUranium);
        dzxxDto.setShortUranium(shortUranium);

        // 存库
        DZXXInfluence dzxxInfluence = new DZXXInfluence();
        BeanUtils.copyProperties(dzxxDto, dzxxInfluence);
        dzxxInfluence.getGeom().setSRID(4490);
        save(dzxxInfluence);
        log.info("地震影响场已保存!");
    }

}
