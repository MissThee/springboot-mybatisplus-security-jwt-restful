package com.github.common.config.exception.handler;

import com.github.common.config.exception.model.ExceptionResultModel;
import com.github.common.config.log.builder.LogBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

//controller异常捕捉返回。捕获预期之外的异常
@ApiIgnore
@RestControllerAdvice
@Order(1000)
@Slf4j
public class UnknownExceptionHandler {

    //运行时所有异常
    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public Object exceptionHandler(HttpServletRequest request, Exception e) {
        log.debug(LogBuilder.requestLogBuilder(request, e));
        e.printStackTrace();
        return new ExceptionResultModel("Exception: " + e);
    }
}