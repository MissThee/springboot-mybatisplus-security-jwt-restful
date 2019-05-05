package com.github.flow.vo;

import com.github.flow.dto.FlowLinePositionDTO;
import com.github.flow.dto.FlowNodePositionDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.List;

public class ImgVO {
    @Data
    @Accessors(chain = true)
    @ApiModel("ImgVO.ImgReq")
    public static class ImgReq {
        @ApiModelProperty("通过taskId查询图片")
        private String taskId;
        @ApiModelProperty("通过processInstanceId查询图片")
        private String processInstanceId;
        @ApiModelProperty("通过processDefinitionId查询图片")
        private String processDefinitionId;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("ImgVO.ImgWithHighLightReq")
    public static class ImgWithHighLightReq {
        @ApiModelProperty("是否仅高亮进度中最后的节点。默认false，查询进度所有节点")
        private Boolean isOnlyLast = false;
        @ApiModelProperty("是否高亮进度中的线。默认true，查询结果高亮线的信息")
        private Boolean isContainHighLightLine = true;
        @ApiModelProperty("通过taskId查询图片")
        private String taskId;
        @ApiModelProperty("通过processInstanceId查询图片")
        private String processInstanceId;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("ImgVO.ImgHighLightDataAllReq")
    public static class ImgHighLightDataAllReq {
        @ApiModelProperty("是否仅查询进度中最后的节点。默认false，查询进度所有节点")
        private Boolean isOnlyLast = false;
        @ApiModelProperty("是否查询进度中的线。默认true，查询结果有线的信息")
        private Boolean isContainHighLightLine = true;
        @ApiModelProperty("通过taskId查询进度")
        private String taskId;
        @ApiModelProperty("通过processInstanceId查询进度")
        private String processInstanceId;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("ImgVO.ImgHighLightDataAllRes")
    public static class ImgHighLightDataAllRes {
        @ApiModelProperty("线信息集合")
        private Collection<List<FlowLinePositionDTO>> flowLineList;
        @ApiModelProperty("点信息集合")
        private Collection<FlowNodePositionDTO> flowNodeList;
    }
}
