package com.ruoyi.system.service.pub.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.config.DocumentConfig;
import com.ruoyi.common.constant.BaseConstants;
import com.ruoyi.common.enums.ImagePositionEnum;
import com.ruoyi.common.enums.ImageTypeEnum;
import com.ruoyi.common.exception.ServeException;
import com.ruoyi.common.utils.file.DocumentUtils;
import com.ruoyi.system.domain.dto.pub.EqReportDTO;
import com.ruoyi.system.domain.dto.pub.RReportDTO;
import com.ruoyi.system.domain.entity.pub.DZProduct;
import com.ruoyi.system.domain.params.DocumentArgs;
import com.ruoyi.system.domain.vo.pub.SecondaryDisasterVO;
import com.ruoyi.system.handler.EarthquakeHandler;
import com.ruoyi.system.mapper.pub.DZProductMapper;
import com.ruoyi.system.service.pub.IReportsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName ReportsServiceImpl
 * @Description
 * @Author Huang Yx
 * @Date 2026/3/12 15:23
 */
@Slf4j
@Service
public class ReportsServiceImpl implements IReportsService {

    @Autowired
    private DZProductMapper dzProductMapper;
    @Resource
    private RabbitTemplate rabbitTemplate;

    // 地震灾害文档
    @Override
    public void earthquakeDisasterDocument(EqReportDTO args, String queueId) {
        log.info("开始创建地震灾情评估报告...");

        // 创建一个新的Word文档
        try (XWPFDocument document = new XWPFDocument();
             FileOutputStream out = new FileOutputStream(BaseConstants.EQ_DOCUMENT_OUTPUT_PATH)) {
            // 创建保密提示
            createConfidentialityTip(document, "对内掌握");
            // 三个空行
            DocumentUtils.createBlankLine(document, 4);
            // 创建标题
            DocumentUtils.createTitle(document, "地震应急预评估报告", 1);
            // 一个空行
            DocumentUtils.createBlankLine(document, 3);
            // 创建部门信息
            createDept(document, "西安市应急管理局                " + EarthquakeHandler.format(LocalDateTime.now()));
            // 两个空行
            DocumentUtils.createBlankLine(document, 2);
            // 地震概况
            createEarthquakeBasicInfo(document, args);
            // 风险评估
            createEarthquakeAffected(document, args, queueId);
            // 救援需求
            createIntensityQuickReport(document, args, queueId);
            // 应急处置建议
            createCopeEarthquakes(document, args);
            // 保存文档
            document.write(out);
            log.info("地震灾情评估报告已创建完成！");

            settingArgs(BaseConstants.EQ_DISASTER_DOCUMENT, queueId);
        } catch (FileNotFoundException e) {
            log.error("地震灾情评估报告创建失败,文件 Not Found 404！");
            throw new ServeException(BaseConstants.EQ_SERVER_ERROR);
        } catch (IOException e) {
            log.error("地震灾情评估报告创建失败,文件流输出失败！");
            throw new ServeException(BaseConstants.EQ_SERVER_ERROR);
        }
    }

    // 暴雨灾害文档
    @Override
    public void rainstormDisasterDocument(RReportDTO args, String queueId) {
        log.info("开始创建暴雨灾情评估报告...");

        // 创建一个新的Word文档
        try (XWPFDocument document = new XWPFDocument();
             FileOutputStream out = new FileOutputStream(BaseConstants.RAIN_DOCUMENT_OUTPUT_PATH)) {
            // 创建保密提示
            createConfidentialityTip(document, "对内掌握");
            // 三个空行
            DocumentUtils.createBlankLine(document, 4);
            // 创建标题
            DocumentUtils.createTitle(document, "暴雨应急预评估报告", 1);
            // 一个空行
            DocumentUtils.createBlankLine(document, 3);
            // 创建部门信息
            createDept(document, "西安市应急管理局                " + EarthquakeHandler.format(LocalDateTime.now()));
            // 两个空行
            DocumentUtils.createBlankLine(document, 2);
            // 降雨概况
            createRainstormBasicInfo(document, args);
            // 风险评估
            createRainstormAffected(document, args, queueId);
            // 如果没有灾害，就不添加应急处置
            if (addEmergencyResponseSuggestions(args)) {
                // 应急处置建议
                createCopeRainstorm(document, args);
            }
            // 保存文档
            document.write(out);
            log.info("暴雨灾情评估报告已创建完成！");

            settingArgs(BaseConstants.RAIN_DISASTER_DOCUMENT, queueId);
        } catch (FileNotFoundException e) {
            log.error("暴雨灾情评估报告创建失败,文件 Not Found 404！");
            throw new ServeException(BaseConstants.RAIN_SERVER_ERROR);
        } catch (IOException e) {
            log.error("暴雨灾情评估报告创建失败,文件流输出失败！");
            throw new ServeException(BaseConstants.RAIN_SERVER_ERROR);
        }
    }

    // 创建保密提示
    private void createConfidentialityTip(XWPFDocument doc, String tip) {
        XWPFParagraph paragraph = DocumentUtils.addRegularParagraph(doc, tip);
        paragraph.setAlignment(ParagraphAlignment.RIGHT);
    }

    // 创建部门信息
    private void createDept(XWPFDocument doc, String dept) {
        XWPFParagraph paragraph = DocumentUtils.addRegularParagraph(doc, null);
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        paragraph.setFirstLineIndent(0);
        paragraph.setSpacingBetween(1.5);// 设置1.5倍行距
        paragraph.setSpacingBefore(0); // 段前0倍行距
        paragraph.setSpacingAfter(2); // 段后0行距

        XWPFRun run = paragraph.createRun();
        run.setText(dept);
        run.setFontSize(DocumentConfig.FONT_SIZE_THREE);
        run.setFontFamily(DocumentConfig.FONT_FANG_SONG_GB2312);

        // 一个空行
        DocumentUtils.createBlankLine(doc, 1);
        // 添加边框线
        DocumentUtils.addParagraphBorderLine(paragraph, null);
    }

    /**
     * 一、地震概况
     *
     * @param doc
     */
    private void createEarthquakeBasicInfo(XWPFDocument doc, EqReportDTO args) {
        // 标题
        DocumentUtils.createTitle(doc, "一、地震概况", 3);
        // 内容
        XWPFParagraph paragraph = DocumentUtils.addRegularParagraph(doc, null);

        // 拼接地震基本信息
        String content = String.format("据中国地震台网正式测定，" +
                        "北京时间%s在%s（北纬%s度，东经%s度）发生%s级地震，" +
                        "震源深度%s公里。",
                EarthquakeHandler.formatTime(args.getEqTime()),
                args.getEqAddr(),
                args.getLatitude(), args.getLongitude(),
                args.getEqMagnitude(),
                args.getEqDepth());
        // 添加普通段落
        DocumentUtils.addRegularContents(paragraph, content);
        // 一个空行
        DocumentUtils.createBlankLine(doc, 1);
    }

    /**
     * 二、风险评估
     *
     * @param doc
     */
    private void createEarthquakeAffected(XWPFDocument doc, EqReportDTO args, String queueId) {
        DocumentUtils.createTitle(doc, "二、风险评估", 3);
        // （一）影响范围
        DocumentUtils.createTitle(doc, "（一）影响范围", 4);
        // 影响内容
        XWPFParagraph influenceParagraph = DocumentUtils.addRegularParagraph(doc, null);

        //
        String influence = String.format("本次地震震中所在地区%s。" +
                        "本次地震重灾区烈度预计超过%s度，重灾区面积约%s平方公里；" +
                        "地震影响人口约%s-%s人，预计伤亡人数约%s-%s人。",
                args.getEqAddr(), args.getIntensity(), args.getArea(),
                args.getInfluencePopulationMin(), args.getInfluencePopulationMax(),
                args.getDeathMin(), args.getDeathMax());

        // 添加段落
        DocumentUtils.addRegularContents(influenceParagraph, influence);
        // 换行
        DocumentUtils.createBlankLine(doc, 1);
        log.info("等待插入地震影响估计范围分布图！");
        // 地震与周边县区位置关系图
        DocumentUtils.insertImageWithCaption(doc, getThematic(queueId, "地震影响估计范围分布图"),
                ImageTypeEnum.JPEG, null, null, "",
                ImagePositionEnum.AFTER);
        // 图片注释
        DocumentUtils.createTitle(doc, "地震影响估计范围分布图", 5);
        // 换行
        DocumentUtils.createBlankLine(doc, 1);
        log.info("已插入地震影响估计范围分布图！");
        // （二）活动断裂分布
        DocumentUtils.createTitle(doc, "（二）活动断裂分布", 4);
        // 活动断层内容
        XWPFParagraph faultParagraph = DocumentUtils.addRegularParagraph(doc, null);

        String fault = String.format("震中距离震中最近的断裂是%s，震中及周边活动断裂带分布情况如下图所示。", args.getFault());
        // 添加段落
        DocumentUtils.addRegularContents(faultParagraph, fault);
        // 换行
        DocumentUtils.createBlankLine(doc, 1);
        log.info("等待插入震中附近活动断裂分布图！");
        // 震中附近活动断裂分布图
        DocumentUtils.insertImageWithCaption(doc, getThematic(queueId, "震中附近活动断裂分布图"),
                ImageTypeEnum.JPEG, null, null, "",
                ImagePositionEnum.AFTER);
        // 图片注释
        DocumentUtils.createTitle(doc, "震中附近活动断裂分布图", 5);
        // 换行
        DocumentUtils.createBlankLine(doc, 1);
        log.info("已插入震中附近活动断裂分布图！");

        // （三）震中附近医院分布
        DocumentUtils.createTitle(doc, "（三）周边医院分布", 4);
        // 医院内容
        XWPFParagraph communitiesParagraph = DocumentUtils.addRegularParagraph(doc, null);

        String hospital = String.format("本次地震震中100公里范围内的医院（一级及以上）分布情况如表格所示。");

        // 添加段落
        DocumentUtils.addRegularContents(communitiesParagraph, hospital);
        // 换行
        DocumentUtils.createBlankLine(doc, 1);
        log.info("等待插入震区医院分布图！");
        // 震区医院分布图
        DocumentUtils.insertImageWithCaption(doc, getThematic(queueId, "震区医院分布图"),
                ImageTypeEnum.JPEG, null, null, "",
                ImagePositionEnum.AFTER);
        // 图片注释
        DocumentUtils.createTitle(doc, "震区医院分布图", 5);
        // 换行
        DocumentUtils.createBlankLine(doc, 2);
        log.info("已插入震区医院分布图！");
        DocumentUtils.createTitle(doc, "表1：Ⅷ（8）度极震区医院表", 5);

        // 创建表格
        DocumentUtils.createGenericTable(doc,
                args.getHospital100km(),
                DocumentUtils.stringArray2List("医院名称", "地址", "医院等级", "总床位", "距离(km)"),    // 表头
                DocumentUtils.stringArray2List("hospitalName", "hospitalAddress", "hospitalLevel", "hospitalBeds", "km"),
                true);
        // 换行
        DocumentUtils.createBlankLine(doc, 1);
    }

    /**
     * 三、救援需求
     *
     * @param doc
     */
    private void createIntensityQuickReport(XWPFDocument doc, EqReportDTO args, String queueId) {
        DocumentUtils.createTitle(doc, "三、救援需求", 3);
        // 内容
        XWPFParagraph teamsParagraph = DocumentUtils.addRegularParagraph(doc, null);

        // 救援队伍
        String rescueTeams = String.format("本次地震震中100公里范围内驻扎救援队伍分布情况如表格所示。");

        // 添加段落
        DocumentUtils.addRegularContents(teamsParagraph, rescueTeams);

        log.info("等待插入震中附近救援队伍分布图！");
        // 震中附近救援队伍分布图
        DocumentUtils.insertImageWithCaption(doc, getThematic(queueId, "震中附近救援队伍分布图"),
                ImageTypeEnum.JPEG, null, null, "",
                ImagePositionEnum.AFTER);
        // 图片注释
        DocumentUtils.createTitle(doc, "震中附近救援队伍分布图", 5);
        // 换行
        DocumentUtils.createBlankLine(doc, 2);
        log.info("已插入震中附近救援队伍分布图！");
        DocumentUtils.createTitle(doc, "表2：救援队伍信息表", 5);

        // 创建表格
        DocumentUtils.createGenericTable(doc,
                args.getFireTeams100km(),
                DocumentUtils.stringArray2List("队伍名称", "队伍类型", "地址", "总人数", "距离(km)"),    // 表头
                DocumentUtils.stringArray2List("fireFighterName", "fireFighterType", "fireFighterAddress", "fireFighterNum", "km"),
                true);
        // 换行
        DocumentUtils.createBlankLine(doc, 1);

        // 内容
        XWPFParagraph materialParagraph = DocumentUtils.addRegularParagraph(doc, null);

        // 抗震设防烈度
        String material = String.format("本次地震震中100公里范围内救援物资分布情况如表格所示。");
        // 添加段落
        DocumentUtils.addRegularContents(materialParagraph, material);
        log.info("等待插入震区附近救援物资分布图！");
        // 震区附近救援物资分布图
        DocumentUtils.insertImageWithCaption(doc, getThematic(queueId, "震区附近救援物资分布图"),
                ImageTypeEnum.JPEG, null, null, "",
                ImagePositionEnum.AFTER);
        // 图片注释
        DocumentUtils.createTitle(doc, "震区附近救援物资分布图", 5);
        // 换行
        DocumentUtils.createBlankLine(doc, 2);
        log.info("已插入震区附近救援物资分布图！");
        DocumentUtils.createTitle(doc, "表3：救援物资信息表", 5);

        // 创建表格
        DocumentUtils.createGenericTable(doc,
                args.getStorePoint100km(),
                DocumentUtils.stringArray2List("储备库名称", "地址", "所属部门", "有效库容", "距离(km)"),    // 表头
                DocumentUtils.stringArray2List("storePointName", "storePointAddress", "storePointDep", "storePointNum", "km"),
                true);
        // 换行
        DocumentUtils.createBlankLine(doc, 1);

    }

    /**
     * 四、应急处置建议
     *
     * @param doc
     */
    private void createCopeEarthquakes(XWPFDocument doc, EqReportDTO args) {
        DocumentUtils.createTitle(doc, "四、应急处置建议", 3);
        // 内容
        XWPFParagraph paragraph = DocumentUtils.addRegularParagraph(doc, null);

        if (args.getEarthQuakeEmergencyLevel().equals("不启动响应")) {
            String emergency = String.format("初步判定本次地震事件属于较小地震灾害，建议%s。",
                    args.getEarthQuakeEmergencyLevel());
            // 添加段落
            DocumentUtils.addRegularContents(paragraph, emergency);
        } else {
            String[] contents = {
                    String.format("本次地震初步判定本次地震事件属于较大地震灾害，建议启动%s级地震应急响应。",
                            args.getEarthQuakeEmergencyLevel()),
                    String.format("目前灾区灾情信息未知，据初步估计本次地震极震区为%1$s度。" +
                                    "建议由市抗震救灾指挥部提出%1$s级应急响应建议，经市委、市政府同意，由市政府宣布启动地震%1$s级应急响应。"
                                    + "在国务院、省抗震救灾指挥部的统一领导、指挥和协调下，市抗震救灾指挥部负责组织实施全市抗震救灾工作。",
                            args.getEarthQuakeEmergencyLevel()),
                    "先期处置：",
                    "（1）市防震减灾服务中心迅速对地震影响进行评估，通报有关领导和部门,并立即向市应急管理局报告震情、预估烈度、灾情初步判断意见，提出抗震救灾工作建议。",
                    "（2）市经济和信息化局、市教育局、市公安局、市自然资源和规划局、市生态环境局、市住房和城乡建设局、市交通运输局、市水利局、市卫生健康委、市通信发展办、国网雅电集团等按照职责及时收集灾情信息通报市应急管理局。",
                    "（3）市应急管理局迅速汇总灾情，组织开展灾情评估，并按规定向市委、市政府、市应急委、应急厅报告，提出应急响应建议。集结抢险救援队伍，调集本地救灾物资，组织支援力量进入备战状态。",
                    "（4）市抗震救灾指挥部成员单位迅速按照职能分工和应急预案规定进行先期处置，及时向市委、市政府、市应急委、市抗震救灾指挥部报告工作进展，相关单位负责同志立即前往市应急管理局参加紧急会议。",
                    "处置措施建议：",
                    "人员安置方面，应优先选择地势较高、远离河道且具备较好排水条件的安全区域，村内学校、村委会等公共设施也可作为临时安置点，具备一定容纳能力和生活配套条件。对于本地安置条件受限的村组，可组织跨区域转移，安排至周边安全城镇，利用当地酒店、学校等资源保障群众基本生活与应急避险需求。",
                    "救援队伍准备方面：建议提前联系消防、交通和医疗三类专业救援力量：",
                    "消防救援队准备生命探测设备、环境探测设备和破拆工具等，可以及时定位被困人员，评估现场环境。",
                    "交通救援队预置挖掘机、装载机和道路抢修车等，开辟抗震救灾绿色通道。",
                    "医疗救援队组织具备外科、内科、急救能力的医疗人员，配备心肺复苏仪、骨折固定夹板、担架等装备及绷带、消炎药、止痛药等应急药物。",
                    "救援物资准备方面，应根据预测受灾人口数量，提前调配各类救援物资。"
            };
            for (String content : contents) {
                // 添加段落
                DocumentUtils.addRegularContents(paragraph, content);
            }
        }
    }

    /**
     * 一、降雨概况
     *
     * @param doc
     */
    private void createRainstormBasicInfo(XWPFDocument doc, RReportDTO args) {
        // 标题
        DocumentUtils.createTitle(doc, "一、降雨概况", 3);
        // 内容
        XWPFParagraph paragraph = DocumentUtils.addRegularParagraph(doc, null);
        String maxHoursRainfall = "";
        if (Float.parseFloat(args.getRainAreaQuantity().get(0)) > 50) {
            maxHoursRainfall = "单小时最大降雨量超过50毫米，";
        }

        // 拼接基本信息
        String content = String.format("北京时间%s，%s12小时累积降雨量达到%s，%s" +
                        "根据最新实时气象监测数据，降雨主要集中在%s一带。",
                args.getRainTime(),
                DocumentUtils.list2Str(args.getRainAreaPosition(), null),
                DocumentUtils.list2Str(args.getRainAreaQuantity(), "毫米"),
                maxHoursRainfall,
                args.getConcentratedAreaPosition(),
                DocumentUtils.list2Str(args.getConcentratedAreaDetailStreet(), null),
                DocumentUtils.list2Str(args.getConcentratedAreaDetailQuantity(), null),
                DocumentUtils.list2Str(args.getConcentratedAreaDetailGrade(), null));
        // 添加普通段落
        DocumentUtils.addRegularContents(paragraph, content);
        // 一个空行
        DocumentUtils.createBlankLine(doc, 1);
    }

    /**
     * 二、风险评估
     *
     * @param doc
     */
    private void createRainstormAffected(XWPFDocument doc, RReportDTO args, String queueId) {

        DocumentUtils.createTitle(doc, "二、风险评估", 3);
        // 影响内容
        // 一段
        XWPFParagraph paragraph1 = DocumentUtils.addRegularParagraph(doc, null);

        String content1 = String.format("受持续强降雨影响，基于线性回归和贝叶斯模型构建的灾害风险评估模型在%s%d个地质灾害风险区、%s%d个地质灾害在测隐患点的范围内，结合了%s和近年来历史灾害数据共11类致灾因子的632条数据进行评估，" +
                        "本次暴雨预计可能形成%s复合灾害链，并评估得到%s%s的地质灾害风险显著上升，需高度警惕其中%d个地质灾害在测隐患点发生山洪、泥石流等次生灾害发生的可能性。",
                args.getConcentratedAreaPosition(),
                args.getRiskAreaQuantity(),
                DocumentUtils.list2Str(args.getHideArea(), null),
                args.getHideAreaQuantity(),
                DocumentUtils.list2Str(args.getHazards(), null),
                args.getDisasterChain(),
                args.getSignificantIncreaseArea(),
                DocumentUtils.list2Str(args.getSignificantIncreaseAreaStreet(), null),
                args.getSignificantIncreaseAreaHideQuantity()
        );

        // 添加段落
        DocumentUtils.addRegularContents(paragraph1, content1);
        // 换行
        DocumentUtils.createBlankLine(doc, 1);

        // 第二段，滑坡、泥石流、山洪、内涝分别处理
        for (int i = 0; i < args.getSecondaryDisasterReport().size(); i++) {
            SecondaryDisasterVO disasterReport = args.getSecondaryDisasterReport().get(i);

            // 如果List长度为0，就不显示
            if (disasterReport.getDisasterTableData().size() != 0) {
                XWPFParagraph paragraph2 = DocumentUtils.addRegularParagraph(doc, null);
                StringBuilder contentBuilder = new StringBuilder();
                if (i == 0) {
                    contentBuilder.append("测算结果根据降雨集中区域结合地形地势因素分析得出，");
                }

                // 灾害类型
                contentBuilder.append(
                        String.format("%s风险主要集中在%s附近区域，" +
                                        "为本轮强降雨期间%s风险最高区域，需要提前疏散居民，严加防范。",
                                disasterReport.getDisasterType().getDisasterName(),
                                disasterReport.getRiskStreet(),
                                disasterReport.getDisasterType().getDisasterName()
                        )
                );
                DocumentUtils.addRegularRun(paragraph2, contentBuilder.toString());

                // 设置表格标题
                XWPFParagraph tableParagraph = DocumentUtils.addRegularParagraph(doc, null);
                XWPFRun tableRun = DocumentUtils.addRegularRun(tableParagraph,
                        String.format("%s灾害预测概率统计表", disasterReport.getDisasterType().getDisasterName()));
                tableParagraph.setAlignment(ParagraphAlignment.CENTER);
                tableRun.setFontFamily(DocumentConfig.FONT_FANG_SONG_GB2312);
                tableRun.setFontSize(DocumentConfig.FONT_SIZE_FOUR);

                // 生成表格
                List<String> headers = DocumentUtils.stringArray2List(
                        "位置",
                        disasterReport.getDisasterType().getDisasterName() + "发生概率",
                        "灾害等级");
                List<String> fieldNames = DocumentUtils.stringArray2List("position", "probability", "grade");
                DocumentUtils.createGenericTable(doc,
                        disasterReport.getDisasterTableData(),
                        headers,
                        fieldNames,
                        true);
                // 换行
                DocumentUtils.createBlankLine(doc, 1);

                String imageName;
                if (disasterReport.getDisasterType().getDisasterName().equals("滑坡")) {
                    imageName = "暴雨滑坡潜在隐患点及人口分布图";
                } else if (disasterReport.getDisasterType().getDisasterName().equals("泥石流")) {
                    imageName = "暴雨泥石流潜在隐患点及人口分布图";
                } else if (disasterReport.getDisasterType().getDisasterName().equals("内涝")) {
                    imageName = "暴雨内涝潜在隐患点及人口分布图";
                } else {
                    imageName = "暴雨山洪潜在隐患点及人口分布图";
                }
                log.info("等待插入{}！", imageName);
                // 震中附近活动断裂分布图
                DocumentUtils.insertImageWithCaption(doc, getThematic(queueId, imageName),
                        ImageTypeEnum.JPEG, null, null, "",
                        ImagePositionEnum.AFTER);
                // 图片注释
                DocumentUtils.createTitle(doc, imageName, 5);
                // 换行
                DocumentUtils.createBlankLine(doc, 1);
                log.info("已插入{}！", imageName);


                Long originalData = disasterReport.getInfluencePeopleQuantity();
                Long[] bounds = calculateFloatBounds(originalData);
                Long data1 = bounds[0]; // 较小值
                Long data2 = bounds[1]; // 较大值

                // 第三段
                String text = String.format(
                        "其中，%s可能的大型灾害，预计影响%d到%d人，附近居民和风险影响区域居民必须撤离。",
                        disasterReport.getDisasterType().getDisasterName(),
                        data1.intValue(),
                        data2.intValue()
                );
                XWPFParagraph paragraph3 = DocumentUtils.addRegularParagraph(doc, text);
                paragraph3.setIndentationFirstLine(0);      // 首行不缩进
            }
            if ((i + 1) == args.getSecondaryDisasterReport().size()) {
                // 换行
                DocumentUtils.createBlankLine(doc, 1);
                log.info("等待插入暴雨避难场所分布图！");
                // 震中附近活动断裂分布图
                DocumentUtils.insertImageWithCaption(doc, getThematic(queueId, "暴雨避难场所分布图"),
                        ImageTypeEnum.JPEG, null, null, "",
                        ImagePositionEnum.AFTER);
                // 图片注释
                DocumentUtils.createTitle(doc, "暴雨避难场所分布图", 5);
                // 换行
                DocumentUtils.createBlankLine(doc, 1);
                log.info("已插入暴雨滑坡潜在隐患点及人口分布图！");
            }
        }
        DocumentUtils.createBlankLine(doc, 1);
    }

    /**
     * 三、应急处置建议
     *
     * @param doc
     */
    private void createCopeRainstorm(XWPFDocument doc, RReportDTO args) {

        // 标题
        DocumentUtils.createTitle(doc, "三、应急处置建议", 3);
        // 内容
        XWPFParagraph paragraph = DocumentUtils.addRegularParagraph(doc, null);
// 段落内容数组
        String[] contents = {
                "防范总建议：",
                String.format("1. %s政府要做好地质灾害防范工作安排部署，组织镇街、村组开展地质灾害隐患点、风险区巡查排查监测，必要时组织受威胁群众转移避险。",
                        DocumentUtils.list2Str(args.getWorkScheduleArea(), null)),
                "2. 资源规划部门要加强与相关部门沟通衔接，及时叫应预警区内地质灾害隐患点、风险区监测员和巡查员，督促指导有关单位做好重点区域巡查排查技术指导。",
                "3.水务、交通、旅游等部门开展预警区内水库、公路、景区等重要基础设施周边地质灾害风险隐患巡查监测。",
                "4.应急管理部门做好可能发生的地质灾害应急准备工作。",
                "处置措施建议：",
                String.format("人员疏散方面，建议优先组织%s中紧邻河道的民房、农家乐等高风险区域居民转移，此类区域靠近水体，受山洪和泥石流突发影响最为显著。" +
                                "同时，应重点关注%s等城市内涝易发的低洼积水区域，确保上述重点区域人员能够及时、安全撤离，最大限度保障群众生命安全。",
                        DocumentUtils.list2Str(args.getEvacuateTheCrowdArea(), null),
                        DocumentUtils.list2Str(args.getFocusArea(), null)),
                "人员安置方面，应优先选择地势较高、远离河道且具备较好排水条件的安全区域，村内学校、村委会等公共设施也可作为临时安置点，具备一定容纳能力和生活配套条件。对于本地安置条件受限的村组，可组织跨区域转移，安排至周边安全城镇，利用当地酒店、学校等资源保障群众基本生活与应急避险需求。",
                "救援队伍准备方面：建议提前联系消防、交通和医疗三类专业救援力量：",
                "消防救援队准备生命探测设备、环境探测设备和破拆工具等，可以及时定位被困人员，评估现场环境。",
                "交通救援队预置挖掘机、装载机和道路抢修车等，开辟抗震救灾绿色通道。",
                "医疗救援队组织具备外科、内科、急救能力的医疗人员，配备心肺复苏仪、骨折固定夹板、担架等装备及绷带、消炎药、止痛药等应急药物。",
                "救援物资准备方面，应根据预测受灾人口数量，提前调配各类救援物资。"
        };
        // 添加普通段落
        for (String content : contents) {
            DocumentUtils.addRegularContents(paragraph, content);
        }
    }

    // 获取专题图
    private String getThematic(String queueId, String fileName) {
        // 筛选条件
        QueryWrapper<DZProduct> wrapper = new QueryWrapper<>();
        wrapper.eq("eqqueue_id", queueId);
        wrapper.like("code", "A4");    //  只筛选简图
        String sourceFile = null;
        while (true) {
            List<DZProduct> dzProducts = dzProductMapper.selectList(wrapper);
            // 列表为空，继续查询
            if (dzProducts.isEmpty()) {
                continue;
            }
            boolean found = false; // 标记是否找到目标
            for (DZProduct product : dzProducts) {
                if (product.getFileName().equals(fileName)) {
                    sourceFile = product.getSourceFile();
                    found = true;
                    break;
                }
            }
            if (found) {
                break;
            }
        }

        // 返回图片路径
        return sourceFile;
    }

    // 计算浮动范围的上下边界（带截断处理）
    private Long[] calculateFloatBounds(Long originalValue) {
        if (originalValue == null) {
            return new Long[]{0L, 0L};
        }

        int floatRange = calculateFloatRange(originalValue);

        Long lowerBound = Math.max(0L, originalValue - floatRange);
        Long upperBound = originalValue + floatRange;

        // 对边界值进行截断处理
        lowerBound = truncateByDigits(lowerBound);
        upperBound = truncateByDigits(upperBound);

        return new Long[]{lowerBound, upperBound};
    }

    // 根据数值位数计算浮动范围
    private int calculateFloatRange(Long value) {
        if (value == null || value == 0) {
            return 10; // 默认浮动范围
        }

        // 计算数值的位数
        int digits = String.valueOf(Math.abs(value)).length();

        // 根据位数计算浮动范围
        if (digits <= 2) {
            return 10;
        } else if (digits <= 7) {
            return (int) Math.pow(10, digits - 1);
        } else {
            return 1000000; // 7位数的浮动范围
        }
    }

    //根据位数截断数值
    private Long truncateByDigits(Long value) {
        if (value == null || value == 0) {
            return value;
        }

        // 计算数值的位数
        int digits = String.valueOf(Math.abs(value)).length();

        if (digits == 1) {
            // 个位数不处理
            return value;
        } else if (digits == 2 || digits == 3) {
            // 2位数和3位数：除了最高位其他为0
            long divisor = (long) Math.pow(10, digits - 1);
            long highestDigit = value / divisor;
            return highestDigit * divisor;
        } else if (digits == 4) {
            // 4位数：最后两位为0
            return (value / 100) * 100;
        } else if (digits == 5) {
            // 5位数：最后三位为0
            return (value / 1000) * 1000;
        } else if (digits == 6) {
            // 6位数：最后四位为0
            return (value / 10000) * 10000;
        } else if (digits == 7) {
            // 7位数：最后四位为0
            return (value / 10000) * 10000;
        } else {
            // 超过7位数，按7位数处理
            return (value / 10000) * 10000;
        }
    }

    // 是否添加应急处置意见
    private boolean addEmergencyResponseSuggestions(RReportDTO args) {
        for (int i = 0; i < args.getSecondaryDisasterReport().size(); i++) {
            if (args.getSecondaryDisasterReport().get(i).getDisasterTableData().size() != 0)
                return true;
        }
        return false;
    }

    // 设置保存参数
    private void settingArgs(String type, String queueId) {
        DocumentArgs arg = new DocumentArgs();
        arg.setQueueId(queueId);
        if (type.equals(BaseConstants.EQ_DISASTER_DOCUMENT)) {
            arg.setName("地震应急预评估报告");
            arg.setDisaster(BaseConstants.EQ_DISASTER_DOCUMENT);
            arg.setOutFile(BaseConstants.EQ_DOCUMENT_OUTPUT_PATH);
        } else {
            arg.setName("暴雨应急预评估报告");
            arg.setDisaster(BaseConstants.RAIN_DISASTER_DOCUMENT);
            arg.setOutFile(BaseConstants.RAIN_DOCUMENT_OUTPUT_PATH);
        }
        // 推送消息
        rabbitTemplate.convertAndSend(BaseConstants.ASSESS_EXCHANGE, BaseConstants.DOCUMENTS_QUEUE, arg);
    }

}
