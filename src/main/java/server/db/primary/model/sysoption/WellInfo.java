package server.db.primary.model.sysoption;

import lombok.Data;

import java.util.Date;


@Data
public class WellInfo {
    private Long id;

    private String wellName;

    private String wellNameAlias;

    private Long areaId;

    private String areaName;

    private Long stationId;

    private String stationName;

    private Double wellLong;

    private Double wellLat;

    private Long wProperty;

    private String wPropertyName;

    private Long wellType;

    private String wellTypeName;

    private Long pumpType;

    private String pumpTypeName;

    private String wellIco;

    private Long commMark;

    private Long rtuType;

    private String rtuIpaddr;

    private Long wellNum;

    private Date investmentDate;

    private String groupName;

    private Long wellStroke;

    private Long vMark;

    private String vIpaddr;

    private Integer elecBaseV;

    private Long autoMark;

    private String remark;

    private Long mark;

    private Date stime;

    private Integer vVirtualPort;

    private Long commType;

    private String commTypeName;

    private Long blockId;

    private String blockName;

    private Long oilStorageType;

    private String oilStorageName;

    private String runState;

    private String a11Code;

    private String a11CodeFather;

    private Long wellAddr;

    private Long elecAddr;

    private Long loadAddr;

}