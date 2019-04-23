package com.github.flow.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;

public class ManaVO {
    @Data
    @Accessors(chain = true)
    @ApiModel("ManaVO.DeployProcessByZipRes")
    public static class DeployProcessByZipRes {
        private String id;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("ManaVO.SearchProcessDeployReq")
    public static class SearchDeployReq {
        @ApiModelProperty("部署key")
        private String key;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("ManaVO.SearchProcessDeployRes")
    public static class SearchProcessDeployRes {
        private List deploymentList;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("ManaVO.SearchProcessDefinitionReq")
    public static class SearchProcessDefinitionReq {
        @ApiModelProperty("流程定义key")
        private String key;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("ManaVO.SearchProcessDefinitionRes")
    public static class SearchProcessDefinitionRes {
        @ApiModelProperty("流程定义key")
        private List processDefinitionList;
    }


    @Data
    @Accessors(chain = true)
    @ApiModel("ManaVO.DeleteProcessDefinitionReq")
    public static class DeleteProcessDefinitionReq {
        @NotEmpty
        @ApiModelProperty("流程定义id")
        private String id;
        @ApiModelProperty("强制删除？包括所有执行中的相关流程实例、流程定义")
        private Boolean isForceDelete = false;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("ManaVO.ImgByProcessIdReq")
    public static class ImgByProcessIdReq {
        @NotEmpty
        private String id;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("ManaVO.SearchNewestProcessDefinitionRes")
    public static class SearchNewestProcessDefinitionRes {
        private List processDefList;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("ManaVO.DeleteProcessDefinitionByKeyReq")
    public static class DeleteProcessDefinitionByKeyReq {
        @NotEmpty
        @ApiModelProperty("流程定义key")
        private String key;
        @ApiModelProperty("强制删除？包括所有执行中的相关流程实例、流程定义")
        private Boolean isForceDelete = false;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("ManaVO.SuspendProcessDefinitionByIdReq")
    public static class OperateProcessDefinitionByIdReq {
        @Pattern(regexp = "suspend|activate")
        @ApiModelProperty("挂起/激活。值可为:suspend,activate")
        private String operation;
        @NotEmpty
        @ApiModelProperty("流程定义id")
        private String id;
        @ApiModelProperty("是否将相关的实例也进行此操作。默认false")
        private Boolean isOperateRunningInstance = false;
        @ApiModelProperty("生效日期。默认null，立即生效")
        private Date operateDate = null;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("ManaVO.SearchTimerJobRes")
    public static class SearchTimerJobRes {
        private List jobList;
    }

}
