package com.github.common.config.security.springsecurity.exception;

import com.github.common.config.exception.model.ExceptionResultModel;
import com.github.common.config.log.builder.LogBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

//捕获未授权访问引起的异常，返回固定格式json返回值
@ApiIgnore
@RestControllerAdvice
@Order(1)
@Slf4j
public class SecurityExceptionHandler {
    //无访问权限。返回状态403。约定前端任何时候接收403状态码时需给用户提示无权访问提示
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public Object unauthorizedException(HttpServletRequest request, AccessDeniedException e) {
        log.debug(LogBuilder.requestLogBuilder(request, null, e));
        return new ExceptionResultModel(e.getClass().getSimpleName() + ":" + e.getMessage());
    }

    //需要登录。返回状态401。约定前端任何时候接收401状态码时需给用户提供登录页面
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public Object unauthenticatedException(HttpServletRequest request, BadCredentialsException e) {
        log.debug(LogBuilder.requestLogBuilder(request, null, e));
        return new ExceptionResultModel(e.getClass().getSimpleName() + ":" + e.getMessage());
    }
}