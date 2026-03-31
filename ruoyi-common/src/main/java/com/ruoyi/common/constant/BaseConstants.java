package com.ruoyi.common.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName BaseConstants
 * @Description 公共常量类
 * @Author Huang Yx
 * @Date 2026/1/30 1:13
 */
public class BaseConstants {


    // url
    public static final String EQ_MAPS_TEMPLATE_PATH = "D:/home/dzxx/maps/eq/";     // 地震专题图模板路径
    public static final String RAIN_MAPS_TEMPLATE_PATH = "D:/home/dzxx/maps/rain/";     // 暴雨专题图模板路径
    public static final String INTENSITY_GEOJSON_PATH = "D:/home/dzxx/output/influence/";     // 影响场保存路径
    public static final String EQ_MAPS_OUTPUT_PATH = "D:/home/dzxx/output/eq/map/";     // 地震专题图文件输出路径
    public static final String RAIN_MAPS_OUTPUT_PATH = "D:/home/dzxx/output/rain/map/";     // 暴雨专题图文件输出路径
    public static final String EQ_DOCUMENT_OUTPUT_PATH = "D:/home/dzxx/output/eq/地震应急预评估报告.docx";     // 地震评估报告文件输出路径
    public static final String RAIN_DOCUMENT_OUTPUT_PATH = "D:/home/dzxx/output/rain/暴雨应急预评估报告.docx";     // 暴雨评估报告文件输出路径

    // ip
    public static final String HTTP_QGIS_THEMATIC = "http://localhost:18998/qgis/make/map";    // 专题图

    // 提示语
    public static final String PARAMS_ERROR = "参数有误,请重新传入!";
    public static final String RESULT_ERROR = "结果空值!";
    public static final String QUEUE_ID_ERROR = "queueId不能为空!";
    public static final String PRODUCTS_ERROR = "获取产品失败!";
    public static final String POM_ERROR = "经纬度坐标错误!";
    public static final String EQ_SERVER_ERROR = "地震业务出错!";
    public static final String RAIN_SERVER_ERROR = "暴雨业务出错!";
    public static final String ASSESS_SERVER_ERROR = "评估业务出错!";
    public static final String INFLUENCE_CONVERT_ERROR = "影响场转换GeoJson出错!";
    public static final String UPLOAD_FAILED = "文件上传失败";
    public static final String FILE_NOT_FOUND_ERROR = "文件不存在";
    public static final String EQ_DISASTER_MAP = "地震专题图";
    public static final String RAIN_DISASTER_MAP = "暴雨专题图";
    public static final String EQ_DISASTER_DOCUMENT = "地震评估文档";
    public static final String RAIN_DISASTER_DOCUMENT = "暴雨评估文档";
    public static final String THEMATIC_FAILED = "专题图获取失败";
    public static final String FILE_FAILED = "评估报告获取失败";

    // 功能常量
    public static final int TOKEN_EXPIRED = 30;     // token过期时间
    public static final String AUTH_USERNAME = "authorize_user";   // 授权用户名
    public static final String AUTH_PWD = "ApiOpen#123";
    public static final int NUM_VERTICES = 100;     // 椭圆边缘顶点数
    public static final String CITY_CODE = "511800"; // 西安市行政编码
    public static final Integer AUTOMATIC = 0; // 自动
    public static final Integer MANUAL = 1; // 手动
    public static final Integer EQ_TOTAL_FILES = 20;    // 38张地震专题图(A3、A4) + 1个影响场文件
    public static final Integer RAIN_TOTAL_FILES = 15;    // 30张地震专题图(A3、A4)
    public static final Map<Integer, String> SEISMIC_INTENSITY_MAPPING = new HashMap<Integer, String>()     // 烈度映射
    {{
        put(4, "Ⅳ");
        put(5, "Ⅴ");
        put(6, "Ⅵ");
        put(7, "Ⅶ");
        put(8, "Ⅷ");
        put(9, "Ⅸ");
        put(10, "Ⅹ");
        put(11, "Ⅺ");
        put(12, "Ⅻ");
    }};
    public static final Map<String, String> ADMINISTRATIVE_CODE = new HashMap<String, String>() {{  // 西安行政区划代码
        put("新城区", "610102");
        put("碑林区", "610103");
        put("莲湖区", "610104");
        put("雁塔区", "610113");
        put("灞桥区", "610111");
        put("未央区", "610112");
        put("阎良区", "610114");
        put("临潼区", "610115");
        put("长安区", "610116");
        put("高陵区", "610117");
        put("鄠邑区", "610118");
        put("蓝田县", "610122");
        put("周至县", "610124");
    }};

    // RabbitMQ 常量
    public final static String MAPS_QUEUE = "maps";   // 专题图类队列
    public final static String DOCUMENTS_QUEUE = "documents";     // 文档类队列
    public final static String DLQ_QUEUE = "dlq";   // 死信队列
    public final static String ASSESS_EXCHANGE = "assess";    // 定义普通交换机名称
    public final static String DLX_EXCHANGE  = "dlx";    // 定义普通交换机名称

    // 制图参数常量
    public final static String MAP_LAYOUT_A3 = "A3";    // A3 画幅尺寸
    public final static String MAP_LAYOUT_A4 = "A4";    // A4 画幅尺寸
    public final static String MAP_UNIT = "制图单位：西安市应急管理局";
    public final static String MAP_TIME = "制图时间：";
    public final static String EQ_TEST = "（测试地震）";
    public final static String EQ_NORMAL = "（真实地震）";
    public final static String EQ_EXERCISE = "（演练地震）";
    public final static String RAIN_TEST = "（测试降雨）";
    public final static String RAIN_NORMAL = "（真实降雨）";
    public final static String RAIN_EXERCISE = "（演练降雨）";
    public static final Map<String, String> EQ_TYPE = new HashMap<String, String>()     // 地震类型映射
    {{
        put("T", EQ_TEST);
        put("Z", EQ_NORMAL);
        put("Y", EQ_EXERCISE);
    }};
    public static final Map<String, String> RAIN_TYPE = new HashMap<String, String>()     // 暴雨类型映射
    {{
        put("T", RAIN_TEST);
        put("Z", RAIN_NORMAL);
        put("Y", RAIN_EXERCISE);
    }};

}
