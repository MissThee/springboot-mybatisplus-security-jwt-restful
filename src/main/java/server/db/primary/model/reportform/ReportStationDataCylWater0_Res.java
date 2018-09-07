package server.db.primary.model.reportform;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "REPORT_STATION_DATA_CYL_WATER0")
public class ReportStationDataCylWater0_Res {
    private Long id;

    private String reportDate;

    @Column(name ="REPORT_HOUR")  private String report_hour;
    @Column(name ="FCX_GH")       private String fcx_gh;
    @Column(name ="FCX_PL")       private String fcx_pl;
    @Column(name ="REPORT_HOUR1") private String report_hour1;
    @Column(name ="SY_CYG")       private String sy_cyg;
    @Column(name ="SY_TJG")       private String sy_tjg;
    @Column(name ="SY_WSC")       private String sy_wsc;
    @Column(name ="bz")           private String bz;

}