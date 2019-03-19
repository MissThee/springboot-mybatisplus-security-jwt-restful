package com.github.missthee.config.security.security;

import com.alibaba.fastjson.JSONObject;
import com.github.missthee.config.log.builder.LogBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
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
public class ExceptionControllerSecurity {
    //访问无权限接口
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public Object unauthorizedException(HttpServletRequest request, Exception e) {
        log.debug(LogBuilder.requestLogBuilder(request, null, e));
        return new JSONObject() {{
            put("msg", e.getClass().getSimpleName() + ":" + e.getMessage());
        }};
    }

    //需要登录
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public Object unauthenticatedException(HttpServletRequest request, Exception e) {
        log.debug(LogBuilder.requestLogBuilder(request, null, e));
        return new JSONObject() {{
            put("msg", e.getClass().getSimpleName() + ":" + e.getMessage());
        }};
    }
}