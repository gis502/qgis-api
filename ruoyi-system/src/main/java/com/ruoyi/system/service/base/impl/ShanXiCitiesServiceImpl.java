package com.ruoyi.system.service.base.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.BaseConstants;
import com.ruoyi.common.exception.ParmaException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.system.handler.GeoDistanceHandler;
import com.ruoyi.system.domain.dto.base.ShanXiCitiesDTO;
import com.ruoyi.system.domain.entity.base.SXCities;
import com.ruoyi.system.mapper.base.ShanXiCitiesMapper;
import com.ruoyi.system.service.base.IShanXiCitiesService;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTWriter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ShanXiCitiesServiceImpl
 * @Description
 * @Author Huang Yx
 * @Date 2026/2/9 17:38
 */
@Slf4j
@Service
public class ShanXiCitiesServiceImpl extends ServiceImpl<ShanXiCitiesMapper, SXCities> implements IShanXiCitiesService {

    private static final WKTWriter WKT_WRITER = new WKTWriter();

    // 查询距离震中最近的市州
    @Override
    public List<ShanXiCitiesDTO> getMostIntensityAreaCities(double lon, double lat) {
        // 边界判断
        if (lon < -180 || lon > 180 || lat < -90 || lat > 90) {
            throw new ParmaException(BaseConstants.PARAMS_ERROR);
        }
        // 获取所有市州
        List<SXCities> cities = this.baseMapper.selectList(null);
        if (cities == null || cities.isEmpty()) {
            throw new ParmaException(BaseConstants.RESULT_ERROR);
        }

        // 获取附近震中 最近的市州（本地和外地）
        List<ShanXiCitiesDTO> citiesDto = handle(cities, lon, lat);
        if (citiesDto == null) {
            throw new ParmaException(BaseConstants.RESULT_ERROR);
        }
        // 城市数据
        return citiesDto;
    }

    /**
     * 处理最近距离震中最近
     *
     * @param cities
     * @param lon
     * @param lat
     */
    private List<ShanXiCitiesDTO> handle(List<SXCities> cities, double lon, double lat) {
        // 记录距离最近的市州
        List<ShanXiCitiesDTO> citiesdots = new ArrayList<>();
        // 记录最近市州、最小距离
        SXCities nearest = null;
        // 所有市州中的最小距离
        double minTotalDistance = Double.MAX_VALUE;
        // 保留两位小数
        DecimalFormat df = new DecimalFormat("#.00");
        df.setRoundingMode(java.math.RoundingMode.HALF_UP);

        // 计算距离并筛选最近
        for (SXCities city : cities) {
            try {
                // 获取Geometry对象，并转换为标准WKT字符串
                Geometry geometry = city.getGeometry();
                if (geometry == null) {
                    log.warn("市州名称：{} 无有效Geometry对象，无法解析该空间类型", city.getNAME());
                    continue;
                }
                // 使用WKTWriter将Geometry对象转换为标准WKT字符串
                String pointStringWkt = WKT_WRITER.write(geometry);
                if (StringUtils.isBlank(pointStringWkt)) {
                    log.warn("市州名称：{} 转换后无有效WKT数据，无法解析该空间类型", city.getNAME());
                    continue;
                }
                // 计算震中到当前点的距离
                double distance = GeoDistanceHandler.calculateDistanceFromEpicenterToRegionPoint(lon, lat, pointStringWkt);
                // 计算该断层所有市州段的最短距离
                double minCityDistance = Double.MAX_VALUE;
                if (distance < minCityDistance) {
                    minCityDistance = distance;
                }

                // 更新市州距离
                if (minCityDistance < minTotalDistance) {
                    minTotalDistance = minCityDistance;
                    nearest = city;
                }

            } catch (Exception e) {
                log.warn("处理市州名称：{} 失败，异常信息：{}", city.getNAME(), e.getMessage(), e);
                continue;
            }
        }
        log.info("震中距{}市中{}km!", nearest.getNAME(), minTotalDistance);
        ShanXiCitiesDTO citiesdto = new ShanXiCitiesDTO();
        // 是否西安，不是的话需要返回临市和本市
        if (!nearest.getNAME().equals("西安市")) {

            // 计算震中到西安的距离
            double distance = GeoDistanceHandler.calculateDistanceFromEpicenterToRegionPoint(lon, lat, "POINT (108.935124 34.343595)");
            // citiesdto.setGeometry();
            citiesdto.setDistance(Double.parseDouble(df.format(distance)));
            citiesdto.setX(108.935124);
            citiesdto.setY(34.343595);
            citiesdto.setNAME("西安市");

            citiesdots.add(citiesdto);
            return citiesdots;
        }
        // 西安属地
        BeanUtils.copyProperties(nearest,citiesdto);
        // 保留两位小数
        citiesdto.setDistance(Double.parseDouble(df.format(minTotalDistance)));

        citiesdots.add(citiesdto);
        // 距离最近的市州
        return citiesdots;
    }

}
