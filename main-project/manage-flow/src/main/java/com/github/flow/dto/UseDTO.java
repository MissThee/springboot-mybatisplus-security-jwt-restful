package com.github.flow.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

public class UseDTO {
    @Data
    public static class MyTask {
        @ApiModelProperty("任务ID")
        private String Id;
        @ApiModelProperty("任务名称")
        private String Name;
        @ApiModelProperty("任务创建时间")
        private Date CreateTime;
        @ApiModelProperty("任务办理人")
        private String Assignee;
        @ApiModelProperty("执行实例ID")
        private String ExecutionId;
        @ApiModelProperty("流程实例ID")
        private String ProcessInstanceId;
        @ApiModelProperty("流程定义ID")
        private String ProcessDefinitionId;
    }

    @Data
    public static class MyIdentityLink {
        @ApiModelProperty("人员ID")
        private String UserId;
        @ApiModelProperty("任务ID")
        private String TaskId;
        @ApiModelProperty("执行实例对应流程实例id")
        private String ProcessInst;
        @ApiModelProperty("执行实例活动id")
        private String Type;
    }
    @Data
    public static class MyProcessInstance {
        @ApiModelProperty("流程实例ID")
        private String getId;
        @ApiModelProperty("流程实例名称")
        private String getName;
        @ApiModelProperty("流程实例对应业务id")
        private String getBusinessKey;
        @ApiModelProperty("流程实例开始时间")
        private String getStartTime;
    }
}
