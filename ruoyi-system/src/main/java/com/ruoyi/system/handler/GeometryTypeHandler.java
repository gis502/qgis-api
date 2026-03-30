package com.ruoyi.system.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;
import org.postgis.PGgeometry;
import org.postgresql.util.PGobject;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 修复编译报错版：PostgreSQL geometry → JTS Geometry 类型处理器
 * 解决 JTS Geometry 与 PostGIS Geometry 类的冲突问题
 */
@MappedTypes(Geometry.class) // 实体类字段类型（JTS 的 Geometry）
@MappedJdbcTypes(JdbcType.OTHER) // 数据库 geometry 对应 JDBC 类型 OTHER
public class GeometryTypeHandler extends BaseTypeHandler<Geometry> {

    // WKT 解析器（JTS → 数据库，线程安全）
    private static final WKTReader WKT_READER = new WKTReader();
    // WKT 生成器（数据库 → JTS，线程安全）
    private static final WKTWriter WKT_WRITER = new WKTWriter();

    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory(
            new PrecisionModel(PrecisionModel.FLOATING),
            4490
    );

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Geometry parameter, JdbcType jdbcType) throws SQLException {
        try {
            //  将 JTS 的 Geometry 转为标准 WKT 字符串
            String wktText = WKT_WRITER.write(parameter);
            //  用 WKT 字符串创建 PGgeometry 对象
            PGgeometry pgGeometry = new PGgeometry(wktText);
            //  存入 PreparedStatement
            ps.setObject(i, pgGeometry);
        } catch (Exception e) {
            throw new SQLException("将 JTS Geometry 转为 PGgeometry 失败", e);
        }
    }

    @Override
    public Geometry getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return convertToGeometry(rs.getObject(columnName));
    }

    @Override
    public Geometry getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return convertToGeometry(rs.getObject(columnIndex));
    }

    @Override
    public Geometry getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return convertToGeometry(cs.getObject(columnIndex));
    }

    private Geometry convertToGeometry(Object value) {
        // 处理 null 值
        if (value == null) {
            return null;
        }

        String wktText = null;
        try {
            // 兼容 PostGIS 的 PGgeometry 格式
            if (value instanceof PGgeometry) {
                PGgeometry pgGeometry = (PGgeometry) value;
                // 转为标准 WKT 文本
                wktText = pgGeometry.toString();
            }
            // 兼容 PostgreSQL 的 PGobject 格式
            else if (value instanceof PGobject) {
                PGobject pgObject = (PGobject) value;
                String pgValue = pgObject.getValue();
                if (pgValue != null && !pgValue.trim().isEmpty()) {
                    wktText = new PGgeometry(pgValue).toString();
                }
            }
            // 兼容纯 WKT 字符串格式
            else if (value instanceof String) {
                wktText = (String) value;
            }
            // 未知格式直接返回 null
            else {
                return null;
            }

            // 通用清洗 WKT 字符串
            String pureWkt = cleanWkt(wktText);
            if (pureWkt != null && !pureWkt.trim().isEmpty() && !"NULL".equalsIgnoreCase(pureWkt)) {
                Geometry geometry = WKT_READER.read(pureWkt);
                // 给几何对象设置 SRID=4490
                geometry.setSRID(4490);
                return geometry;
            }
            return null;

        } catch (Exception e) {
            System.err.println("解析 geometry 字段失败，原始 WKT：" + wktText + "，异常：" + e.getMessage());
            return null;
        }
    }

    /**
     * 通用版 WKT 清洗方法：支持 Point、LINESTRING 等常见几何类型
     */
    private String cleanWkt(String wkt) {
        if (wkt == null) {
            return null;
        }
        // 步骤1：去除所有不可见字符（换行、回车、制表符）
        String cleaned = wkt.replaceAll("[\n\r\t]", "");
        // 步骤2：去除 SRID 前缀（如 "SRID=4326;"）
        cleaned = cleaned.replaceAll("^SRID=\\d+;", "");
        // 步骤3：将多个连续空格替换为单个空格（避免坐标间多空格干扰）
        cleaned = cleaned.replaceAll("\\s+", " ");
        // 步骤4：去除几何类型与括号间的多余空格（适配 Point、LINESTRING）
        cleaned = cleaned.replaceAll("(POINT|LINESTRING|POLYGON)\\s*\\(", "$1(");
        cleaned = cleaned.replaceAll("\\)\\s*$", ")");
        // 步骤5：去除首尾多余空格
        cleaned = cleaned.trim();
        // 步骤6：验证是否为支持的几何类型（可根据需求扩展）
        if (!cleaned.startsWith("POINT(") && !cleaned.startsWith("LINESTRING(") && !cleaned.startsWith("POLYGON(")) {
            System.err.println("不支持的几何类型，WKT：" + cleaned);
            return null;
        }
        return cleaned;
    }
}