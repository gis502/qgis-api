package com.ruoyi.system.service.base.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.BaseConstants;
import com.ruoyi.common.exception.ParmaException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.system.handler.GeoDistanceHandler;
import com.ruoyi.system.domain.dto.base.ShanXiTownsDTO;
import com.ruoyi.system.domain.entity.base.SXTowns;
import com.ruoyi.system.mapper.base.ShanXiTownsMapper;
import com.ruoyi.system.service.base.IShanXiTownsService;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTWriter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ShanXiTownsServiceImpl
 * @Description
 * @Author Huang Yx
 * @Date 2026/2/9 17:38
 */
@Slf4j
@Service
public class ShanXiTownsServiceImpl extends ServiceImpl<ShanXiTownsMapper, SXTowns> implements IShanXiTownsService {

    private static final WKTWriter WKT_WRITER = new WKTWriter();

    // 查询距离震中最近的市州
    @Override
    public List<ShanXiTownsDTO> getMostIntensityAreaTowns(double dis, double lon, double lat) {
        // 边界判断
        if (lon < -180 || lon > 180 || lat < -90 || lat > 90) {
            throw new ParmaException(BaseConstants.PARAMS_ERROR);
        }
        // 距离值判断
        if (dis < 0) {
            throw new ParmaException(BaseConstants.PARAMS_ERROR);
        }

        // 获取所有乡镇
        List<SXTowns> towns = this.baseMapper.selectList(null);
        if (towns == null || towns.isEmpty()) {
            throw new ParmaException(BaseConstants.RESULT_ERROR);
        }

        // 获取附近震中 乡镇
        List<ShanXiTownsDTO> townsDto = handle(towns, lon, lat, dis);
        if (townsDto == null) {
            throw new ParmaException(BaseConstants.RESULT_ERROR);
        }
        // 乡镇数据
        return townsDto;
    }

    /**
     * 筛选指定距离下的所有乡镇
     * @param towns
     * @param lon
     * @param lat
     */
    private List<ShanXiTownsDTO> handle(List<SXTowns> towns, double lon, double lat, double dis) {
        // 乡镇距离表
        List<ShanXiTownsDTO> townsdots = new ArrayList<>();

        // 计算距离并筛选最近
        for (SXTowns town : towns) {
            try {
                // 获取Geometry对象，并转换为标准WKT字符串
                Geometry geometry = town.getGeometry();
                if (geometry == null) {
                    log.warn("乡镇名称：{} 无有效Geometry对象，无法解析该空间类型", town.getNAME());
                    continue;
                }
                // 使用WKTWriter将Geometry对象转换为标准WKT字符串
                String pointStringWkt = WKT_WRITER.write(geometry);
                if (StringUtils.isBlank(pointStringWkt)) {
                    log.warn("乡镇名称：{} 转换后无有效WKT数据，无法解析该空间类型", town.getNAME());
                    continue;
                }
                // 计算震中到当前点的距离
                double distance = GeoDistanceHandler.calculateDistanceFromEpicenterToRegionPoint(lon, lat, pointStringWkt);

                // 判断是否在指定范围内
                if (distance < dis) {
                    ShanXiTownsDTO towndto = new ShanXiTownsDTO();
                    BeanUtils.copyProperties(town, towndto);
                    towndto.setDistance(distance);
                    // 指定范围内的所有乡镇
                    townsdots.add(towndto);
                }

            } catch (Exception e) {
                log.warn("处理乡镇名称：{} 失败，异常信息：{}", town.getNAME(), e.getMessage(), e);
                continue;
            }
        }
        log.info("震中{}km范围内共有{}个乡镇!", dis, townsdots.size());

        // 距离最近的市州
        return townsdots;
    }

}
