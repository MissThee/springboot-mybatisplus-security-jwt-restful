package server.db.primary.model.sysoption;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class WellPipeInfo_Group {

    private Long pipeNodeNum;

    private List<WellPipeInfo> wellPipeInfoList;

}