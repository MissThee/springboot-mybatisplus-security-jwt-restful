package com.github.flow.vo;

import com.github.flow.dto.FormDataDTO;
import com.github.flow.dto.ProcessInstanceDTO;
import com.github.flow.dto.TaskDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;

public class UseWithFormVO {
    @Data
    @Accessors(chain = true)
    @ApiModel("UseWithFormVO.SearchTaskReq")
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
    @ApiModel("UseWithFormVO.SearchTaskRes")
    public static class SearchTaskRes {
        private Long total;
        private Collection<TaskDTO> taskList;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("UseWithFormVO.GetStartFormDataReq")
    public static class GetStartFormDataReq {
        private String processDefinitionId;
        private String processDefinitionKey;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("UseWithFormVO.GetStartFormDataRes")
    public static class GetStartFormDataRes {
        private Map<String, FormDataDTO> formProperty;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel(value = "UseWithFormVO.StartFormReq", description = "重要！！：设置表单内容时，时间参数的格式，须严格给出的格式，否则时间参数值和类型会为空。即使是必须值，也不会因为值为空报错，因为提供了此参数，但值为空")
    public static class StartFormReq {
        private String processDefinitionId;
        private String processDefinitionKey;
        private String businessKey;
        private Map<String, String> variableMap = new HashMap<>();
    }

    @Data
    @Accessors(chain = true)
    @ApiModel(value = "UseWithFormVO.StartFormRes")
    public static class StartFormRes {
        private ProcessInstanceDTO processInstance;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel(value = "UseWithFormVO.GetTaskFormDataReq")
    public static class GetTaskFormDataReq {
        @NotEmpty
        private String taskId;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel(value = "UseWithFormVO.GetTaskFormDataRes")
    public static class GetTaskFormDataRes {
        private Map<String, FormDataDTO> formProperty;
        private Collection<String> nextOutValueList;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel(value = "UseWithFormVO.SubmitTaskFormDataReq")
    public static class SubmitTaskFormDataReq {
        @NotEmpty
        private String taskId;
        private Map<String, String> formVariableMap;
        private Map<String, String> variableMap;
        private String comment;
    }


}
