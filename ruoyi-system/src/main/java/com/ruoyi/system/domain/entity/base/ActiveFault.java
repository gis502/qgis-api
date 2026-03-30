package com.ruoyi.system.domain.entity.base;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.locationtech.jts.geom.Geometry;

/**
 * @ClassName ActiveFault
 * @Description 陕西省活动断层实体
 * @Author Huang Yx
 * @Date 2026/1/30 0:27
 */
@Data
@TableName("base.active_fault")
public class ActiveFault {

    @TableId("\"ID\"")
    private String ID;

    @TableField("\"SmUserID\"")
    private Integer SmUserID;

    @TableField("\"Scale\"")
    private Integer Scale;

    @TableField("\"FractureZo\"")
    private String FractureZo;

    @TableField("\"Name\"")
    private String Name;

    @TableField("\"FaultSegme\"")
    private String FaultSegme;

    @TableField("\"StrikeDire\"")
    private Integer StrikeDire;

    @TableField("\"Strike\"")
    private Integer Strike;

    @TableField("\"Direction\"")
    private Integer Direction;

    @TableField("\"Clination\"")
    private Integer Clination;

    @TableField("\"Length\"")
    private Double Length;

    @TableField("\"TopDepth\"")
    private String TopDepth;

    @TableField("\"Width\"")
    private Double Width;

    @TableField("\"FractureBe\"")
    private Double FractureBe;

    @TableField("\"Feature\"")
    private Integer Feature;

    @TableField("\"LatestActi\"")
    private Integer LatestActi;

    @TableField("\"StartTimeE\"")
    private String StartTimeE;

    @TableField("\"VDisplaceE\"")
    private Double VDisplaceE;

    @TableField("\"VDisplac_1\"")
    private Double VDisplac_1;

    @TableField("\"HDisplaceE\"")
    private Double HDisplaceE;

    @TableField("\"HDisplac_1\"")
    private Double HDisplac_1;

    @TableField("\"TDisplaceE\"")
    private Double TDisplaceE;

    @TableField("\"TDisplac_1\"")
    private Double TDisplac_1;

    @TableField("\"AveVRate\"")
    private Double AveVRate;

    @TableField("\"AveVRateEr\"")
    private Double AveVRateEr;

    @TableField("\"AveHRate\"")
    private Double AveHRate;

    @TableField("\"AveHRateEr\"")
    private Double AveHRateEr;

    @TableField("\"StartTimeN\"")
    private Double StartTimeN;

    @TableField("\"NewVRate\"")
    private Double NewVRate;

    @TableField("\"NewVRateEr\"")
    private Double NewVRateEr;

    @TableField("\"NewHRate\"")
    private Double NewHRate;

    @TableField("\"NewHRateEr\"")
    private Double NewHRateEr;

    @TableField("\"MaxVRate\"")
    private Double MaxVRate;

    @TableField("\"MaxVRateEr\"")
    private Double MaxVRateEr;

    @TableField("\"MaxHRate\"")
    private Double MaxHRate;

    @TableField("\"MaxHRateEr\"")
    private Double MaxHRateEr;

    @TableField("\"EQEventCou\"")
    private Integer EQEventCou;

    @TableField("\"EQEventRIB\"")
    private Integer EQEventRIB;

    @TableField("\"EQEventRIT\"")
    private Integer EQEventRIT;

    @TableField("\"Method\"")
    private String Method;

    @TableField("\"MaxRupture\"")
    private Integer MaxRupture;

    @TableField("\"AverageRup\"")
    private Integer AverageRup;

    @TableField("\"ElapseTime\"")
    private Integer ElapseTime;

    @TableField("\"SlipDepthE\"")
    private Integer SlipDepthE;

    @TableField("\"SlipDept_1\"")
    private Integer SlipDept_1;

    @TableField("\"AverageSli\"")
    private Double AverageSli;

    @TableField("\"AverageS_1\"")
    private Double AverageS_1;

    @TableField("\"CreepRateE\"")
    private Double CreepRateE;

    @TableField("\"CreepRat_1\"")
    private Double CreepRat_1;

    @TableField("\"CoSeismicM\"")
    private Double CoSeismicM;

    @TableField("\"CoSeismi_1\"")
    private Double CoSeismi_1;

    @TableField("\"CoSeismicA\"")
    private Double CoSeismicA;

    @TableField("\"CoSeismi_2\"")
    private Double CoSeismi_2;

    @TableField("\"LatestCoSe\"")
    private Double LatestCoSe;

    @TableField("\"LatestCo_1\"")
    private Double LatestCo_1;

    @TableField("\"CommentInf\"")
    private String CommentInf;

    @TableField("\"Shape_Leng\"")
    private Double Shape_Leng;

    @TableField("\"Project\"")
    private String Project;

    @TableField("\"Coordinate\"")
    private String Coordinate;

    @TableField("\"DateSource\"")
    private String DateSource;

    @TableField("\"Geometry\"")
    @JsonSerialize(using = ToStringSerializer.class)
    private Geometry geometry;


}
