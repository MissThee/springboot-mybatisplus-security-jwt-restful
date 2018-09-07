package server.db.primary.model.reportform;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;
@Data
@Table(name = "REPORT_STATION_DATA_CYL_OIL")
public class ReportStationDataCylOil_Extra {
    private Long id;

    private String reportDate;

    private String reportHour;
    @Column(name = "TI_202")        private Double  ti_202;
    @Column(name = "TI_203")        private Double  ti_203;
    @Column(name = "TI_204")        private Double  ti_204;
}