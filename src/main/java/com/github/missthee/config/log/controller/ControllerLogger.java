package com.github.missthee.config.log.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Aspect
@Slf4j
public class ControllerLogger {
    private static int RES_KEY_LENGTH;
    private static final List<Class> BASE_TYPE_LIST = new ArrayList<Class>() {{
        add(byte.class);
        add(Byte.class);
        add(short.class);
        add(Short.class);
        add(int.class);
        add(Integer.class);
        add(long.class);
        add(Long.class);
        add(float.class);
        add(Float.class);
        add(double.class);
        add(Double.class);
        add(boolean.class);
        add(Boolean.class);
        add(char.class);
        add(Character.class);
        add(String.class);
    }};

    @Pointcut("execution(public * *..controller..*.*(..))")
    public void webLog() {
    }

    //返回值为方法执行的结果对象
    @Around("webLog()")
    @SuppressWarnings("all")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object returnObj;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                stringBuilder.append("\r\n-------------------·REQ·--------------------");
            } else {
                HttpServletRequest request = attributes.getRequest();
                // 输出请求内容
                stringBuilder.append("\r\n-------------------↓REQ↓--------------------");
                stringBuilder.append("\r\nURI      : " + request.getRequestURI());
                stringBuilder.append("\r\nMETHOD   : " + request.getMethod());
                stringBuilder.append("\r\nIP       : " + request.getRemoteAddr());
                stringBuilder.append("\r\nCLASS    : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
                //获取所有参数：
                Object[] argsObj = joinPoint.getArgs();
                List<Object> argsObjList = Arrays.stream(argsObj).filter(e -> !(e instanceof HttpServletRequest || e instanceof HttpServletResponse || e instanceof HttpHeaders)).collect(Collectors.toList());//筛选掉HttpServlet相关参数
                try {
                    stringBuilder.append("\r\nARGS[J]  : " + JSONArray.toJSONString(argsObjList));
                } catch (Exception e) {
                    stringBuilder.append("\r\nARGS     : " + argsObjList);
                }
                Enumeration<String> enu = request.getParameterNames();
                StringBuilder paramSB = new StringBuilder();
                while (enu.hasMoreElements()) {
                    String paraName = enu.nextElement();
                    paramSB.append(paraName);
                    paramSB.append("=");
                    paramSB.append(request.getParameter(paraName));
                    paramSB.append("  ");
                }
                stringBuilder.append("\r\nPARAM    : " + paramSB.toString());
                try {
                    stringBuilder.append("\r\nUSER     : " + SecurityUtils.getSubject().getPrincipal().toString());
                } catch (Exception e) {
                    stringBuilder.append("\r\nUSER     : GUEST");
                }
                stringBuilder.append("\r\n-------------------↑REQ↑--------------------");
            }
        } catch (Exception e) {
            stringBuilder.append("\r\n!!!!!!!!!!!!!!!!!!!REQ-LOG-ERROR!!!!!!!!!!!!!!!!!!!");
        }
        returnObj = joinPoint.proceed();
        // 处理完请求，返回内容
        try {
            if (returnObj == null) {
                stringBuilder.append("\r\n-------------------·RES·--------------------");
            } else {
                stringBuilder.append("\r\n-------------------↓RES↓--------------------");
                Class<?> returnValueClass = returnObj.getClass();
                Field[] declaredFields = returnValueClass.getDeclaredFields();

                if (BASE_TYPE_LIST.contains(returnValueClass)) {
                    stringBuilder.append("\r\nRES : " + returnObj);
                } else {
                    if (RES_KEY_LENGTH == 0) {
                        for (Field field : declaredFields) {
                            RES_KEY_LENGTH = Math.max(RES_KEY_LENGTH, field.getName().length());
                        }
                    }
                    for (Field field : declaredFields) {
                        try {
                            String propertyType = field.getName();
                            String propertyName = field.getName();
                            Object value = GetterAndSetter.invokeGetMethod(returnObj, field.getName());
                            String valueStr;
                            try {
                                valueStr = JSON.toJSONString(value);
                            } catch (Exception ignord) {
                                valueStr = String.valueOf(value);
                            }
                            stringBuilder.append("\r\n" + String.format("%-" + RES_KEY_LENGTH + "s", propertyName.toUpperCase()) + " : " + valueStr);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                stringBuilder.append("\r\n-------------------↑RES↑--------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
            stringBuilder.append("\r\n!!!!!!!!!!!!!!!!!!!RES-LOG-ERROR!!!!!!!!!!!!!!!!!!!");
        }
        log.debug(stringBuilder.toString());
        return returnObj;
    }
}
