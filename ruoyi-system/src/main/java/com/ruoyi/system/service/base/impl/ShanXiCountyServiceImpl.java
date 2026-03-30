package com.ruoyi.system.service.base.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.BaseConstants;
import com.ruoyi.common.exception.ParmaException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.system.handler.GeoDistanceHandler;
import com.ruoyi.system.domain.dto.base.ShanXiCountyDTO;
import com.ruoyi.system.domain.entity.base.SXCounty;
import com.ruoyi.system.mapper.base.ShanXiCountyMapper;
import com.ruoyi.system.service.base.IShanXiCountyService;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTWriter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
public class ShanXiCountyServiceImpl extends ServiceImpl<ShanXiCountyMapper, SXCounty> implements IShanXiCountyService {

    private static final WKTWriter WKT_WRITER = new WKTWriter();

    // 查询距离震中最近的市州
    @Override
    public List<ShanXiCountyDTO> getMostIntensityAreaCounty(double dis, double lon, double lat) {
        // 边界判断
        if (lon < -180 || lon > 180 || lat < -90 || lat > 90) {
            throw new ParmaException(BaseConstants.PARAMS_ERROR);
        }
        // 距离值判断
        if (dis < 0) {
            throw new ParmaException(BaseConstants.PARAMS_ERROR);
        }

        // 获取所有区县
        List<SXCounty> counties = this.baseMapper.selectList(null);
        if (counties == null || counties.isEmpty()) {
            throw new ParmaException(BaseConstants.RESULT_ERROR);
        }

        // 获取附近震中 区县
        List<ShanXiCountyDTO> countiesDto = handle(counties, lon, lat, dis);
        if (countiesDto == null) {
            throw new ParmaException(BaseConstants.RESULT_ERROR);
        }
        // 区县数据
        return countiesDto;
    }

    /**
     * 筛选指定距离下的所有区县
     * @param counties
     * @param lon
     * @param lat
     */
    private List<ShanXiCountyDTO> handle(List<SXCounty> counties, double lon, double lat, double dis) {
        // 区县距离表
        List<ShanXiCountyDTO> countiesdots = new ArrayList<>();

        // 计算距离并筛选最近
        for (SXCounty county : counties) {
            try {
                // 获取Geometry对象，并转换为标准WKT字符串
                Geometry geometry = county.getGeometry();
                if (geometry == null) {
                    log.warn("市州名称：{} 无有效Geometry对象，无法解析该空间类型", county.getNAME());
                    continue;
                }
                // 使用WKTWriter将Geometry对象转换为标准WKT字符串
                String pointStringWkt = WKT_WRITER.write(geometry);
                if (StringUtils.isBlank(pointStringWkt)) {
                    log.warn("市州名称：{} 转换后无有效WKT数据，无法解析该空间类型", county.getNAME());
                    continue;
                }
                // 计算震中到当前点的距离
                double distance = GeoDistanceHandler.calculateDistanceFromEpicenterToRegionPoint(lon, lat, pointStringWkt);

                // 判断是否在指定范围内
                if (distance < dis) {
                    ShanXiCountyDTO countydto = new ShanXiCountyDTO();
                    BeanUtils.copyProperties(county, countydto);
                    countydto.setDistance(distance);
                    // 指定范围内的所有县区
                    countiesdots.add(countydto);
                }

            } catch (Exception e) {
                log.warn("处理区县名称：{} 失败，异常信息：{}", county.getNAME(), e.getMessage(), e);
                continue;
            }
        }
        log.info("震中{}km范围内共有{}个县区!", dis, countiesdots.size());

        // 距离最近的市州
        return countiesdots;
    }

}
