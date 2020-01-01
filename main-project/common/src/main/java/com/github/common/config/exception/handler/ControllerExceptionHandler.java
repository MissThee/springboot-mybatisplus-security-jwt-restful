package com.github.common.config.exception.handler;

import com.github.common.config.exception.custom.MyMethodArgumentNotValidException;
import com.github.common.config.exception.custom.MyMissingDataException;
import com.github.common.config.exception.model.ExceptionResultModel;
import com.github.common.config.log.builder.LogBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

//controller异常捕捉返回
//此处出现的异常是已经预期到的异常情况。UnknownExceptionHandler中捕获预期之外的exception。使用@Order控制两者顺序
@ApiIgnore
@RestControllerAdvice
@Order(100)
@Slf4j
public class ControllerExceptionHandler {

    //参数错误
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Object httpMessageNotReadableException(HttpServletRequest request, HttpMessageNotReadableException e) {
        log.debug(LogBuilder.requestLogBuilder(request, e));
        ExceptionResultModel exceptionResultModel = new ExceptionResultModel();
        if (String.valueOf(e).contains("Required request body is missing")) {
            exceptionResultModel.setMsg("HttpMessageNotReadableException: 请求体缺少body。" + e);
        } else {
            exceptionResultModel.setMsg("HttpMessageNotReadableException: 无法正确读取请求中的参数。" + e);
        }
        return exceptionResultModel;
    }

    @ExceptionHandler(MissingRequestHeaderException.class)//ServletRequestBindingException  2.1.0之前
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Object missingRequestHeaderException(HttpServletRequest request, MissingRequestHeaderException e) {
        log.debug(LogBuilder.requestLogBuilder(request, e));
        String paramName = null;
        try {
            paramName = String.valueOf(e).substring(String.valueOf(e).indexOf("'") + 1, String.valueOf(e).lastIndexOf("'"));
        } catch (Exception ignored) {

        }
        return new ExceptionResultModel((paramName == null ? "" : "MissingRequestHeaderException: 请求体header中缺少必须的参数【" + paramName + "】。") + e);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Object methodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException e) {
        log.debug(LogBuilder.requestLogBuilder(request, e));
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder errorMessageSB = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMessageSB.append(fieldError.getField());
            errorMessageSB.append(":");
            errorMessageSB.append(fieldError.getDefaultMessage());
            errorMessageSB.append("; ");
        }
        return new ExceptionResultModel("MethodArgumentNotValidException: 参数有误：" + errorMessageSB.toString());
    }

    @ExceptionHandler(MyMethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Object myMethodArgumentNotValidException(HttpServletRequest request, MyMethodArgumentNotValidException e) {
        log.debug(LogBuilder.requestLogBuilder(request, e));
        return new ExceptionResultModel(e.getMessage());
    }

    @ExceptionHandler(MyMissingDataException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public Object myMissingDataException(HttpServletRequest request, MyMethodArgumentNotValidException e) {
        log.debug(LogBuilder.requestLogBuilder(request, e));
        return new ExceptionResultModel(e.getMessage());
    }
}