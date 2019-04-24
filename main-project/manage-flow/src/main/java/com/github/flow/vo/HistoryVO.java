package com.github.flow.vo;

import com.github.flow.dto.HistoricProcessInstanceDTO;
import com.github.flow.dto.HistoricVariableInstanceDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

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
        @NotEmpty
        private int pageIndex;
        @NotEmpty
        private int pageSize;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("HistoryVO.searchHistoryTaskRes")
    public static class SearchHistoryTaskRes {
        private long total;
        private List hisTaskList;
    }


    @Data
    @Accessors(chain = true)
    @ApiModel("HistoryVO.GetHistoryVariableReq")
    public static class GetHistoryVariableReq {
        private String taskId;
        private String executionId;
        private String processInstanceId;
        @NotEmpty
        private int pageIndex;
        @NotEmpty
        private int pageSize;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("HistoryVO.GetHistoryVariableRes")
    public static class GetHistoryVariableRes {
        private long total;
        private List<HistoricVariableInstanceDTO> hisVariableList;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("HistoryVO.SearchHistoryProcessReq")
    public static class SearchHistoryProcessReq {
        private String involvedUser;
        @NotEmpty
        private int pageIndex;
        @NotEmpty
        private int pageSize;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("HistoryVO.SearchHistoryProcessRes")
    public static class SearchHistoryProcessRes {
        private long total;
        private List<HistoricProcessInstanceDTO> hisTaskList;
    }

}
