package com.github.common.config.log.aspect;


import com.github.common.config.log.builder.LogBuilder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
//自定义日志输出。仅输出controller包下相关controller的请求和返回内容日志，方便调试。
@Component
@Aspect
@Slf4j
public class ControllerLogger {
    @Pointcut("execution(public !void *..controller..*.*(..))")
    public void webLog() {
    }

    //返回值为方法执行的结果对象
    @Around("webLog()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object returnObj;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                //LogBuilder.requestLogBuilder主要负责将请求中的参数、url、等信息整理输出
                stringBuilder.append(LogBuilder.requestLogBuilder(request,joinPoint));
            }
        } catch (Exception e) {
            stringBuilder.append("\r\n!!!!!!!!!!!!!!!!!!!REQ-LOG-ERROR!!!!!!!!!!!!!!!!!!!");
        }
        returnObj = joinPoint.proceed();//执行方法
        try {
            if (returnObj != null) {
                //LogBuilder.requestLogBuilder主要负责将返回中的结果、url、等信息整理输出
                stringBuilder.append(LogBuilder.responseLogAspect(returnObj));
            }
        } catch (Exception e) {
            e.printStackTrace();
            stringBuilder.append("\r\n!!!!!!!!!!!!!!!!!!!RES-LOG-ERROR!!!!!!!!!!!!!!!!!!!");
        }
        log.debug(stringBuilder.toString());
        return returnObj;
    }
}
