package com.github.common.config.log.builder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LogBuilder {
    private static ObjectMapper mapper=new ObjectMapper(){{
        configure(SerializationFeature.INDENT_OUTPUT, false); //格式化输出，true就是json格式化，false就是输出一行
        configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true); //键按自然顺序输出
        setSerializationInclusion(JsonInclude.Include.ALWAYS);    //忽略POJO中属性为空的字段
    }};
    public static String requestLogBuilder(HttpServletRequest request, ProceedingJoinPoint joinPoint, Exception exception, Map<String, Object> extractParamMap) {
        String headLabel = "REQ";
        StringBuilder stringBuilder = new StringBuilder();
        //输出请求内容
        stringBuilder.append(paramFormatter("URI", request.getRequestURI()));
        stringBuilder.append(paramFormatter("METHOD", request.getMethod()));
        stringBuilder.append(paramFormatter("IP", request.getRemoteAddr()));
        //输出切点内容
        if (joinPoint != null) {
            stringBuilder.append(paramFormatter("CLASS", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName()));
            //获取所有参数：
            Object[] argsObj = joinPoint.getArgs();
            List<Object> argsObjList = Arrays.stream(argsObj).filter(e -> !(e instanceof HttpServletRequest || e instanceof HttpServletResponse || e instanceof HttpHeaders)).collect(Collectors.toList());//筛选掉HttpServlet相关参数
            try {
                stringBuilder.append(paramFormatter("ARGS[J]", mapper.writeValueAsString(argsObjList)));
            } catch (Exception e) {
                stringBuilder.append(paramFormatter("ARGS", argsObjList));
            }
        }
        //输出请求参数
        if (request.getParameterNames() != null && request.getParameterNames().hasMoreElements()) {
            Enumeration<String> enu = request.getParameterNames();
            StringBuilder paramSB = new StringBuilder();
            while (enu.hasMoreElements()) {
                String paraName = enu.nextElement();
                paramSB.append(paraName);
                paramSB.append("=");
                paramSB.append(request.getParameter(paraName));
                paramSB.append("  ");
            }
            stringBuilder.append(paramFormatter("PARAM", paramSB));
            try {
                String userId = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails()).getUsername();
//               String userId =  SecurityUtils.getSubject().getPrincipal())
                stringBuilder.append(paramFormatter("USER", userId));
            } catch (Exception e) {
                stringBuilder.append(paramFormatter("USER", "GUEST"));
            }
        }
        //输出异常，并修改分隔行label
        if (exception != null) {
            stringBuilder.append(paramFormatter("EXCEPTION", exception.getMessage()));
            headLabel = "EXCEPTION";
        }
        if (extractParamMap != null) {
            for (String key : extractParamMap.keySet()) {
                stringBuilder.append(paramFormatter(key, extractParamMap.get(key)));
            }
        }
        //添加分隔行
        stringBuilder.insert(0, splitRow(headLabel, false));
        stringBuilder.append(splitRow(headLabel, true));
        return stringBuilder.toString();
    }

    public static String requestLogBuilder(HttpServletRequest request, ProceedingJoinPoint joinPoint, Exception exception) {
        return requestLogBuilder(request, joinPoint, exception, null);
    }

    public static String requestLogBuilder(HttpServletRequest request, ProceedingJoinPoint joinPoint, Map<String, Object> extractParamMap) {
        return requestLogBuilder(request, joinPoint, null, extractParamMap);
    }

    public static String requestLogBuilder(HttpServletRequest request, Exception exception, Map<String, Object> extractParamMap) {
        return requestLogBuilder(request, null, exception, extractParamMap);
    }

    public static String requestLogBuilder(HttpServletRequest request, ProceedingJoinPoint joinPoint) {
        return requestLogBuilder(request, joinPoint, null, null);
    }

    public static String requestLogBuilder(HttpServletRequest request, Exception exception) {
        return requestLogBuilder(request, null, exception, null);
    }

    public static String responseLogAspect(Object returnObj) {
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder resContent = new StringBuilder();
        try {
            Field[] declaredFields = returnObj.getClass().getDeclaredFields();
            for (Field field : declaredFields) {

                String propertyName = field.getName();
                Object value = GetterAndSetter.invokeGetMethod(returnObj, field.getName());
                String valueStr;
                try {
                    valueStr = mapper.writeValueAsString(value);
                } catch (Exception ignord) {
                    valueStr = String.valueOf(value);
                }
                resContent.append(paramFormatter(propertyName.toUpperCase(), valueStr));

            }
        } catch (Exception ignored) {
            resContent = new StringBuilder(paramFormatter("RES", returnObj));
        }
        stringBuilder.append(resContent);
        //添加分隔行
        stringBuilder.insert(0, splitRow("RES", false));
        stringBuilder.append(splitRow("RES", true));
        return stringBuilder.toString();
    }

    /**
     * 分隔行文字
     *
     * @param headLabel
     * @param isEnd
     * @return
     */
    private static String splitRow(String headLabel, boolean isEnd) {
        if (isEnd) {
            return "\r\n-------------------" + headLabel + " End--------------------";
        } else {
            return "\r\n-------------------" + headLabel + " Start----------------------";
        }
    }

    private static String paramFormatter(String key, Object value) {
        return "\r\n" + String.format("%-8s", key.toUpperCase()) + " : " + value;
    }
}
