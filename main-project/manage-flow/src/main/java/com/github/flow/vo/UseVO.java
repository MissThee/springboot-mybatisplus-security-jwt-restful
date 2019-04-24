package com.github.flow.vo;

import com.github.flow.dto.IdentityLinkDTO;
import com.github.flow.dto.ProcessInstanceDTO;
import com.github.flow.dto.TaskDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

public class UseVO {
    @Data
    @Accessors(chain = true)
    @ApiModel("UseVO.StartProcessReq")
    public static class StartProcessReq {
        private String processDefKey;
        private String businessKey;
        private Map<String, Object> variableMap;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("UseVO.StartProcessRes")
    public static class StartProcessRes {
        private ProcessInstanceDTO processInstance;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("UseVO.SearchTaskReq")
    public static class SearchTaskReq {
        private String assignee;
        private String candidateUser;
        private List<String> candidateGroup;
        private Boolean isOnlyUnassigned = false;
        @NotEmpty
        private int pageIndex;
        @NotEmpty
        private int pageSize;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("UseVO.SearchTaskRes")
    public static class SearchTaskRes {
        private long total;
        private List<TaskDTO> taskList;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("UseVO.ClaimTaskReq")
    public static class ClaimTaskReq {
        @NotEmpty
        private String taskId;
        @NotEmpty
        private String assignee;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("UseVO.ReturnTaskReq")
    public static class ReturnTaskReq {
        @NotEmpty
        private String taskId;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("UseVO.GetCandidateUserReq")
    public static class GetCandidateUserReq {
        @NotEmpty
        private String taskId;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("UseVO.GetCandidateUserRes")
    public static class GetCandidateUserRes {
        List<IdentityLinkDTO> identityLinkList;
    }


    @Data
    @Accessors(chain = true)
    @ApiModel("UseVO.AddCandidateUserReq")
    public static class AddCandidateUserReq {
        @NotEmpty
        private String taskId;
        @NotEmpty
        private String candidateUser;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("UseVO.DeleteCandidateUserReq")
    public static class DeleteCandidateUserReq {
        @NotEmpty
        private String taskId;
        @NotEmpty
        private String candidateUser;
    }


    @Data
    @Accessors(chain = true)
    @ApiModel("UseVO.CompleteTaskReq")
    public static class CompleteTaskReq {
        @NotEmpty
        private String taskId;
        private String comment;
        Map<String, Object> variableMap;
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

    @Data
    @Accessors(chain = true)
    @ApiModel("UseVO.SetVariableReq")
    public static class SetVariableReq {
        private String taskId;
        private String executionId;
        private String variableName;
        private String variable;
        private Map<String, Object> variableMap;

    }

    @Data
    @Accessors(chain = true)
    @ApiModel("UseVO.GetVariableReq")
    public static class GetVariableReq {
        private String taskId;
        private String executionId;
        private String variableName;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("UseVO.GetVariableRes")
    public static class GetVariableRes {
        private Object variable;
        private Object variables;
    }

//    @Data
//    @Accessors(chain = true)
//    @ApiModel("UseVO.SetVariableReq")
//    public static class SetVariableReq {
//        @NotEmpty
//        private String processInstanceId;
//    }


}
