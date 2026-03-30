package com.ruoyi.system.domain.dto.pub;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName GeoJsonFeatureDTO
 * @Description 影响场geojson格式规范
 * @Author Huang Yx
 * @Date 2026/2/11 15:40
 */
@Data
public class IntyGeoJsonDTO {

    private final String type = "FeatureCollection";
    private List<GeoJsonFeature> features = new ArrayList<>();

    @Data
    public static class GeoJsonFeature {
        private final String type = "Feature";
        private GeoJsonGeometry geometry;
        private GeoJsonProperties properties = new GeoJsonProperties();

        @Data
        public static class GeoJsonGeometry {
            private String type;
            private List<List<List<Double>>> coordinates; // 坐标数组
        }

        @Data
        public static class GeoJsonProperties {
            private String intensity; // 烈度等级
            private String eqAddr;
            private Double longitude;
            private Double latitude;
            private String description = "地震影响场范围";
        }
    }


}
