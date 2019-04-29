package com.github.flow.config.exception;

import com.github.common.config.exception.ExceptionResultModel;
import com.github.common.config.log.builder.LogBuilder;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.FlowableException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

//controller异常捕捉返回
@ApiIgnore
@RestControllerAdvice
@Order(100)
@Slf4j
public class FlowableExceptionHandler {
    @ExceptionHandler(FlowableException.class)
    public Object flowableException(HttpServletRequest request, FlowableException e) {
        log.debug(LogBuilder.requestLogBuilder(request, e));
        boolean is400 = false;
        if ((e.getMessage().startsWith("form property '") && (e.getMessage().endsWith("' is not writable") || e.getMessage().endsWith("' is required")))) {
            is400 = true;
        } else if (e.getMessage().startsWith("Invalid value for enum form property: ")) {
            is400 = true;
        } else {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ExceptionResultModel("FlowableException: " + e.getMessage()), is400 ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
