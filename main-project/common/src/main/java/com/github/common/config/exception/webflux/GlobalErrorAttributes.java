//package com.github.common.config.exception.webflux;
//
//import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
//import org.springframework.stereotype.Component;
////import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
////import org.springframework.core.annotation.AnnotatedElementUtils;
////import org.springframework.http.HttpStatus;
////import org.springframework.stereotype.Component;
////import org.springframework.web.bind.annotation.ResponseStatus;
////import org.springframework.web.bind.support.WebExchangeBindException;
////import org.springframework.web.reactive.function.server.ServerRequest;
////import org.springframework.web.server.ResponseStatusException;
////import java.util.LinkedHashMap;
////import java.util.Map;
////webflux异常处理
////本类用于修改errorAttributes，但最终返回结果可由GlobalErrorWebExceptionHandler处理，故此处无需对其进行处理
//@Component
//public class GlobalErrorAttributes extends DefaultErrorAttributes {
////    public GlobalErrorAttributes() {
////        super(false);
////    }
//
////    @Override
////    public Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
////        Map<String, Object> errorAttributes = new LinkedHashMap<>(); //super.getErrorAttributes(request, includeStackTrace);
////        Throwable error = getError(request);
////        HttpStatus errorStatus = this.determineHttpStatus(error);
////        errorAttributes.put("status", errorStatus.value());
////        errorAttributes.put("msg", this.determineMessage(error));
////        return errorAttributes;
////    }
////
////    private String determineMessage(Throwable error) {
////        if (error instanceof WebExchangeBindException) {
////            return error.getMessage();
////        } else if (error instanceof ResponseStatusException) {
////            return ((ResponseStatusException) error).getReason();
////        } else {
////            ResponseStatus responseStatus = AnnotatedElementUtils.findMergedAnnotation(error.getClass(), ResponseStatus.class);
////            return responseStatus != null ? responseStatus.reason() : error.getMessage();
////
////        }
////    }
////    private HttpStatus determineHttpStatus(Throwable error) {
////        if (error instanceof ResponseStatusException) {
////            return ((ResponseStatusException)error).getStatus();
////        } else {
////            ResponseStatus responseStatus = AnnotatedElementUtils.findMergedAnnotation(error.getClass(), ResponseStatus.class);
////            return responseStatus != null ? responseStatus.code() : HttpStatus.INTERNAL_SERVER_ERROR;
////        }
////    }
//}