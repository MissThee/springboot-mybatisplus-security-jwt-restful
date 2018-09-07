package server.db.primary.model.reportform;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "REPORT_DATA_OIL_WELL")
public class ReportDataOilWell_Res {
    private Long id;
    //    private String loopName;//name  掺水阀组
//    private String reportWellName;//well_name  井号
//    private Integer dumpDiameter;//pump_dia  泵径
//    private Double dumpLength;//pump_len  泵深
//    private Double wellStroke;//well_stroke  冲程
//    private Double jigFrequency;//jig_frequency  冲次
//    private String strokeFrequency;//stroke_frequency 上报冲程/冲次
//    private Double prodLiquidW;//prod_liquid  日产液（吨）
//    private Double prodWaterCut;//prod_water_cut  含水
//    private String eventWellSwitch;//event_well_switch  开关井事件
//    private String remark;//remark 备注信息
    @Column(name = "LOOP_NAME")
    private String name;//  掺水阀组
    @Column(name = "REPORT_WELL_NUM")
    private Long ring_seq;//   环序号
    @Column(name = "REPORT_WELL_NAME")
    private String well_name;//well_name  井号
    @Column(name = "DUMP_DIAMETER")
    private Double pump_dia;//pump_dia  泵径 Integer
    @Column(name = "DUMP_LENGTH")
    private Double pump_len;//pump_len  泵深 Double
    @Column(name = "WELL_STROKE")
    private Double well_stroke;//well_stroke  冲程 Double
    @Column(name = "JIG_FREQUENCY")
    private Double jig_frequency;//jig_frequency  冲次 Double
    @Column(name = "STROKE_FREQUENCY")
    private String stroke_frequency;//stroke_frequency 上报冲程/冲次 String
    @Column(name = "PROD_LIQUID_W")
    private Double prod_liquid;//prod_liquid  日产液（吨） Double
    @Column(name = "PROD_WATER_CUT")
    private Double prod_water_cut;//prod_water_cut  含水(%) Double
    @Column(name = "I_UPPER")
    private Double i_upper;//i_upper  上行电流(A) Double
    @Column(name = "I_LOWER")
    private Double i_lower;//i_lower  下行电流(A) Double
    @Column(name = "EVENT_WELL_SWITCH")
    private String event_well_switch;//event_well_switch  开关井事件
    @Column(name = "REMARK")
    private String remark;//remark 备注信息

    private String reportDate;
    private Long reportStationId;

    private Long areaId;
//
//    private Long reportWellId;
//
//    private Double reportWellNum;
//
//    private Long wellId;
//
//    private String wellName;
//
//    private Long loopId;
//
//
//    private String reportStationName;
//
//    private String areaName;
//
//    private Double prodLiquidV;
//
//    private Double prodOilV;
//
//    private Double prodOilW;
//
//    private Double mark;
//
//    private Date stime;

}