package server.db.primary.model.reportform;

import lombok.Data;


import java.util.Date;

@Data
public class ReportStationDataCylOil1 {
    private Long id;

    private String reportDate;

    private String reportHour;

    private Double valFwater;

    private Double valTwater;

    private Double valInoil;

    private String valWsoil;

    private String valMoil;

    private String valHs;

    private String valCoil;

    private String remark;

    private Short mark;

    private Date stime;


}