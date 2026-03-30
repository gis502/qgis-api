package com.ruoyi.system.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.common.constant.BaseConstants;
import com.ruoyi.common.exception.ParmaException;
import com.ruoyi.system.domain.dto.dzxx.DZXXInfluenceDTO;
import com.ruoyi.system.domain.dto.pub.IntyGeoJsonDTO;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.WKTReader;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName GeoFilesHandler
 * @Description 处理空间数据文件形式
 * @Author Huang Yx
 * @Date 2026/2/11 15:38
 */
@Slf4j
@Component
public class GeoFilesHandler {

    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();
    private static final WKTReader WKT_READER = new WKTReader(GEOMETRY_FACTORY);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public void writeGeoJsonToFile(IntyGeoJsonDTO featureCollection, String fileName) throws IOException {
        // 序列化
        String geoJsonStr = OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(featureCollection);
        // geojson文件路径
        String path = BaseConstants.INTENSITY_GEOJSON_PATH + fileName + ".geojson";
        // 创建文件目录
        File file = new File(path);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            boolean mkdirs = parentDir.mkdirs();
            if (!mkdirs) {
                throw new IOException("创建GeoJson文件目录失败!" + parentDir.getAbsolutePath());
            }
        }

        // 写入文件
        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
            writer.write(geoJsonStr);
        }
    }

    // 将JTS转换为GeoJSON坐标
    private List<List<Double>> convertCoordinateSequenceToGeoJson(CoordinateSequence cs) {
        List<List<Double>> coordinates = new ArrayList<>();
        for (int i = 0; i < cs.size(); i++) {
            double lon = cs.getX(i); // 经度
            double lat = cs.getY(i); // 纬度
            List<Double> point = new ArrayList<>();
            point.add(lon);
            point.add(lat);
            coordinates.add(point);
        }
        return coordinates;
    }

    // 将JTS Polygon转换为GeoJSON Feature对象
    public IntyGeoJsonDTO.GeoJsonFeature convertPolygonToGeoJsonFeature(Polygon polygon, DZXXInfluenceDTO dzxx) {
        IntyGeoJsonDTO.GeoJsonFeature feature = new IntyGeoJsonDTO.GeoJsonFeature();
        IntyGeoJsonDTO.GeoJsonFeature.GeoJsonGeometry geometry = new IntyGeoJsonDTO.GeoJsonFeature.GeoJsonGeometry();

        // 设置几何类型
        geometry.setType("Polygon");

        // 转换JTS坐标为GeoJSON坐标格式
        List<List<List<Double>>> coordinates = new ArrayList<>();
        LinearRing exteriorRing = polygon.getExteriorRing();
        List<List<Double>> exteriorCoordinates = convertCoordinateSequenceToGeoJson(exteriorRing.getCoordinateSequence());
        coordinates.add(exteriorCoordinates);

        // 处理内部环
        for (int i = 0; i < polygon.getNumInteriorRing(); i++) {
            LinearRing interiorRing = polygon.getInteriorRingN(i);
            coordinates.add(convertCoordinateSequenceToGeoJson(interiorRing.getCoordinateSequence()));
        }

        geometry.setCoordinates(coordinates);
        feature.setGeometry(geometry);

        // 烈度等级
        feature.getProperties().setEqAddr(dzxx.getEqName());
        feature.getProperties().setLatitude(dzxx.getLatitude());
        feature.getProperties().setLongitude(dzxx.getLongitude());
        feature.getProperties().setDescription("");
        feature.getProperties().setIntensity("烈度:" + dzxx.getSInty() + "度");
        return feature;
    }

}
