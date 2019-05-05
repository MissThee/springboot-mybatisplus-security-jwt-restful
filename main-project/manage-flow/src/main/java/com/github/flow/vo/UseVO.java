package com.github.flow.vo;

import com.github.flow.dto.IdentityLinkDTO;
import com.github.flow.dto.ProcessInstanceDTO;
import com.github.flow.dto.TaskDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UseVO {
    @Data
    @Accessors(chain = true)
    @ApiModel("UseVO.SearchTaskReq")
    public static class SearchTaskReq {
        private String assignee;
        private String candidateUser;
        private List<String> candidateGroup;
        private Boolean isOnlyUnassigned = false;
        @NotNull
        @ApiModelProperty("页序号。0开始")
        private Integer pageIndex;
        @NotNull
        @ApiModelProperty("每页个数。")
        private Integer pageSize;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("UseVO.SearchTaskRes")
    public static class SearchTaskRes {
        private Long total;
        private Collection<TaskDTO> taskList;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("UseVO.ProcessIsFinishedReq")
    public static class ProcessIsFinishedReq {
        @NotEmpty
        private String processInstanceId;
    }
    @Data
    @Accessors(chain = true)
    @ApiModel("UseVO.ProcessIsFinishedRes")
    public static class ProcessIsFinishedRes {
        @NotEmpty
        private Boolean isFinished;
    }

}
