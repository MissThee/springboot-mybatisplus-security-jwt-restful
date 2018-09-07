package server.db.primary.model.reportform;

import lombok.Data;

import java.util.Date;
@Data
public class ReportConfStation {
    private Long id;

    private String reportStationName;

    private Long areaId;

    private String areaName;

    private String remark;

    private Short mark;

    private Date stime;
}