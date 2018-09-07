package server.db.primary.model.sysoption;

import lombok.Data;

import java.util.Date;
@Data
public class StationInfoValve {
    private Long id;

    private Long stationId;

    private String stationName;

    private String valveName;

    private Short valveNameNum;

    private String valvePoint;

    private String valveUnit;

    private Long areaId;

    private String areaName;

    private String remark;

    private Short mark;

    private Date stime;
}