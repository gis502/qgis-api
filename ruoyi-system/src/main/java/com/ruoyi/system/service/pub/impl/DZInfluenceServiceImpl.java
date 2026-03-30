package com.ruoyi.system.service.pub.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.BaseConstants;
import com.ruoyi.common.exception.ParmaException;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.dto.dzxx.DZXXInfluenceDTO;
import com.ruoyi.system.domain.dto.pub.DZInfluenceDTO;
import com.ruoyi.system.domain.dto.pub.IntyGeoJsonDTO;
import com.ruoyi.system.domain.entity.dzxx.DZXXInfluence;
import com.ruoyi.system.domain.entity.pub.DZInfluence;
import com.ruoyi.system.domain.query.EqQuery;
import com.ruoyi.system.handler.GeoFilesHandler;
import com.ruoyi.system.mapper.pub.DZInfluenceMapper;
import com.ruoyi.system.service.pub.IDZInfluenceService;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Polygon;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName DZInfluenceServiceImpl
 * @Description 地震影响场实现
 * @Author Huang Yx
 * @Date 2026/2/11 15:04
 */
@Slf4j
@Service
public class DZInfluenceServiceImpl extends ServiceImpl<DZInfluenceMapper, DZInfluence> implements IDZInfluenceService {

    @Autowired
    private GeoFilesHandler handler;


    // 将影响场以文件形式保存
    @Async("taskExecutor")
    @Override
    public void handle(List<DZXXInfluenceDTO> dzxxs) {
        // 异常
        if (dzxxs == null || dzxxs.isEmpty()) {
            throw new ParmaException(BaseConstants.PARAMS_ERROR);
        }
        try {
            // 影响场名称
            String fineName = "";
            IntyGeoJsonDTO features = new IntyGeoJsonDTO();
            DZInfluenceDTO influence = new DZInfluenceDTO();
            // 处理影响场数据
            for (DZXXInfluenceDTO dzxx : dzxxs) {
                // 接收面数据
                Polygon polygon = (Polygon) dzxx.getGeom();
                // 转换Polygon为GeoJSON Geometry结构
                IntyGeoJsonDTO.GeoJsonFeature feature = handler.convertPolygonToGeoJsonFeature(polygon, dzxx);
                features.getFeatures().add(feature);
                // 根据批次编码拼接目录和文件名
                fineName = dzxx.getEqQueueId() + "/" + dzxx.getEqName();
                // 获取地震影响场信息
                influence.setEqQueueId(dzxx.getEqQueueId());
                influence.setEvent(dzxx.getEvent());
                influence.setName(dzxx.getEqName());
            }

            if (features.getFeatures() == null || features.getFeatures().isEmpty()) {
                throw new ServiceException(BaseConstants.INFLUENCE_CONVERT_ERROR);
            }

            // 保存GeoJSON文件
            handler.writeGeoJsonToFile(features, fineName);
            log.info("地震影响场GeoJson文件已生成成功!");

            influence.setPath(BaseConstants.INTENSITY_GEOJSON_PATH + fineName + ".geojson");
            handleDzxxData(influence);

        } catch (Exception ex) {
            log.error("影响场保存为geojson失败!：", ex.getMessage());
            throw new ServiceException(BaseConstants.INFLUENCE_CONVERT_ERROR);
        }
    }

    // 获取影响场文件
    @Override
    public Map<String, String> getInfluence(EqQuery query) {
        // 异常
        if (query == null) {
            throw new ParmaException(BaseConstants.PARAMS_ERROR);
        }
        // 条件
        LambdaQueryWrapper<DZInfluence> lambdaQuery = Wrappers.lambdaQuery(DZInfluence.class);
        lambdaQuery.select(DZInfluence::getPath);
        lambdaQuery.eq(DZInfluence::getEvent, query.getEvent());
        lambdaQuery.eq(DZInfluence::getEqQueueId, query.getEqQueueId());
        lambdaQuery.orderByDesc(DZInfluence::getCreateTime);    // 防止多条记录
        lambdaQuery.last("limit 1");
        // 查询
        DZInfluence influence = (DZInfluence) this.baseMapper.selectList(lambdaQuery);
        if (influence == null) {
            throw new ParmaException(BaseConstants.RESULT_ERROR);
        }
        Map<String, String> hashmap = new HashMap<>();
        hashmap.put("file", influence.getPath());
        return hashmap;
    }


    // 处理地震信息数据
    private void handleDzxxData(DZInfluenceDTO dto) {
        DZInfluence influence = new DZInfluence();
        BeanUtils.copyProperties(dto, influence);
        // 存库
        save(influence);
    }
}
