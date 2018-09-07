package server.db.primary.model.sysoption;

import lombok.Data;

import java.util.Date;

@Data
public class WellDataMaintian {
    private Long id;

    private String dataDate;

    private Long wellId;

    private String wellName;

    private Double pressCase;

    private Double pressPipe;

    private Double pressBack;

    private Double pressHoledown;

    private Double lengthHoledown;

    private Double waterRate;

    private String remark;

    private Long opId;

    private String opName;

    private Short mark;

    private Date stime;

}