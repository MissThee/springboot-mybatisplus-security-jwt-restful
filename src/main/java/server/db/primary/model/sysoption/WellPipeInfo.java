package server.db.primary.model.sysoption;

import lombok.Data;

import java.util.Date;
@Data
public class WellPipeInfo {
    private Long id;

    private String pipeNode;

    private Long pipeNodeNum;

    private Long pipeNodeSort;

    private Double pipeLong;

    private Double pipeLati;

    private String remark;

    private Short mark;

    private Date stime;

}