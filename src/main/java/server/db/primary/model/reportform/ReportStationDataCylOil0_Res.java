package server.db.primary.model.reportform;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

import java.util.Date;
@Data
@Table(name = "REPORT_STATION_DATA_CYL_OIL0")
public class ReportStationDataCylOil0_Res {
    private Long id;

    @Column(name = "REPORT_HOUR")
    private String report_hour;//report_hour
    @Column(name = "VAL_OIL")
    private Double val_oil;//val_oil
    @Column(name = "VAL_GAS")
    private Double val_gas;//val_gas
    @Column(name = "REMARK")
    private String remark;//remark

    private String reportDate;

    private Double valWater;

    private Short mark;

    private Date stime;

}
