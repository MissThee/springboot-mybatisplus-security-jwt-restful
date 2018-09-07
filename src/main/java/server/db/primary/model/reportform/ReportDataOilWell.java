package server.db.primary.model.reportform;

import lombok.Data;


import java.util.Date;

@Data
public class ReportDataOilWell {
    private Long id;

    private String reportDate;

    private Long reportWellId;

    private String reportWellName;

    private Double reportWellNum;

    private Long wellId;

    private String wellName;

    private Long loopId;

    private String loopName;

    private Long reportStationId;

    private String reportStationName;

    private Long areaId;

    private String areaName;

    private Integer dumpDiameter;

    private Double dumpLength;

    private Double wellStroke;

    private Double jigFrequency;

    private Double prodLiquidV;

    private Double prodLiquidW;

    private Double prodOilV;

    private Double prodOilW;

    private Double prodWaterCut;

    private Double iUpper;

    private Double iLower;

    private String eventWellSwitch;

    private String remark;

    private Double mark;

    private Date stime;

    private String strokeFrequency;
}