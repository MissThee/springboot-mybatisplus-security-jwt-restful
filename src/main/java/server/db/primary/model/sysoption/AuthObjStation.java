package server.db.primary.model.sysoption;

import lombok.Data;

import java.util.Date;
@Data
public class AuthObjStation {
    private Long id;

    private Long objId;

    private String objNo;

    private String objName;

    private Long stationId;

    private String stationName;

    private Short stationType;

    private String stationTypeInfo;

    private Long mark;

    private Date stime;

    private Date rtime;


}