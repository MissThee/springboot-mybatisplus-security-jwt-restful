package server.db.primary.model.sysoption;

import lombok.Data;

import java.util.Date;

@Data
public class StationInfoVideo {
    private Long id;

    private Long stationId;

    private String stationName;

    private Long vNum;

    private String vIpaddr;

    private String remark;

    private Long mark;

    private Date stime;

}
