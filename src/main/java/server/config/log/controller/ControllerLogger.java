package server.config.log.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import javassist.bytecode.SignatureAttribute;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import server.config.security.JavaJWT;
import server.db.primary.mapper.basic.UserMapper;
import server.db.primary.model.basic.User;
import server.tool.Res;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Aspect
@Slf4j
public class ControllerLogger {

    @Pointcut("execution(public * server.controller..*.*(..))")
    public void webLog() {
    }

    //返回值为方法执行的结果对象
    @Around("webLog()")
    @SuppressWarnings("all")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object returnValue;
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
        returnValue = joinPoint.proceed();
        // 处理完请求，返回内容
        try {
            if (returnValue == null) {
                stringBuilder.append("\r\n-------------------·RES·--------------------");
            } else {
                stringBuilder.append("\r\n-------------------↓RES↓--------------------");
                if (returnValue instanceof Res) {
                    stringBuilder.append("\r\nRESULT : " + ((Res) returnValue).getResult());
                    stringBuilder.append("\r\nDATA   : " + JSONObject.toJSONString(((Res) returnValue).getData()));
                    stringBuilder.append("\r\nMSG    : " + ((Res) returnValue).getMsg());
                } else {
                    stringBuilder.append("\r\nRESPONSE: " + returnValue);
                }
                stringBuilder.append("\r\n-------------------↑RES↑--------------------");
            }
        } catch (Exception e) {
            stringBuilder.append("\r\n!!!!!!!!!!!!!!!!!!!RES-LOG-ERROR!!!!!!!!!!!!!!!!!!!");
        }
        log.debug(stringBuilder.toString());
        return returnValue;
    }
}
