package server.db.primary.model.reportform;

import lombok.Data;

import java.util.Date;
@Data
public class ReportDataWaterWell {
    private Long id;

    private String reportDate;

    private Long reportWaterWellId;

    private String reportWaterWellName;

    private Long reportWaterWellNum;

    private Long waterWellId;

    private String waterWellName;

    private Long reportStationId;

    private String reportStationName;

    private Long areaId;

    private String areaName;

    private Double pressTrunk;

    private Double pressOil;

    private Double flowStart;

    private Double flowEnd;

    private Double flowWaterDay;

    private Double flowWaterConf;

    private Double flowConfDay;

    private Double flowDataStart;

    private Date flowDataStartTime;

    private Double flowDataEnd;

    private Date flowDataEndTime;

    private String injectionInfo;

    private String remark;

    private Short mark;

    private Date stime;

    private String eventWellSwitch;


}