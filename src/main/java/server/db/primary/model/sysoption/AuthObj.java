package server.db.primary.model.sysoption;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AuthObj {
    private Long id;

    private String objNo;

    private String objName;

    private String remark;

    private String logoPng;

    private Short mark;

    private Date stime;

    private Date rtime;

    private List<StationInfo> stationInfoList;

    private List<AreaInfo> areaInfoList;
}