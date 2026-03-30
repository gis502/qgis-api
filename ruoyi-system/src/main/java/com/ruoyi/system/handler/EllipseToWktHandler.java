package com.ruoyi.system.handler;

import com.ruoyi.system.domain.query.IntensityQuery;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.proj4j.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName EllipseToWkt
 * @Description 椭圆烈度衰减圈转换为WKT格式
 * @Author Huang Yx
 * @Date 2026/1/29 19:47
 */

@Component
public class EllipseToWktHandler {
    // 投影转换工厂
    private final CRSFactory crsFactory = new CRSFactory();
    private final CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
    GeometryFactory geometryFactory = new GeometryFactory();

    /**
     * 将椭圆参数转换为 POLYGON 格式的 WKT 字符串
     * @param query 椭圆烈度参数
     * @return POLYGON格式的WKT字符串
     */
    public Polygon ellipseToPolygonWkt(IntensityQuery query) {
        try {
            // 确定UTM投影带并创建转换器
            int utmZone = (int) Math.floor((query.getCenterLon() + 180) / 6) + 1;
            String epsgCode = query.getCenterLat() >= 0 ? "EPSG:326" + utmZone : "EPSG:327" + utmZone;

            // 创建坐标参考系（WGS84 -> UTM）
            CoordinateReferenceSystem wgs84 = crsFactory.createFromName("EPSG:4326");
            CoordinateReferenceSystem utm = crsFactory.createFromName(epsgCode);

            // 创建转换器
            CoordinateTransform toUtmTransform = ctFactory.createTransform(wgs84, utm);
            CoordinateTransform toWgs84Transform = ctFactory.createTransform(utm, wgs84);

            // 将中心点经纬度转换为UTM坐标
            ProjCoordinate centerWgs84 = new ProjCoordinate(query.getCenterLon(), query.getCenterLat());
            ProjCoordinate centerUtm = new ProjCoordinate();
            toUtmTransform.transform(centerWgs84, centerUtm);
            double centerX = centerUtm.x;
            double centerY = centerUtm.y;

            // 生成椭圆的平面顶点（极坐标方式）
            double rotationRad = Math.toRadians(query.getRotation());
            List<ProjCoordinate> verticesUtm = new ArrayList<>();

            // 生成0到2π的角度序列
            double angleStep = 2 * Math.PI / query.getNumVertices();
            for (int i = 0; i < query.getNumVertices(); i++) {
                double theta = i * angleStep;
                // 椭圆极坐标方程
                double xEllipse = query.getSemiMajor() * Math.cos(theta);
                double yEllipse = query.getSemiMinor() * Math.sin(theta);
                // 旋转矩阵计算
                double xRot = xEllipse * Math.cos(rotationRad) - yEllipse * Math.sin(rotationRad);
                double yRot = xEllipse * Math.sin(rotationRad) + yEllipse * Math.cos(rotationRad);
                // 加上中心点坐标
                double xFinal = centerX + xRot;
                double yFinal = centerY + yRot;

                verticesUtm.add(new ProjCoordinate(xFinal, yFinal));
            }

            // 将UTM顶点转换回经纬度
            List<ProjCoordinate> verticesWgs84 = new ArrayList<>();
            for (ProjCoordinate utmCoord : verticesUtm) {
                ProjCoordinate wgs84Coord = new ProjCoordinate();
                toWgs84Transform.transform(utmCoord, wgs84Coord);
                verticesWgs84.add(wgs84Coord);
            }

            // 拼接WKT字符串（保留6位小数，闭合多边形）
            StringBuilder verticesStr = new StringBuilder();
            for (ProjCoordinate coord : verticesWgs84) {
                verticesStr.append(roundTo6Decimals(coord.x))
                        .append(" ")
                        .append(roundTo6Decimals(coord.y))
                        .append(", ");
            }

            // 添加第一个点闭合多边形
            ProjCoordinate firstCoord = verticesWgs84.get(0);
            verticesStr.append(roundTo6Decimals(firstCoord.x))
                    .append(" ")
                    .append(roundTo6Decimals(firstCoord.y));
            // 构建最终WKT字符串
            String POLYGON = String.format("POLYGON ((%s)) | 4490", verticesStr);
            // 返回Polygon类型
            Polygon polygon = buildPolygonFromWKTString(POLYGON);

            return polygon;
        } catch (Exception e) {
            throw new RuntimeException("生成烈度多边形失败", e);
        }
    }

    /**
     * 解析WKT获取经纬度范围
     */
    public void printLatLonRange(String wkt) {
        // 提取顶点坐标
        String coordsPart = wkt.replace("POLYGON ((", "").replace("))", "");
        String[] coordStrs = coordsPart.split(", ");

        double minLon = Double.MAX_VALUE;
        double maxLon = Double.MIN_VALUE;
        double minLat = Double.MAX_VALUE;
        double maxLat = Double.MIN_VALUE;

        for (String coordStr : coordStrs) {
            String[] lonLat = coordStr.split(" ");
            double lon = Double.parseDouble(lonLat[0]);
            double lat = Double.parseDouble(lonLat[1]);

            minLon = Math.min(minLon, lon);
            maxLon = Math.max(maxLon, lon);
            minLat = Math.min(minLat, lat);
            maxLat = Math.max(maxLat, lat);
        }

        System.out.println("\n椭圆的经纬度范围：");
        System.out.printf("经度范围：%.6f ~ %.6f%n", minLon, maxLon);
        System.out.printf("纬度范围：%.6f ~ %.6f%n", minLat, maxLat);
    }

    /**
     * 保留6位小数
     */
    private double roundTo6Decimals(double value) {
        return new BigDecimal(value).setScale(6, RoundingMode.HALF_UP).doubleValue();
    }

    // 构建 Polygon 对象
    public Polygon buildPolygonFromWKTString(String wktPolygon) {
        try {
            //  WKT解析器
            WKTReader wktReader = new WKTReader(geometryFactory);
            // 解析 POLYGON 字符串
            Geometry geometry = wktReader.read(wktPolygon);
            return (Polygon) geometry;
        } catch (Exception e) {
            throw new RuntimeException("解析 POLYGON 字符串失败", e);
        }
    }


}
