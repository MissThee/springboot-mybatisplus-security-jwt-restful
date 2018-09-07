package server.db.primary.model.reportform;

import lombok.Data;


import java.util.Date;
@Data
public class ReportDataOwaterWells {
    private Long id;

    private String reportDate;

    private Long reportWellId;

    private String reportWellName;

    private Long reportWellNum;

    private Long wellId;

    private String wellName;

    private Long reportStationId;

    private String reportStationName;

    private Long loopId;

    private String loopName;

    private Long areaId;

    private String areaName;

    private Double pressOil08;

    private Double pressOil10;

    private Double pressOil12;

    private Double pressOil14;

    private Double pressOil16;

    private Double pressOil18;

    private Double pressOil20;

    private Double pressOil22;

    private Double pressOil00;

    private Double pressOil02;

    private Double pressOil04;

    private Double pressOil06;

    private String remark;

    private Short mark;

    private Date stime;

}