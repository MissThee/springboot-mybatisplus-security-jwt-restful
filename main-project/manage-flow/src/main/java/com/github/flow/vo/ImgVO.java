package com.github.flow.vo;

import lombok.Data;

public class ImgVO {
    @Data
    public static class ImgReq {
        private String taskId;
        private String processInstanceId;
    }

    @Data
    public static class ImgWithHighLightReq {
        private Boolean isOnlyLast;
        private String taskId;
        private String processInstanceId;
    }

    @Data
    public static class ImgHighLightDataAllReq {
        private Boolean isOnlyLast;
        private Boolean  isContainHighLightLine;
        private String taskId;
        private String processInstanceId;
    }

}
