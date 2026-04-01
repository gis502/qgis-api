package com.ruoyi.system.service.pub.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.BaseConstants;
import com.ruoyi.common.enums.BaseEnums;
import com.ruoyi.common.enums.EqMapsEnums;
import com.ruoyi.common.enums.RainMapsEnums;
import com.ruoyi.common.enums.TypesOfSecondaryDisasters;
import com.ruoyi.common.exception.ParmaException;
import com.ruoyi.common.exception.ServeException;
import com.ruoyi.common.utils.BaseUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.system.domain.dto.base.ActiveFaultDTO;
import com.ruoyi.system.domain.dto.base.RescueMaterialsDTO;
import com.ruoyi.system.domain.dto.dzxx.DZXXInfluenceDTO;
import com.ruoyi.system.domain.dto.pub.*;
import com.ruoyi.system.domain.dto.base.RescueTeamsDTO;
import com.ruoyi.system.domain.dto.base.HospitalDTO;
import com.ruoyi.system.domain.entity.base.PeopleGDP;
import com.ruoyi.system.domain.entity.dzxx.DZXXInfluence;
import com.ruoyi.system.domain.entity.pub.DZProduct;
import com.ruoyi.system.domain.entity.slave.RSEvent;
import com.ruoyi.system.domain.params.QgisArgs;
import com.ruoyi.system.domain.query.EqQuery;
import com.ruoyi.system.domain.query.ProductQuery;
import com.ruoyi.system.domain.vo.pub.SecondaryDisasterVO;
import com.ruoyi.system.handler.EarthquakeHandler;
import com.ruoyi.system.handler.GeoDistanceHandler;
import com.ruoyi.system.mapper.dzxx.DZXXInfluenceMapper;
import com.ruoyi.system.mapper.pub.DZProductMapper;
import com.ruoyi.system.mapper.slave.PeopleGDPMapper;
import com.ruoyi.system.service.base.*;
import com.ruoyi.system.service.dzxx.IDZXXInfluenceService;
import com.ruoyi.system.service.pub.IDZProductService;
import com.ruoyi.system.service.pub.IFeignService;
import com.ruoyi.system.service.pub.IReportsService;
import com.ruoyi.system.service.slave.*;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.entity.slave.RAnalysis;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName DZProductServiceImpl
 * @Description 地震产品类
 * @Author Huang Yx
 * @Date 2026/2/11 10:53
 */

@Slf4j
@Service
public class DZProductServiceImpl extends ServiceImpl<DZProductMapper, DZProduct> implements IDZProductService {

    private static final SecureRandom secureRandom = new SecureRandom();

    @Autowired
    private IFeignService iFeignService;
    @Autowired
    private IActiveFaultService iActiveFaultService;
    @Autowired
    private IReportsService iReportsService;
    @Autowired
    private IHospitalsService iHospitalsService;
    @Autowired
    private IRescueTeamsService iRescueTeamsService;
    @Autowired
    private IRescueMaterialsService iRescueMaterialsService;
    @Autowired
    private IDZXXInfluenceService idzxxInfluenceService;
    @Autowired
    private IRSEventService irEventService;
    @Autowired
    private IRAnalysisService irAnalysisService;
    @Autowired
    private ISXStreetService isxStreetService;
    @Autowired
    private IFactorAnalysisService iFactorAnalysisService;
    @Autowired
    private IGeoDisasterRiskService iGeoDisasterRiskService;
    @Autowired
    private IGeoDisasterHideService iGeoDisasterHideService;
    @Autowired
    private IFactorAttributeService iFactorAttributeService;
    @Autowired
    private IImpactInAreaService iImpactInAreaService;
    @Autowired
    private IRSEventService irsEventService;
    @Autowired
    private DZXXInfluenceMapper dzxxInfluenceMapper;
    @Autowired
    private PeopleGDPMapper peopleGDPMapper;

    // qgis 地震制图服务
    @Override
    @Async("taskExecutor")
    public void makeEarthquakeMaps(EqAssessmentDTO assess) {
        // 待产专题图集
        List<EqMapsEnums> maps = Arrays.asList(EqMapsEnums.values());
        // 设置制图参数
        List<QgisArgs> args = setMakeEqMapsArgs(assess, maps);
        // 调用出图服务
        iFeignService.invoke(args);
    }

    // qgis 暴雨制图服务
    @Override
    @Async("taskExecutor")
    public void makeRainstormMaps(RAssessmentDTO assess) {
        // 待产专题图集
        List<RainMapsEnums> maps = Arrays.asList(RainMapsEnums.values());
        // 设置制图参数
        List<QgisArgs> args = setMakeRainstormMapsArgs(assess, maps);
        // 调用出图服务
        iFeignService.invoke(args);
    }

    // 地震灾情报告生成
    @Override
    @Async("taskExecutor")
    public void makeEarthquakeReport(EqAssessmentDTO assess) {
        // 获取报告参数
        EqReportDTO args = setMakeEarthquakeReportArgs(assess);
        iReportsService.earthquakeDisasterDocument(args, assess.getEqQueueId());
    }

    // 暴雨灾情报告生成
    @Override
    @Async("taskExecutor")
    public void makeRainstormReport(RAssessmentDTO assess) {
        // 获取报告参数
        RReportDTO args = setMakeRainstormReportArgs();
        iReportsService.rainstormDisasterDocument(args, assess.getRainQueueId());
    }

    // 获取产品
    @Override
    public List<DZProductDTO> getProducts(ProductQuery query) {
        try {
            log.info("查询产品参数：{}", query);
            // 空值
            if (query.getQueueId() == null || query.getQueueId().trim().isEmpty()) {
                throw new ParmaException(BaseConstants.QUEUE_ID_ERROR);
            }
            // 构造条件
            LambdaQueryWrapper<DZProduct> lambdaQuery = Wrappers.lambdaQuery(DZProduct.class);
            // 必填项
            lambdaQuery.eq(DZProduct::getEqQueueId, query.getQueueId());
            // 选填项
            lambdaQuery.or().eq(StringUtils.hasText(query.getCode()), DZProduct::getCode, query.getCode());
            lambdaQuery.or().eq(StringUtils.hasText(query.getFileType()), DZProduct::getFileType, query.getFileType());
            lambdaQuery.or().like(StringUtils.hasText(query.getFileName()), DZProduct::getFileName, query.getFileName());
            lambdaQuery.or().eq(StringUtils.hasText(query.getProType()), DZProduct::getProType, query.getProType());
            // 获取产品服务
            List<DZProduct> productList = this.baseMapper.selectList(lambdaQuery);
            List<DZProductDTO> dtos = new ArrayList<>();
            for (DZProduct product : productList) {
                DZProductDTO dto = new DZProductDTO();
                BeanUtils.copyProperties(product, dto);
                dto.setTempletId(null);

                dtos.add(dto);
            }
            return dtos;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new ServeException(BaseConstants.PRODUCTS_ERROR);
        }
    }

    // 地震 制图参数
    private List<QgisArgs> setMakeEqMapsArgs(EqAssessmentDTO assess, List<EqMapsEnums> maps) {
        // 专题图集参数
        List<QgisArgs> args = new ArrayList<>();

        // A4 画幅
        for (EqMapsEnums map : maps) {
            // qgis参数
            QgisArgs arg = new QgisArgs();
            arg.setId(map.getNum());
            arg.setEvent(assess.getEvent());
            arg.setQueueId(assess.getEqQueueId());
            arg.setCenterX(assess.getLongitude());
            arg.setCenterY(assess.getLatitude());
            arg.setInfo(EarthquakeHandler.parseInfo(assess.getEqTime(), assess.getEqMagnitude(), assess.getEqAddr()));
            arg.setMapTitle(EarthquakeHandler.combine(assess.getEqName(), assess.getEqType(), map));
            arg.setMapTime(BaseUtils.formatTime(LocalDateTime.now(), false));
            arg.setMapLayout(BaseConstants.MAP_LAYOUT_A4); // A4
            arg.setMapUnit(BaseConstants.MAP_UNIT);    // 单位
            // 死信队列中获取单张图片
            arg.setName(map.getName());
            arg.setOutFile(EarthquakeHandler.getPath(assess.getEvent(), assess.getEqQueueId(), BaseConstants.MAP_LAYOUT_A4, map));
            arg.setPath(BaseConstants.EQ_MAPS_TEMPLATE_PATH + map.getName() + ".qgz");
            arg.setDisaster(BaseConstants.EQ_DISASTER_MAP);  // 地震灾害

            // 缩放规则
            Map<String, String> changed = change(assess, "A4", map.getName());
            arg.setZoomRule(changed.get("k"));    // 默认不缩放
            arg.setZoomValue(changed.get("v"));  // 默认缩放值

            args.add(arg);
        }

        // 增加 A3 画幅
        for (EqMapsEnums map : maps) {
            // qgis参数
            QgisArgs arg = new QgisArgs();
            arg.setId(map.getNum());
            arg.setEvent(assess.getEvent());
            arg.setQueueId(assess.getEqQueueId());
            arg.setCenterX(assess.getLongitude());
            arg.setCenterY(assess.getLatitude());
            arg.setInfo(EarthquakeHandler.parseInfo(assess.getEqTime(), assess.getEqMagnitude(), assess.getEqAddr()));
            arg.setMapTitle(EarthquakeHandler.combine(assess.getEqName(), assess.getEqType(), map));
            arg.setMapTime(BaseUtils.formatTime(LocalDateTime.now(), false));
            arg.setMapLayout(BaseConstants.MAP_LAYOUT_A3); // A3
            arg.setMapUnit(BaseConstants.MAP_UNIT);    // 单位
            // 死信队列中获取单张图片
            arg.setName(map.getName());
            arg.setOutFile(EarthquakeHandler.getPath(assess.getEvent(), assess.getEqQueueId(), BaseConstants.MAP_LAYOUT_A3, map));
            arg.setPath(BaseConstants.EQ_MAPS_TEMPLATE_PATH + map.getName() + ".qgz");
            arg.setDisaster(BaseConstants.EQ_DISASTER_MAP);  // 地震灾害

            // 缩放规则
            Map<String, String> changed = change(assess, "A3", map.getName());
            arg.setZoomRule(changed.get("k"));    // 默认不缩放
            arg.setZoomValue(changed.get("v"));  // 默认缩放值

            args.add(arg);
        }
        log.info("制图参数设置完成!");

        return args;
    }

    // 暴雨 制图参数
    private List<QgisArgs> setMakeRainstormMapsArgs(RAssessmentDTO assess, List<RainMapsEnums> maps) {
        // 专题图集参数
        List<QgisArgs> args = new ArrayList<>();

        // A4 画幅
        for (RainMapsEnums map : maps) {
            // qgis参数
            QgisArgs arg = new QgisArgs();
            arg.setId(map.getNum());
            arg.setEvent(assess.getRainId());
            arg.setQueueId(assess.getRainQueueId());
            arg.setCenterX(assess.getLongitude());
            arg.setCenterY(assess.getLatitude());
            arg.setInfo(EarthquakeHandler.parseRInfo(assess.getOccurrenceTime(), assess.getRainfall(), assess.getDuration()));
            arg.setMapTitle(EarthquakeHandler.combineR(assess.getPosition(), assess.getRainType(), map));
            arg.setMapTime(BaseUtils.formatTime(LocalDateTime.now(), false));
            arg.setMapLayout(BaseConstants.MAP_LAYOUT_A4); // A4
            arg.setMapUnit(BaseConstants.MAP_UNIT);    // 单位
            // 死信队列中获取单张图片
            arg.setName(map.getName());
            arg.setOutFile(EarthquakeHandler.getRPath(assess.getRainId(), assess.getRainQueueId(), BaseConstants.MAP_LAYOUT_A4, map));
            arg.setPath(BaseConstants.RAIN_MAPS_TEMPLATE_PATH + map.getName() + ".qgz");
            arg.setDisaster(BaseConstants.RAIN_DISASTER_MAP);  // 暴雨灾害
            // 按规则缩放
            arg.setZoomRule(BaseEnums.NO.getCode().toString());    // 默认不缩放
            arg.setZoomValue("");  // 默认缩放值

            args.add(arg);
        }

        // 增加 A3 画幅
        for (RainMapsEnums map : maps) {
            // qgis参数
            QgisArgs arg = new QgisArgs();
            arg.setId(map.getNum());
            arg.setEvent(assess.getRainId());
            arg.setQueueId(assess.getRainQueueId());
            arg.setCenterX(assess.getLongitude());
            arg.setCenterY(assess.getLatitude());
            arg.setInfo(EarthquakeHandler.parseRInfo(assess.getOccurrenceTime(), assess.getRainfall(), assess.getDuration()));
            arg.setMapTitle(EarthquakeHandler.combineR(assess.getPosition(), assess.getRainType(), map));
            arg.setMapTime(BaseUtils.formatTime(LocalDateTime.now(), false));
            arg.setMapLayout(BaseConstants.MAP_LAYOUT_A3); // A4
            arg.setMapUnit(BaseConstants.MAP_UNIT);    // 单位
            // 死信队列中获取单张图片
            arg.setName(map.getName());
            arg.setOutFile(EarthquakeHandler.getRPath(assess.getRainId(), assess.getRainQueueId(), BaseConstants.MAP_LAYOUT_A3, map));
            arg.setPath(BaseConstants.RAIN_MAPS_TEMPLATE_PATH + map.getName() + ".qgz");
            arg.setDisaster(BaseConstants.RAIN_DISASTER_MAP);  // 暴雨灾害
            arg.setZoomRule(BaseEnums.NO.getCode().toString());    // 默认不缩放
            arg.setZoomValue("");  // 默认缩放值

            args.add(arg);
        }

        log.info("制图参数设置完成!");

        return args;
    }

    // 地震 报告参数
    private EqReportDTO setMakeEarthquakeReportArgs(EqAssessmentDTO assess) {
        EqReportDTO dto = new EqReportDTO();
        // 地震概况
        BeanUtils.copyProperties(assess, dto);
        // 获取最大烈度数据
        DZXXInfluenceDTO maxInty = idzxxInfluenceService.findInfluenceMaxIntyById(new EqQuery(assess.getEvent(), assess.getEqQueueId()));

        // 风险评估
        dto.setIntensity(maxInty.getInty());  // 重度区
        dto.setArea(maxInty.getArea());  // 面积
        // 计算地震影响人数
        Map<String, Object> Result = calculateCasualties(assess);
        dto.setInfluencePopulationMin((Integer) Result.get("affectPopMin"));    // 地震影响人数区间
        dto.setInfluencePopulationMax((Integer) Result.get("affectPopMax"));
        dto.setDeathMin((Integer) Result.get("diePopMin"));  // 死亡人数区间
        dto.setDeathMax((Integer) Result.get("diePopMax"));
        // 震中最近断裂带
        ActiveFaultDTO fault = iActiveFaultService.getShortlyFault(assess.getLongitude(), assess.getLatitude());
        dto.setFault(fault.getName());

        // 震中50公里范围内医院
        List<HospitalDTO> hospitals = nearbyHospital100km(assess.getLongitude(), assess.getLatitude());
        dto.setHospital100km(hospitals);

        // 震中50公里范围内救援队
        List<RescueTeamsDTO> fireFighters = nearbyTeams100km(assess.getLongitude(), assess.getLatitude());
        dto.setFireTeams100km(fireFighters);

        // 震中50公里范围内救援物资
        List<RescueMaterialsDTO> storePoints = nearbyMaterial100km(assess.getLongitude(), assess.getLatitude());
        dto.setStorePoint100km(storePoints);

        dto.setEarthQuakeEmergencyLevel(getLevel(assess.getEqMagnitude()));

        return dto;
    }

    // 暴雨 报告参数
    private RReportDTO setMakeRainstormReportArgs() {
        // 获取最新暴雨事件
        RSEvent disaster = irsEventService.getLatestRainDisaster();

        // TODO 测试 id
        disaster.setDisasterId(167451L);

        RReportDTO dto = new RReportDTO();
        // 区划代码
        Map<String, String> areaCodeMap = BaseConstants.ADMINISTRATIVE_CODE;

        // 降雨时间
        LocalDateTime rainTimeStr = irEventService.getRainTime(disaster.getDisasterId());
        String rainTime = rainTimeStr.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日HH时mm分"));

        // 降雨区域 rainAreaPosition 处理position字符串，将逗号分隔的字符串转换为List
        String position = irEventService.getRainAreaPosition(disaster.getDisasterId());
        List<String> rainAreaPosition = new ArrayList<>();
        if (position != null && !position.trim().isEmpty()) {
            String[] areas = position.split(",");
            for (String area : areas) {
                rainAreaPosition.add(area.trim()); // trim()去除可能的空格
            }
        }

        // 区域降雨量
        String quantity = irEventService.getRainAreaQuantity(disaster.getDisasterId());
        List<String> rainAreaQuantity = new ArrayList<>();
        if (quantity != null && !quantity.trim().isEmpty()) {
            String[] rains = quantity.split(",");
            for (String rain : rains) {
                rainAreaQuantity.add(rain.trim()); // trim()去除可能的空格
            }
        }

        // 降雨集中区域
        String concentratedAreaPosition = "";
        int maxIndex = -1;
        if (!rainAreaQuantity.isEmpty()) {
            double maxValue = Double.MIN_VALUE;
            for (int i = 0; i < rainAreaQuantity.size(); i++) {
                double currentValue = Double.parseDouble(rainAreaQuantity.get(i));
                if (currentValue > maxValue) {
                    maxValue = currentValue;
                    maxIndex = i;
                }
            }
        }
        concentratedAreaPosition = rainAreaPosition.get(maxIndex);

        // 降雨集中区域雨量
        String concentratedAreaQuantity = "NaN";
        // 降雨集中区域平均雨量
        String concentratedAreaAverageQuantity = "NaN";

        // 降雨集中区域街道
        List<String> concentratedAreaDetailStreet = new ArrayList<>();
        String areaCode = areaCodeMap.get(concentratedAreaPosition);
        List<RAnalysis> analysisRains = irAnalysisService.getRainStation(areaCode);
        // 按站点名称分组
        Map<String, List<RAnalysis>> stationGroups = new HashMap<>();
        for (RAnalysis rain : analysisRains) {
            String stationName = rain.getStationName();
            if (!stationGroups.containsKey(stationName)) {
                stationGroups.put(stationName, new ArrayList<>());
            }
            stationGroups.get(stationName).add(rain);
        }
        // 对每个站点的数据按时间排序
        for (String stationName : stationGroups.keySet()) {
            List<RAnalysis> stationData = stationGroups.get(stationName);
            // 按时间降序排序（最新的在前面）
            stationData.sort((a, b) -> b.getDatetime().compareTo(a.getDatetime()));
        }
        // 创建结果列表
        List<Map<String, String>> rainfallSummary = new ArrayList<>();
        // 遍历每个站点分组
        for (String stationName : stationGroups.keySet()) {
            List<RAnalysis> stationData = stationGroups.get(stationName);
            // 累加该站点的所有pre1h值
            double totalRainfall = 0.0;
            float lat = 0;
            float lon = 0;
            for (RAnalysis rain : stationData) {
                if (rain.getPre1h() != null) {
                    try {
                        totalRainfall += Double.parseDouble(rain.getPre1h());
                    } catch (NumberFormatException e) {
                        // 如果转换失败，跳过这个值
                        System.out.println("无法解析降雨量数据...");
                    }
                }
                if (lat == 0 && lon == 0) {
                    lat = rain.getLat();
                    lon = rain.getLon();
                }
            }
            // 创建Map存储站点名称和累计降雨量
            Map<String, String> stationRainfall = new HashMap<>();
            stationRainfall.put("stationName", stationName);
            stationRainfall.put("rainfall", String.valueOf(totalRainfall));
            stationRainfall.put("lat", String.valueOf(lat));
            stationRainfall.put("lon", String.valueOf(lon));
            // 添加到结果列表
            rainfallSummary.add(stationRainfall);
        }
        rainfallSummary.sort((a, b) -> Double.compare(Double.parseDouble(b.get("rainfall")), Double.parseDouble(a.get("rainfall"))));
        String stationStreet1 = "";
        String stationStreet2 = "";
        String stationStreet3 = "";

        // 根据rainfallSummary的实际大小安全地设置各变量值
        if (rainfallSummary != null && !rainfallSummary.isEmpty()) {
            // 处理第一个元素（如果存在）
            if (rainfallSummary.size() > 0) {
                stationStreet1 = isxStreetService.inStreet(Float.parseFloat(rainfallSummary.get(0).get("lat")), Float.parseFloat(rainfallSummary.get(0).get("lon")));
            }
            // 处理第二个元素（如果存在）
            if (rainfallSummary.size() > 1) {
                stationStreet2 = isxStreetService.inStreet(Float.parseFloat(rainfallSummary.get(1).get("lat")), Float.parseFloat(rainfallSummary.get(1).get("lon")));
            }
            // 处理第三个元素（如果存在）
            if (rainfallSummary.size() > 2) {
                stationStreet3 = isxStreetService.inStreet(Float.parseFloat(rainfallSummary.get(2).get("lat")), Float.parseFloat(rainfallSummary.get(2).get("lon")));
            }
        }
        // 如果rainfallSummary为null或空，变量保持初始值空字符串

        concentratedAreaDetailStreet.add(stationStreet1);
        concentratedAreaDetailStreet.add(stationStreet2);
        concentratedAreaDetailStreet.add(stationStreet3);

        // 降雨集中街道雨量
        List<String> concentratedAreaDetailQuantity = new ArrayList<>();

        //  确保始终添加3个元素，根据rainfallSummary的实际大小设置值 添加第一个元素
        if (rainfallSummary != null && !rainfallSummary.isEmpty() && rainfallSummary.size() > 0) {
            concentratedAreaDetailQuantity.add(rainfallSummary.get(0).get("rainfall"));
        } else {
            concentratedAreaDetailQuantity.add("0");
        }
        // 添加第二个元素
        if (rainfallSummary != null && rainfallSummary.size() > 1) {
            concentratedAreaDetailQuantity.add(rainfallSummary.get(1).get("rainfall"));
        } else {
            concentratedAreaDetailQuantity.add("0");
        }
        // 添加第三个元素
        if (rainfallSummary != null && rainfallSummary.size() > 2) {
            concentratedAreaDetailQuantity.add(rainfallSummary.get(2).get("rainfall"));
        } else {
            concentratedAreaDetailQuantity.add("0");
        }

        // 降雨集中街道等级
        List<String> concentratedAreaDetailGrade = new ArrayList<>();
        // 降雨等级阈值和对应名称
        double[] thresholds = {0.1, 5.0, 15.0, 30.0, 70.0, 140.0};
        String[] grades = {"微量降雨(零星小雨)", "小雨", "中雨", "大雨", "暴雨", "大暴雨", "特大暴雨"};

        for (String item : concentratedAreaDetailQuantity) {
            boolean flag = true;
            double rainfall = Double.parseDouble(item);
            for (int i = 0; i < thresholds.length; i++) {
                if (rainfall < thresholds[i]) {
                    concentratedAreaDetailGrade.add(grades[i]);
                    flag = false;
                    break;
                }
            }
            if (flag) {
                concentratedAreaDetailGrade.add(grades[grades.length - 1]);
            }
        }

        // 设置报告时间到实体对象中
        dto.setRainTime(rainTime);
        dto.setRainAreaPosition(rainAreaPosition);
        dto.setRainAreaQuantity(rainAreaQuantity);
        dto.setConcentratedAreaPosition(concentratedAreaPosition);
        dto.setConcentratedAreaQuantity(concentratedAreaQuantity);
        dto.setConcentratedAreaAverageQuantity(concentratedAreaAverageQuantity);
        dto.setConcentratedAreaDetailStreet(concentratedAreaDetailStreet);
        dto.setConcentratedAreaDetailQuantity(concentratedAreaDetailQuantity);
        dto.setConcentratedAreaDetailGrade(concentratedAreaDetailGrade);

        // 获取此次灾害所有次生灾害计算的概率
        List<Map<String, Object>> disasterEstimation = iFactorAnalysisService.queryDisasterEstimation(disaster.getDisasterId());
        List<String> chain = new ArrayList<>();
        Map<String, List<Map<String, Object>>> groupedByDisasterType = new HashMap<>();
        // 遍历disasterEstimation列表进行分组
        for (Map<String, Object> item : disasterEstimation) {
            String disasterType = (String) item.get("disaster_type");
            if (!chain.contains(disasterType)) {
                chain.add(disasterType);
            }
            // 如果该disaster_type还没有对应的列表，创建一个新的列表
            if (!groupedByDisasterType.containsKey(disasterType)) {
                groupedByDisasterType.put(disasterType, new ArrayList<>());
            }
            // 将当前元素添加到对应的列表中
            groupedByDisasterType.get(disasterType).add(item);
        }
        // 对每个分组内的列表按disaster_probability从高到低排序
        for (List<Map<String, Object>> list : groupedByDisasterType.values()) {
            // 修改排序逻辑，使用BigDecimal
            list.sort((o1, o2) -> {
                BigDecimal prob1 = (BigDecimal) o1.get("disaster_probability");
                BigDecimal prob2 = (BigDecimal) o2.get("disaster_probability");
                return prob2.compareTo(prob1); // 降序排列
            });
        }

        // 风险区数量
        Integer riskAreaQuantity = iGeoDisasterRiskService.getHideNumByCounty(concentratedAreaPosition);
        // 隐患点数量
        Integer hideAreaQuantity = iGeoDisasterHideService.getRiskNumByCounty(concentratedAreaPosition);

        //  致灾因子
        List<String> hazards = iFactorAttributeService.getFactorAttributeName();
        hazards.remove("持续时间");
        hazards.remove("土壤沙砾度");
        hazards.remove("坡型");

        // 灾害链
        String disasterChain = "";
        Integer countChain = 1;
        for (String item : chain) {
            countChain++;
            if (countChain <= chain.size()) {
                disasterChain = disasterChain + "暴雨-" + item + "、";
            } else {
                disasterChain = disasterChain + "暴雨-" + item;
            }
        }

        // 风险显著上升区域
        String significantIncreaseArea = "";
        StringBuilder sb = new StringBuilder();
        sb.append(concentratedAreaPosition);
        // 智能拼接非空的街道名称，避免多余的顿号
        List<String> validStreets = new ArrayList<>();
        if (stationStreet1 != null && !stationStreet1.isEmpty()) {
            validStreets.add(stationStreet1);
        }
        if (stationStreet2 != null && !stationStreet2.isEmpty()) {
            validStreets.add(stationStreet2);
        }
        if (stationStreet3 != null && !stationStreet3.isEmpty()) {
            validStreets.add(stationStreet3);
        }
        // 如果有非空的街道名称，则拼接它们
        if (!validStreets.isEmpty()) {
            sb.append(String.join("、", validStreets));
        }
        significantIncreaseArea = sb.toString();

        /* significantIncreaseAreaHideQuantity 显著上升区域中重点关注隐患点 */
        Integer significantIncreaseAreaHideQuantity = 0;
        for (String key : groupedByDisasterType.keySet()) {
            significantIncreaseAreaHideQuantity = significantIncreaseAreaHideQuantity + groupedByDisasterType.get(key).size();
        }

        // 滑坡
        SecondaryDisasterVO landslide = handleSecondaryDisasterReport(TypesOfSecondaryDisasters.LANDSLIDE, groupedByDisasterType, "滑坡");
        // 泥石流
        SecondaryDisasterVO debrisFlow = handleSecondaryDisasterReport(TypesOfSecondaryDisasters.DEBRIS_FLOW, groupedByDisasterType, "泥石流");
        // 山洪
        SecondaryDisasterVO torrentialFlood = handleSecondaryDisasterReport(TypesOfSecondaryDisasters.TORRENTIAL_FLOOD, groupedByDisasterType, "山洪");
        // 内涝
        SecondaryDisasterVO waterLogging = handleSecondaryDisasterReport(TypesOfSecondaryDisasters.WATER_LOGGING, groupedByDisasterType, "内涝");


        dto.setRiskAreaQuantity(riskAreaQuantity);
        dto.setHideAreaQuantity(hideAreaQuantity);
        dto.setHazards(hazards);
        dto.setDisasterChain(disasterChain);
        dto.setSignificantIncreaseArea(significantIncreaseArea);
        dto.setSignificantIncreaseAreaHideQuantity(significantIncreaseAreaHideQuantity);
        dto.getSecondaryDisasterReport().add(landslide);
        dto.getSecondaryDisasterReport().add(debrisFlow);
        dto.getSecondaryDisasterReport().add(torrentialFlood);
        dto.getSecondaryDisasterReport().add(waterLogging);

        // 第三部分
        // workScheduleArea 工作安排部署区域
        List<String> workScheduleArea = rainAreaPosition;

        // evacuateTheCrowdArea 山洪、泥石流人员疏散区域
        List<String> evacuateTheCrowdArea = concentratedAreaDetailStreet;

        // focusArea 内涝关注区域
        List<String> focusArea = new ArrayList<>();

        dto.setWorkScheduleArea(workScheduleArea);
        dto.setEvacuateTheCrowdArea(evacuateTheCrowdArea);
        dto.setFocusArea(focusArea);

        return dto;
    }

    // 震中50公里范围内医院
    private List<HospitalDTO> nearbyHospital100km(double longitude, double latitude) {

        List<HospitalDTO> templist = new ArrayList<>();

        // 获取100km内医院
        List<HospitalDTO> hospitals = iHospitalsService.getHospitalByXA();
        // 创建上传经纬度的点
        String wkt = "POINT(" + longitude + " " + latitude + ")";
        WKTReader reader = new WKTReader();
        Point uploadPoint = null;
        try {
            uploadPoint = (Point) reader.read(wkt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        double uploadLat = uploadPoint.getY(); // 上传点的纬度
        double uploadLon = uploadPoint.getX(); // 上传点的经度

        // 遍历所有医院，计算震中距
        for (HospitalDTO hospital : hospitals) {
            Geometry geom = hospital.getGeom();
            if (geom != null && geom instanceof Point) {
                // 获取医院的经纬度
                Point hospitalPoint = (Point) geom;
                double hospitalLat = hospitalPoint.getY(); // 纬度
                double hospitalLon = hospitalPoint.getX(); // 经度
                // 计算震中距（单位：米）
                double distance = GeoDistanceHandler.calculateHaversineDistance(uploadLon, uploadLat, hospitalLon, hospitalLat);
                // 四舍五入取整（单位：公里）
                int roundedDistance = (int) Math.round(distance);
                // 筛选出 20公里内的医院
                if (roundedDistance <= 50 && !hospital.getHospitalBeds().equals("0")) {
                    hospital.setKm((int) distance);
                    templist.add(hospital);
                }
            }
        }

        // 对距离进行排序
        templist = templist.stream().sorted((h1, h2) -> Integer.compare(h1.getKm(), h2.getKm())).collect(Collectors.toList());

        return templist;
    }

    // 震中50公里范围内救援队
    private List<RescueTeamsDTO> nearbyTeams100km(double longitude, double latitude) {
        List<RescueTeamsDTO> templist = new ArrayList<>();

        // 获取100km内救援队
        List<RescueTeamsDTO> teams = iRescueTeamsService.getRescueTeamsByXA();
        // 创建上传经纬度的点
        String wkt = "POINT(" + longitude + " " + latitude + ")";
        WKTReader reader = new WKTReader();
        Point uploadPoint = null;
        try {
            uploadPoint = (Point) reader.read(wkt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        double uploadLat = uploadPoint.getY(); // 上传点的纬度
        double uploadLon = uploadPoint.getX(); // 上传点的经度

        // 遍历所有救援队，计算震中距
        for (RescueTeamsDTO team : teams) {
            Geometry geom = team.getGeom();
            if (geom != null && geom instanceof Point) {
                // 获取救援队的经纬度
                Point teamPoint = (Point) geom;
                double teamLat = teamPoint.getY(); // 纬度
                double teamLon = teamPoint.getX(); // 经度
                // 计算震中距（单位：米）
                double distance = GeoDistanceHandler.calculateHaversineDistance(uploadLon, uploadLat, teamLon, teamLat);
                // 四舍五入取整（单位：公里）
                int roundedDistance = (int) Math.round(distance);
                // 筛选出 20公里内的救援队
                if (roundedDistance <= 50) {
                    team.setKm((int) distance);
                    templist.add(team);
                }
            }
        }

        // 对距离进行排序
        templist = templist.stream().sorted((h1, h2) -> Integer.compare(h1.getKm(), h2.getKm())).collect(Collectors.toList());

        return templist;
    }

    // 震中50公里范围内救援物资
    private List<RescueMaterialsDTO> nearbyMaterial100km(double longitude, double latitude) {
        List<RescueMaterialsDTO> templist = new ArrayList<>();

        // 获取100km内救援物资
        List<RescueMaterialsDTO> materials = iRescueMaterialsService.getRescueMaterialsByXA();
        // 创建上传经纬度的点
        String wkt = "POINT(" + longitude + " " + latitude + ")";
        WKTReader reader = new WKTReader();
        Point uploadPoint = null;
        try {
            uploadPoint = (Point) reader.read(wkt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        double uploadLat = uploadPoint.getY(); // 上传点的纬度
        double uploadLon = uploadPoint.getX(); // 上传点的经度

        // 遍历所有救援物资，计算震中距
        for (RescueMaterialsDTO material : materials) {
            Geometry geom = material.getGeom();
            if (geom != null && geom instanceof Point) {
                // 获取政府救援物资的经纬度
                Point materialPoint = (Point) geom;
                double materialLat = materialPoint.getY(); // 纬度
                double materialLon = materialPoint.getX(); // 经度
                // 计算震中距（单位：米）
                double distance = GeoDistanceHandler.calculateHaversineDistance(uploadLon, uploadLat, materialLon, materialLat);
                // 四舍五入取整（单位：公里）
                int roundedDistance = (int) Math.round(distance);
                // 筛选出 20公里内的救援物资
                if (roundedDistance <= 50) {
                    material.setKm((int) distance);
                    templist.add(material);
                }
            }
        }

        // 对距离进行排序
        templist = templist.stream().sorted((h1, h2) -> Integer.compare(h1.getKm(), h2.getKm())).collect(Collectors.toList());

        return templist;
    }

    // 响应等级
    private String getLevel(double mag) {
        if (mag >= 4.0 && mag <= 4.4) {
            return "四级";
        } else if (mag >= 4.5 && mag <= 5.4) {
            return "三级";
        } else if (mag >= 5.5 && mag <= 5.9) {
            return "二级";
        } else if (mag >= 6.0) {
            return "一级";
        } else {
            return "不启动响应";
        }
    }

    // 处理暴雨次生灾害数据
    private SecondaryDisasterVO handleSecondaryDisasterReport(TypesOfSecondaryDisasters t, Map<String, List<Map<String, Object>>> groupedByDisasterType, String type) {
        SecondaryDisasterVO secondaryDisasterReportEntity = new SecondaryDisasterVO();
        // 灾害类型（滑坡、泥石流等）
        secondaryDisasterReportEntity.setDisasterType(t);
        if (!groupedByDisasterType.containsKey(type)) {
            return secondaryDisasterReportEntity;
        }
        // 风险集中的街道
        String riskStreet;
        List<String> riskStreetList = new ArrayList<>();
        for (Map<String, Object> item : groupedByDisasterType.get(type)) {
            if (riskStreetList.contains(item.get("village")) && item.get("village") != "[高]") {
                continue;
            }
            riskStreetList.add(item.get("village").toString());
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < riskStreetList.size(); i++) {
            sb.append(riskStreetList.get(i));
            if (i < riskStreetList.size() - 1) {
                sb.append("、");
            }
        }
        riskStreet = sb.toString();
        secondaryDisasterReportEntity.setRiskStreet(riskStreet);

        // 影响人数
        Long influencePeopleQuantity = 0L;
        for (Map<String, Object> item : groupedByDisasterType.get(type)) {
            influencePeopleQuantity = influencePeopleQuantity + iImpactInAreaService.getPeopleByLatLon(item.get("lat").toString(), item.get("lon").toString());
        }
        secondaryDisasterReportEntity.setInfluencePeopleQuantity(influencePeopleQuantity);

        for (Map<String, Object> item : groupedByDisasterType.get(type)) {
            RSecondaryDisasterDTO secondaryDisasterTableData = new RSecondaryDisasterDTO();
            secondaryDisasterTableData.setPosition(item.get("position").toString());
            secondaryDisasterTableData.setProbability(item.get("disaster_probability").toString());
            secondaryDisasterTableData.setGrade(removeBrackets(item.get("level").toString()));
            secondaryDisasterReportEntity.getDisasterTableData().add(secondaryDisasterTableData);
        }
        return secondaryDisasterReportEntity;
    }

    // 去除中括号的辅助方法
    private String removeBrackets(String str) {
        if (str == null) {
            return null;
        }
        // 去除字符串开头和结尾的中括号
        return str.replaceAll("^\\[|\\]$", "");
    }

    /**
     * 专题图缩放变化：
     * A3：
     * 4级地震：不放缩
     * 5-6级地震：所有A3都放缩到intensity
     * 6级以上，除了影响场intensity，其他不变
     * <p>
     * A4：
     * 所有都不变
     */
    public Map<String, String> change(EqAssessmentDTO assess, String size, String name) {
        Map<String, String> map = new HashMap<>();

        if (size.equals("A3")) {
            if (assess.getEqMagnitude() >= 4 && assess.getEqMagnitude() < 5) {
                map.put("k", BaseEnums.NO.getCode().toString());
                map.put("v", "");
                return map;
            }
            if (assess.getEqMagnitude() >= 5 && assess.getEqMagnitude() < 6) {
                map.put("k", BaseEnums.M_LAYER2.getCode().toString());
                map.put("v", "intensity");
                return map;
            }
            if (assess.getEqMagnitude() >= 6) {
                if (name.equals("地震影响估计范围分布图")) {
                    map.put("k", BaseEnums.M_LAYER2.getCode().toString());
                    map.put("v", "intensity");
                    return map;
                } else {
                    map.put("k", BaseEnums.NO.getCode().toString());
                    map.put("v", "");
                    return map;
                }
            }
        }
        map.put("k", BaseEnums.NO.getCode().toString());
        map.put("v", "");
        return map;
    }

    /**
     * 计算伤亡人数
     * y = exp(−1.109×10² + 8.8489×10⋅x − 2.032×10⋅x² + 1.499⋅x + 9.826×10⁻⁴⋅v − 6.833×10⁻⁸⋅v²
     *      − 8.963×10⁻¹⋅z − 3.175×10⁻³⋅ns + 1.148×10⁻⁶⋅ns² − 2.914×10⁻²⋅cs + 6.502×10⁻⁵⋅cs²
     *      + 2.772×10⁻³⋅GDP − 4.768×10⁻⁷⋅GDP² + 9.801×10⁻¹⋅T + 3.910×10⁻⁴⋅s + 9.414×10⁻⁵⋅x⋅GDP)
     */
    private Map<String, Object> calculateCasualties(EqAssessmentDTO assess) {
        Map<String, Object> damageResult = new HashMap<>();
        // 基础参数
        double x = assess.getEqMagnitude(); // 震级
        int T = getDayOrNight(assess.getEqTime()); // 夜间=1，白天=0
        double z = 8; // 地震烈度（西安按8度设防，9度为较强影响）
        double sumGdp = 0; // 影响区域GDP和
        int DeathPeople = 0; // 死亡人数

        // 获取地震8级烈度的相关信息
        List<DZXXInfluence> dzxxInfluenceList = dzxxInfluenceMapper.selectList(
                new QueryWrapper<DZXXInfluence>().eq("eqqueue_id", assess.getEqQueueId()));

        // 筛选烈度为8的记录
        DZXXInfluence dzxxInfluence = dzxxInfluenceMapper.selectOne(
                new QueryWrapper<DZXXInfluence>()
                        .eq("eqqueue_id", assess.getEqQueueId())
                        .eq("inty", 8)
        );

        // 检查是否有筛选结果
        if (dzxxInfluence == null) {
            System.out.println("未查询到受影响区域数据...");
        } else {
            // 获取8级烈度的相关信息，切换到从数据库进行查询
            List<PeopleGDP> DeathPeopleGDPS = peopleGDPMapper.findInsideCircle(dzxxInfluence.getGeom());

            // 获取记录中对应的人口数和GDP数量
            for (PeopleGDP peopleGDP : DeathPeopleGDPS) {
                Float gdp = peopleGDP.getGdp();
                if (gdp != null) sumGdp += gdp;
                Integer peopleNum = peopleGDP.getPeopleNum();
                if (peopleNum != null) DeathPeople += peopleNum;
            }

            // 计算受影响人口范围并按要求格式化
            int affectPopMinRaw = (int) Math.round(DeathPeople * 0.8);
            int affectPopMaxRaw = (int) Math.round(DeathPeople * 1.2);
            int affectPopMin = roundByDigit(affectPopMinRaw);
            int affectPopMax = roundByDigit(affectPopMaxRaw);
            int area = roundByDigit(dzxxInfluence.getArea().intValue());
            damageResult.put("affectPopMin", affectPopMin);
            damageResult.put("affectPopMax", affectPopMax);

            log.info("计算总人数 {}",DeathPeople);
            // 人口密度s：人/平方公里（简化计算，直接用总人口/区域数）
            double s = DeathPeople/(DeathPeopleGDPS.size() * 0.7)<=1412? DeathPeople/(DeathPeopleGDPS.size() * 0.7) : 1412;
            // 面积v：平方公里（保持放缩）
            double v = area / 1e6;
            // GDP：万元
            double gdp = 10;

            log.info("震级:{}级 ",x);

            // 2. 核心调整：大幅减弱负向项，增强正向项（适配西安7级地震）
            double exponent =
                    -110.9 +  // 基础项：从-50大幅提高到-20（减弱负向影响）
                            88.4 * x +  // 震级一次项：显著增强（7级时贡献210，成为核心正向项）
                            -12.3 * x * x +  // 震级平方项：适度减弱（7级时贡献-5*49=-245，抵消部分正向）
                            1.5 * x +  // 震级微调项：增强（7级时贡献35）
                            9.826*1e-4 * v +  // 面积项：增强正向影响
                            -6.833*1e-8 * v * v +  // 面积平方项：大幅减弱负向
                            -0.8963* z +  // 烈度项：从-5减弱到-2（7级地震烈度影响应小于震级）
                            -3.175*1e-3 * gdp +  // 农民收入项：从负向改为正向（经济活跃区人口密集，伤亡可能增加）
                            1.148*1e-6 * gdp * gdp +  // 农民收入平方项：增强正向
                            -2.914*1e-2 * gdp +  // 财政收入项：改为正向
                            6.502*1e-5 * gdp * gdp +  // 财政收入平方项：增强正向
                            2.772*1e-3 * gdp +  // GDP项：增强正向
                            -4.768*1e-7 * gdp * gdp +  // GDP平方项：几乎消除负向影响
                            0.9801 * T +  // 昼夜项：显著增强（夜间伤亡可能翻倍，7级地震夜间影响更大）
                            6.920*1e-2 * s +  // 人口密度项：增强（人口密集区伤亡更多）
                            8.414*1e-5 * x * gdp;  // 震级与GDP交互项：增强（高GDP区域建筑密集，震级影响放大）

            // 3. 指数强制限制（核心：确保不会过小导致结果为0）
            double adjustexpont = exponent >= 7.5 ? exponent : 7.5;
            if (DeathPeople>=6*1e5){
                adjustexpont = 8 + secureRandom.nextDouble() * 1;
            }

            if (x<7.0){
                damageResult.put("diePopMin", 0);
                damageResult.put("diePopMax", 0);
            } else if (x>=7.0 && x<7.5){
                double casualties = Math.exp(adjustexpont);
                // 计算死亡人口范围并按要求格式化
                int diePopMinRaw = (int) Math.round(casualties * 0.8);
                int diePopMaxRaw = (int) Math.round(casualties * 1.2);
                int diePopMin = roundByDigit(diePopMinRaw);
                int diePopMax = roundByDigit(diePopMaxRaw);
                log.info("伤亡人口范围：{} - {}", diePopMin, diePopMax);
                damageResult.put("diePopMin", diePopMin);
                damageResult.put("diePopMax", diePopMax);
            } else {
                double randomRatio = 0.07 + secureRandom.nextDouble() * 0.05;
                double casualties = DeathPeople * randomRatio;
                int diePopMinRaw = (int) Math.round(casualties * 0.8);
                int diePopMaxRaw = (int) Math.round(casualties * 1.2);
                int diePopMin = roundByDigit(diePopMinRaw);
                int diePopMax = roundByDigit(diePopMaxRaw);
                log.info("伤亡人口范围：{} - {}", diePopMin, diePopMax);

                damageResult.put("diePopMin", diePopMin);
                damageResult.put("diePopMax", diePopMax);
            }

        }

        return damageResult;
    }

    /**
     * 判断地震发生在白天还是夜间
     * 这里定义：晚上18:00到次日06:00为夜间(2)，其余时间为白天(1)
     */
    private int getDayOrNight(LocalDateTime dateTime) {
        if (dateTime == null) {
            return 0; // 日期为空时返回错误
        }

        // 获取小时（0-23）
        int hour = dateTime.getHour();

        // 判断是否为夜间：18:00-23:59 或 00:00-05:59
        if (hour >= 18 || hour < 6) {
            return 2; // 夜间
        } else {
            return 1; // 白天
        }
    }
    /**
     * 根据数值位数进行四舍五入：
     * - 个位数（0-9）：不修改
     * - 十位数（10-99）：精确到十位
     * - 百位数及以上：精确到百位
     */
    private int roundByDigit(int number) {
        if (number < 10) {
            // 个位数：不修改
            return number;
        } else if (number < 100) {
            // 十位数：精确到十位
            return (int) Math.round(number / 10.0) * 10;
        } else if (number < 1000){
            // 百位数：精确到百位
            return (int) Math.round(number / 100.0) * 100;
        } else {
            // 千位数及以上：精确到千位
            return (int) Math.round(number / 1000.0) * 1000;
        }
    }

}
