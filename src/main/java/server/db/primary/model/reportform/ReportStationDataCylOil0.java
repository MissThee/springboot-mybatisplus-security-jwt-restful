package server.db.primary.model.reportform;

import lombok.Data;


import java.util.Date;

@Data
public class ReportStationDataCylOil0 {
    private Long id;

    private String reportDate;

    private String reportHour;

    private Double valOil;

    private Double valGas;

    private Double valWater;

    private String remark;

    private Short mark;

    private Date stime;


}