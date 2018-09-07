package server.db.primary.model.sysoption;

import lombok.Data;

import java.util.Date;

@Data
public class StationInfoWaterwell {
    private Long id;

    private Long stationId;

    private String stationName;

    private String wellWaterName;

    private Long wellWaterNum;

    private Long areaId;

    private String areaName;

    private String remark;

    private Long mark;

    private Date stime;


}