package server.db.primary.model.reportform;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

import java.util.Date;
@Data
@Table(name = "REPORT_DATA_WATER_WELL")
public class ReportDataWaterWell_Res {
    private Long id;

    @Column(name = "REPORT_STATION_NAME")
    private String station_name;//station_name  配水站
    @Column(name = "REPORT_WATER_WELL_NAME")
    private String water_well_name;//water_well_name  井号
    @Column(name = "INJECTION_INFO")
    private String injection;//injection  配注
    @Column(name = "PRESS_TRUNK")
    private Double press_trunk;//press_trunk 干压（MPa）
    @Column(name = "PRESS_OIL")
    private Double press_oil;//press_oil  油压（MPa）
    @Column(name = "FLOW_START")
    private Double flow_yes;//flow_yes  昨日累计流量（m³）
    @Column(name = "FLOW_END")
    private Double flow_today;//flow_today  累计流量（m³）
    @Column(name = "FLOW_WATER_DAY")
    private Double flow_water_day;//flow_water_day  日注水量
    @Column(name = "EVENT_WELL_SWITCH")
    private String event_well_switch;//event_well_switch  开关井事件
    @Column(name = "REMARK")
    private String remark;//remark  备注信息

    private Long areaId;
    private Long reportWaterWellId;
    private String reportDate;

    private Long reportWaterWellNum;

    private Long waterWellId;

    private String waterWellName;

    private Long reportStationId;

    private String areaName;

    private Double flowWaterConf;

    private Double flowConfDay;

    private Double flowDataStart;

    private Date flowDataStartTime;

    private Double flowDataEnd;

    private Date flowDataEndTime;

    private Double mark;

    private Date stime;


}