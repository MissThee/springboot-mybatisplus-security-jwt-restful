package com.github.missthee.config.log.builder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

public class LogBuilder {
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

    public static String requestLogAspect(HttpServletRequest request, ProceedingJoinPoint joinPoint, String headLabel) {
        StringBuilder stringBuilder = new StringBuilder();
        // 输出请求内容
        stringBuilder.append("\r\n-------------------↓" + headLabel + "↓--------------------");
        stringBuilder.append("\r\nURI      : " + request.getRequestURI());
        stringBuilder.append("\r\nMETHOD   : " + request.getMethod());
        stringBuilder.append("\r\nIP       : " + request.getRemoteAddr());
        if (joinPoint != null) {
            stringBuilder.append(addJoinPointInfo(joinPoint));
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
        stringBuilder.append("\r\n-------------------↑" + headLabel + "↑--------------------");
        return stringBuilder.toString();
    }


    public static String requestLogAspect(HttpServletRequest request, ProceedingJoinPoint joinPoint) {
        return requestLogAspect(request, joinPoint, "REQ");
    }

    public static String requestLog(HttpServletRequest request, String headLabel) {
        return requestLogAspect(request, null, headLabel);
    }

    public static String requestLog(HttpServletRequest request) {
        return requestLogAspect(request, null, "REQ");
    }

    public static String responseLogAspect(Object returnObj, String headLabel) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\r\n-------------------↓" + headLabel + "↓--------------------");
        Class<?> returnValueClass = returnObj.getClass();
        Field[] declaredFields = returnValueClass.getDeclaredFields();

        if (BASE_TYPE_LIST.contains(returnValueClass)) {
            stringBuilder.append("\r\nRES : " + returnObj);
        } else {

            for (Field field : declaredFields) {
                try {
                    String propertyName = field.getName();
                    Object value = GetterAndSetter.invokeGetMethod(returnObj, field.getName());
                    String valueStr;
                    try {
                        valueStr = JSON.toJSONString(value);
                    } catch (Exception ignord) {
                        valueStr = String.valueOf(value);
                    }
                    stringBuilder.append("\r\n" + String.format("%-8s", propertyName.toUpperCase()) + " : " + valueStr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        stringBuilder.append("\r\n-------------------↑" + headLabel + "↑--------------------");
        return stringBuilder.toString();
    }

    public static String responseLogAspect(Object returnObj) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return responseLogAspect(returnObj, "RES");
    }

    private static String addJoinPointInfo(ProceedingJoinPoint joinPoint) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\r\nCLASS    : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        //获取所有参数：
        Object[] argsObj = joinPoint.getArgs();
        List<Object> argsObjList = Arrays.stream(argsObj).filter(e -> !(e instanceof HttpServletRequest || e instanceof HttpServletResponse || e instanceof HttpHeaders)).collect(Collectors.toList());//筛选掉HttpServlet相关参数
        try {
            stringBuilder.append("\r\nARGS[J]  : " + JSONArray.toJSONString(argsObjList));
        } catch (Exception e) {
            stringBuilder.append("\r\nARGS     : " + argsObjList);
        }
        return stringBuilder.toString();
    }
}
