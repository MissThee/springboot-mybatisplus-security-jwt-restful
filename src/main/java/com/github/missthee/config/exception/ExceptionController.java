package com.github.missthee.config.exception;

import com.alibaba.fastjson.JSONObject;
import com.github.missthee.config.log.builder.LogBuilder;
import lombok.extern.slf4j.Slf4j;
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
@Order(100)
@Slf4j
public class ExceptionController {

    //参数错误
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public Object httpMessageNotReadableException(HttpServletRequest request, Exception e) {
        log.debug(LogBuilder.requestLogBuilder(request, e));
        JSONObject jO = new JSONObject();
        if (String.valueOf(e).contains("Required request body is missing")) {
            jO.put("msg", "HttpMessageNotReadableException: 请求体缺少body。" + e);
        } else {
            jO.put("msg", "HttpMessageNotReadableException: 无法正确读取请求中的参数。" + e);
        }
        return jO;
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public Object missingRequestHeaderExceptionException(HttpServletRequest request, Exception e) {
        log.debug(LogBuilder.requestLogBuilder(request, e));
        JSONObject jO = new JSONObject();
        String paramName = null;
        try {
            paramName = String.valueOf(e).substring(String.valueOf(e).indexOf("'") + 1, String.valueOf(e).lastIndexOf("'"));
        } catch (Exception ignored) {
        }
        jO.put("msg", (paramName == null ? "" : "MissingRequestHeaderException: 请求体header中缺少必须的参数【" + paramName + "】。") + e);
        return jO;
    }

    //运行时所有异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @Order()
    public Object exceptionHandler(HttpServletRequest request, Exception e) {
        log.debug(LogBuilder.requestLogBuilder(request, e));
        e.printStackTrace();
        JSONObject jO = new JSONObject();
        jO.put("msg", "Exception: " + e);
        return jO;
    }

}