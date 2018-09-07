package server.db.primary.model.reportform;

import lombok.Data;

import javax.persistence.Table;
import java.util.Date;

@Data
public class ReportStationDataCez0 {
    private Long id;

    private String reportDate;

    private String reportHour;

    private Short wsl;

    private Short rql;

    private Short xyl;

    private Short jyl;

    private String bz;

    private Short mark;

    private Date stime;

}