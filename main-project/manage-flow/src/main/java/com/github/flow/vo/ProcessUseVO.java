package com.github.flow.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

public class ProcessUseVO {
    @Data
    @Accessors(chain = true)
    @ApiModel("ProcessUseVO.StartProcessReq")
    public static class StartProcessReq {
        private String processDefKey;
        private String businessKey;
        private Map<String, Object> variableMap;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("ProcessUseVO.SearchTaskReq")
    public static class SearchTaskReq {
        private String assignee;
        private String candidateUser;
        private List<String> candidateGroup;
        private Boolean isOnlyUnassigned = false;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("ProcessUseVO.ClaimTaskReq")
    public static class ClaimTaskReq {
        @NotEmpty
        private String taskId;
        @NotEmpty
        private String assignee;
    }
}
