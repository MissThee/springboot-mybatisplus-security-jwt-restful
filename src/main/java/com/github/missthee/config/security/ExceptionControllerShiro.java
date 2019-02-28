package com.github.missthee.config.security;

import com.alibaba.fastjson.JSONObject;
import com.github.missthee.config.log.builder.LogBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

//controller异常捕捉返回
@ApiIgnore
@RestControllerAdvice
@Order(1)
@Slf4j
public class ExceptionControllerShiro {
    //访问无权限接口
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public Object unauthorizedException(HttpServletRequest request, Exception e) {
        log.debug(LogBuilder.requestLog(request,"ERROR") + "\r\nEXCEPTION : " + e.getMessage());
        JSONObject jO = new JSONObject();
        jO.put("msg", "UnauthorizedException:" + HttpStatus.FORBIDDEN.getReasonPhrase());
        return jO;
    }

    //需要登录
    @ExceptionHandler(UnauthenticatedException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public Object unauthenticatedException(HttpServletRequest request, Exception e) {
        log.debug(LogBuilder.requestLog(request,"ERROR") + "\r\nEXCEPTION : " + e.getMessage());
        JSONObject jO = new JSONObject();
        jO.put("msg", "UnauthenticatedException:" + HttpStatus.UNAUTHORIZED.getReasonPhrase());
        return jO;
    }
}