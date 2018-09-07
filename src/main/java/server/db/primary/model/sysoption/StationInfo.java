package server.db.primary.model.sysoption;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class StationInfo {
    private Long id;

    private String stationName;

    private String stationNameAlias;

    private Long coId;

    private String coName;

    private Long areaId;

    private String areaName;

    private Long stationType;

    private String stationTypeName;

    private Double stationLong;

    private Double stationLat;

    private Short autoMark;

    private String plcIpaddr;

    private Long vMark;

    private Long pointCount;

    private String opcTopic;

    private Long stationNum;

    private String a11Code;

    private String a11CodeFather;

    private String remark;

    private Long mark;

    private Date stime;

    private AreaInfo areaInfo;

    private List<StationInfoVideo> stationInfoVideo;

}