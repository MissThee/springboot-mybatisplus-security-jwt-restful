package server.db.primary.model.sysoption;

import lombok.Data;

import java.util.Date;
@Data
public class WellStateMan {
    private Long id;

    private Long wellId;

    private String wellName;

    private Long areaId;

    private String areaName;

    private Long stationId;

    private String stationName;

    private Integer stateTypeId;

    private String stateType;

    private String opLoginname;

    private String opName;

    private String opIpaddr;

    private String opLoginname0;

    private String opName0;

    private String opIpaddr0;

    private String remark;

    private Short mark;

    private Date stime;

    private Date etime;


}