package server.db.primary.model.sheet;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;
@Data
@Table(name = "REPORT_STATION_DATA_CEZ0")
public class ReportStationDataCez0_Res {
    private Long id;

    private String reportDate;
    @Column(name = "REPORT_HOUR")
    private String report_hour;

    private Short wsl;

    private Short rql;

    private Short xyl;

    private Short jyl;

    private String bz;

    private Short mark;

    private Date stime;


}