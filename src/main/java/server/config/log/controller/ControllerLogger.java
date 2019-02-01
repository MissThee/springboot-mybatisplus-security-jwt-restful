package server.config.log.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import javassist.bytecode.SignatureAttribute;
import lombok.extern.slf4j.Slf4j;
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
                stringBuilder.append("\r\nPARAM     : " + paramSB.toString());
                stringBuilder.append(userLog(request));//记录用户信息，无相应方法可删除
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
            }else {
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

    //------用户信息记录，无相应方法可删除----START
    private final UserMapper userMapper;

    @Autowired
    public ControllerLogger(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    private String userLog(HttpServletRequest request) {
        try {
            User user = userMapper.selectByPrimaryKey(JavaJWT.getId(request.getHeader("Authorization")));
            return "\r\nUSER     : " + user.getNickname() + "[id: " + user.getId() + "]";
        } catch (Exception e) {
            return "\r\nUSER     : GUEST";
        }
    }
    //------用户信息记录，无相应方法可删除----END
//// 使用before after无法同时收集传入参数与返回参数，改用around
//    @Before("webLog()")
//    public void doBefore(JoinPoint joinPoint) {
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        if (attributes == null) {
//            log.debug("-------------------↓REQ↓--------------------");
//            log.debug("Empty Attributes");
//            log.debug("-------------------↑REQ↑--------------------");
//        } else {
//            HttpServletRequest request = attributes.getRequest();
//            // 输出请求内容
//            log.debug("-------------------↓REQ↓--------------------");
//            log.debug("URI    : " + request.getRequestURI());
//            log.debug("METHOD : " + request.getMethod());
//            log.debug("IP     : " + request.getRemoteAddr());
//            log.debug("CLASS  : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
//            Object[] argsObj = joinPoint.getArgs();
//            log.debug("ARGS   : " + (argsObj.length > 0 ? (argsObj.length > 1 ? Arrays.toString(argsObj) : argsObj[0]) : ""));
//            //获取所有参数方法一：
//            Enumeration<String> enu = request.getParameterNames();
//            StringBuilder paramSB = new StringBuilder();
//            while (enu.hasMoreElements()) {
//                String paraName = enu.nextElement();
//                paramSB.append(paraName);
//                paramSB.append("=");
//                paramSB.append(request.getParameter(paraName));
//                paramSB.append("  ");
//            }
//            log.debug("PARAM  : " + paramSB.toString());
////        JSONObject paramJObj = JSON.parseObject(HttpKit.readData(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()));
////        log.debug("PARAM-JSON : " + (paramJObj == null ? "" : paramJObj.toString()));
////        try {
////            log.debug("PARAM-JSON : " + getRequestStr(request));
////        } catch (Exception e) {
////            log.debug("PARAM-JSON : 出错:" + e.getMessage());
////        }
//            log.debug("-------------------↑REQ↑--------------------");
//        }
//    }
//
//    @AfterReturning(returning = "ret", pointcut = "webLog()")
//    public void doAfterReturning(Object ret) {
//
//        // 处理完请求，返回内容
//        log.debug("-------------------↓RES↓--------------------");
//        if (ret instanceof Res) {
//            log.debug("RESPONSE: DATA: " + ret);
//        } else {
//            log.debug("RESPONSE: DATA: " + ret);
//        }
//        log.debug("-------------------↑RES↑--------------------");
//    }


//    private static byte[] getRequestBytes(HttpServletRequest request) throws IOException {
//        int contentLength = request.getContentLength();
//        if (contentLength < 0) {
//            return null;
//        }
//        byte buffer[] = new byte[contentLength];
//        for (int i = 0; i < contentLength; ) {
//            int readLen = request.getInputStream().read(buffer, i, contentLength - i);
//            if (readLen == -1) {
//                break;
//            }
//            i += readLen;
//        }
//        return buffer;
//    }

//    private static String getRequestStr(HttpServletRequest request) throws IOException {
//        byte buffer[] = getRequestBytes(request);
//        String charEncoding = request.getCharacterEncoding();
//        if (charEncoding == null) {
//            charEncoding = "UTF-8";
//        }
//        return new String(buffer, charEncoding);
//    }

}
