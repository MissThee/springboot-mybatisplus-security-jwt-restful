package server.db.primary.model.reportform;

import lombok.Data;


import java.util.Date;
@Data
public class ReportDataOwaterLoop {
    private Long id;

    private String reportDate;

    private String reportHour;

    private Long reportLoopId;

    private String reportLoopName;

    private Long reportStationId;

    private String reportStationName;

    private Long areaId;

    private String areaName;

    private Double tempOut;

    private Double pressOut;

    private Double flowInstOut;

    private Double flowTotleOut;

    private Double waterVal;

    private Double tempIn;

    private Double pressIn;

    private Double flowInstIn;

    private Double flowTotleIn;

    private Double liquidVal;

    private String remark;

    private Short mark;

    private Date stime;

    private Double testaaa;


}