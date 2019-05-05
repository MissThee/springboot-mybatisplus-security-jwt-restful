package com.github.flow.vo;

import com.github.flow.dto.HistoricProcessInstanceDTO;
import com.github.flow.dto.HistoricTaskInstanceDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

public class HistoryVO {

    @Data
    @Accessors(chain = true)
    @ApiModel("HistoryVO.searchHistoryTaskReq")
    public static class SearchHistoryTaskReq {
        private String assignee;
        private String processDefinitionId;
        private String processDefinitionKey;
        private String processInstanceId;
        private String processInstanceBusinessKey;
        private Date taskCompletedAfter;
        private Date taskCompletedBefore;
        @NotNull
        @ApiModelProperty("页序号。0开始")
        private Integer pageIndex;
        @NotNull
        @ApiModelProperty("每页个数。")
        private Integer pageSize;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("HistoryVO.searchHistoryTaskRes")
    public static class SearchHistoryTaskRes {
        private Long total;
        private Collection<HistoricTaskInstanceDTO> hisTaskList;
    }


    @Data
    @Accessors(chain = true)
    @ApiModel("HistoryVO.GetHistoryVariableReq")
    public static class GetHistoryVariableReq {
        private String taskId;
        private String executionId;
        private String processInstanceId;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("HistoryVO.GetHistoryVariableRes")
    public static class GetHistoryVariableRes {
        private Map<String,Object> variables;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("HistoryVO.SearchHistoryProcessReq")
    public static class SearchHistoryProcessReq {
        private String involvedUser;
        @NotNull
        @ApiModelProperty("页序号。0开始")
        private Integer pageIndex;
        @NotNull
        @ApiModelProperty("每页个数。")
        private Integer pageSize;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("HistoryVO.SearchHistoryProcessRes")
    public static class SearchHistoryProcessRes {
        private Long total;
        private Collection<HistoricProcessInstanceDTO> hisTaskList;
    }

}
