package com.ruoyi.system.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

/**
 * @ClassName GeoDistanceUtils
 * @Description 地理距离计算工具类
 * @Author Huang Yx
 * @Date 2026/1/30 1:32
 */

@Slf4j
public class GeoDistanceHandler {

    /**
     *地球平均半径（千米）
     */
    private static final double EARTH_RADIUS = 6371.0;
    /**
     * 计算两个经纬度点之间的球面距离（单位：千米）
     * @param lon1 点1经度
     * @param lat1 点1纬度
     * @param lon2 点2经度
     * @param lat2 点2纬度
     * @return 两点之间的距离（千米）
     */
    public static double calculateHaversineDistance(double lon1, double lat1, double lon2, double lat2) {
        // 参数校验（经纬度合理范围）
        Assert.isTrue(lon1 >= -180 && lon1 <= 180 && lat1 >= -90 && lat1 <= 90, "点1经纬度格式非法");
        Assert.isTrue(lon2 >= -180 && lon2 <= 180 && lat2 >= -90 && lat2 <= 90, "点2经纬度格式非法");

        // 转换为弧度（Math类三角函数默认使用弧度）
        double radLat1 = Math.toRadians(lat1);
        double radLon1 = Math.toRadians(lon1);
        double radLat2 = Math.toRadians(lat2);
        double radLon2 = Math.toRadians(lon2);

        // 计算纬度差、经度差
        double deltaLat = radLat1 - radLat2;
        double deltaLon = radLon1 - radLon2;

        // Haversine公式核心计算
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // 返回距离（千米）
        return EARTH_RADIUS * c;
    }

    /**
     * 计算震中点到断层的最短球面距离（单位：千米）
     * @param lon  震中经度
     * @param lat  震中纬度
     * @param lonA 线段端点A经度
     * @param latA 线段端点A纬度
     * @param lonB 线段端点B经度
     * @param latB 线段端点B纬度
     * @return 震中到线段的最短距离（千米）
     */
    public static double calculateDistanceFromPointToLineString(double lon, double lat, double lonA, double latA, double lonB, double latB) {
        // 将经纬度转换为平面直角坐标系近似值
        double x = lon;
        double y = lat;
        double xA = lonA;
        double yA = latA;
        double xB = lonB;
        double yB = latB;

        // 计算向量AB和向量AP
        double vectorABx = xB - xA;
        double vectorABy = yB - yA;
        double vectorAPx = x - xA;
        double vectorAPy = y - yA;

        // 计算点P在AB线段上的投影参数t（判断垂足是否在线段内）
        // t = (AP · AB) / |AB|²
        double dotProduct = vectorAPx * vectorABx + vectorAPy * vectorABy; // 点积
        double abLengthSquared = vectorABx * vectorABx + vectorABy * vectorABy; // AB长度的平方

        // 线段AB是一个点（A和B坐标重合），直接返回点到点的距离
        if (abLengthSquared < 1e-10) {
            return calculateHaversineDistance(lon, lat, lonA, latA);
        }

        // 确定垂足位置
        double t = Math.max(0, Math.min(1, dotProduct / abLengthSquared)); // t限制在[0,1]之间（线段范围内）

        // 计算垂足点C的坐标（平面坐标）
        double xC = xA + t * vectorABx;
        double yC = yA + t * vectorABy;

        // 计算震中点到垂足点C的球面距离（最终最短距离）
        return calculateHaversineDistance(lon, lat, xC, yC);
    }

    /**
     * 解析LINESTRING格式字符串，提取所有端点坐标
     * @param wktLineString LINESTRING格式的WKT字符串
     * @return 端点坐标二维数组（[经度, 纬度]）
     */
    public static double[][] parseLineStringCoordinates(String wktLineString) {
        // 先判空并去除首尾多余空格
        if (wktLineString == null || wktLineString.trim().isEmpty()) {
            throw new IllegalArgumentException("WKT字符串不能为空或空白");
        }
        String cleanWkt = wktLineString.trim();

        // 截取括号内的坐标字符串
        int startIndex = cleanWkt.indexOf("(") + 1; // 找到第一个左括号，取其后一位
        int endIndex = cleanWkt.lastIndexOf(")"); // 找到最后一个右括号
        String coordStr = cleanWkt.substring(startIndex, endIndex).trim();

        // 括号内无坐标数据
        if (coordStr.isEmpty()) {
            throw new IllegalArgumentException("LINESTRING内无有效坐标数据");
        }

        // 按逗号分割多个坐标点
        String[] pointStrArray = coordStr.split(",");
        double[][] coordinates = new double[pointStrArray.length][2];

        // 解析每个坐标点（经度 纬度）
        for (int i = 0; i < pointStrArray.length; i++) {
            String pointStr = pointStrArray[i].trim();
            // 容错：单个坐标点为空
            if (pointStr.isEmpty()) {
                throw new IllegalArgumentException("LINESTRING内存在空坐标点，索引：" + i);
            }

            String[] lonLat = pointStr.split("\\s+");
            if (lonLat.length != 2) {
                throw new IllegalArgumentException("LINESTRING内坐标格式无效：" + pointStr);
            }

            try {
                double lon = Double.parseDouble(lonLat[0]);
                double lat = Double.parseDouble(lonLat[1]);
                coordinates[i][0] = lon; // 经度
                coordinates[i][1] = lat; // 纬度
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("LINESTRING内坐标无法转换为数字：" + pointStr, e);
            }
        }

        return coordinates;
    }

    /**
     * 解析POINT格式WKT字符串，提取经纬度坐标
     * @param wktPoint POINT格式的WKT字符串
     * @return 坐标数组 [经度, 纬度]
     */
    public static double[] parsePointCoordinates(String wktPoint) {
        // 判空和去除首尾空格
        if (wktPoint == null || wktPoint.trim().isEmpty()) {
            throw new IllegalArgumentException("POINT类型WKT字符串不能为空或空白");
        }
        String cleanWkt = wktPoint.trim();

        // 校验POINT格式前缀和后缀
        boolean isValidPrefix = cleanWkt.startsWith("POINT(") || cleanWkt.startsWith("POINT (");
        boolean isValidSuffix = cleanWkt.endsWith(")");
        if (!isValidPrefix || !isValidSuffix) {
            throw new IllegalArgumentException("无效的POINT格式WKT字符串：" + wktPoint);
        }

        // 截取括号内的坐标字符串
        int startIndex = cleanWkt.indexOf("(") + 1;
        int endIndex = cleanWkt.lastIndexOf(")");
        String coordStr = cleanWkt.substring(startIndex, endIndex).trim();

        // 校验括号内是否有有效坐标
        if (coordStr.isEmpty()) {
            throw new IllegalArgumentException("POINT内无有效坐标数据：" + wktPoint);
        }

        // 分割经度和纬度（支持任意数量空格分隔）
        String[] lonLat = coordStr.split("\\s+");
        if (lonLat.length != 2) {
            throw new IllegalArgumentException("POINT坐标格式无效，需为 经度 纬度 格式：" + coordStr);
        }

        //  转换为数字并返回
        try {
            double lon = Double.parseDouble(lonLat[0]);
            double lat = Double.parseDouble(lonLat[1]);
            // 校验经纬度范围
            Assert.isTrue(lon >= -180 && lon <= 180, "经度超出合法范围[-180,180]：" + lon);
            Assert.isTrue(lat >= -90 && lat <= 90, "纬度超出合法范围[-90,90]：" + lat);
            return new double[]{lon, lat};
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("POINT坐标无法转换为数字：" + coordStr, e);
        }
    }

    /**
     * 计算震中点到行政区划点（POINT类型）的球面距离（单位：千米）
     * @param epicenterLon   震中经度
     * @param epicenterLat   震中纬度
     * @param regionWktPoint 行政区划点的POINT格式WKT字符串
     * @return 震中到该行政区划点的距离（千米）
     */
    public static double calculateDistanceFromEpicenterToRegionPoint(double epicenterLon, double epicenterLat, String regionWktPoint) {
        try {
            //  行政区划点经纬度
            double[] regionCoord = parsePointCoordinates(regionWktPoint);
            double regionLon = regionCoord[0];
            double regionLat = regionCoord[1];

            // 球面距离公式计算距离
            return calculateHaversineDistance(epicenterLon, epicenterLat, regionLon, regionLat);
        } catch (Exception e) {
            log.error("计算震中到行政区划点距离失败，WKT字符串：{}，异常信息：{}", regionWktPoint, e.getMessage(), e);
            throw new RuntimeException("计算行政区划点距离失败", e);
        }
    }

}