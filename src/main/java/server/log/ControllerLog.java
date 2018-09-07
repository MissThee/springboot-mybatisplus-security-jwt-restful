package server.log;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;

@Component
@Aspect
public class ControllerLog {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(public * server.controller..*.*(..))")
    public void webLog() {
    }

    //    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        logger.info("-------------------------------------------------------");
        logger.info("URL: {}", request.getRequestURL().toString());
        logger.info("METHOD: {}", request.getMethod());
        logger.info("URI: {}", request.getRequestURI());
        logger.info("IP     : " + request.getRemoteAddr());
        logger.info("CLASS_METHOD: {}", pjp.getSignature().getDeclaringTypeName());
        logger.info("PARAMS: {}", request.getQueryString());
//        logger.info("PARAMS-JSON: {}", getRequestStr(request));
        //        JSONObject paramJObj = JSON.parseObject(HttpKit.readData(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()));
//        logger.info("PARAM-JSON : " + (paramJObj == null ? "" : paramJObj.toString()));
//        try {
//            logger.info("PARAM-JSON : " + getRequestStr(request));
//        } catch (Exception e) {
//            logger.info("PARAM-JSON : 出错:" + e.getMessage());
//        }
        logger.info("-------------------------------------------------------");

        // 执行被拦截方法，获取被拦截方法返回结果，result的值就是被拦截方法的返回值
        Object result = pjp.proceed();

        logger.info("RESPONSE : " + result);
//        //拦截的实体类
//        Object target = pjp.getTarget();
//        //拦截的方法名称
//        String methodName = pjp.getSignature().getName();
//        //拦截的放参数类型
//        Class[] parameterTypes = ((MethodSignature)pjp.getSignature()).getMethod().getParameterTypes();
//        Method method = target.getClass().getMethod(methodName, parameterTypes);
//        Annotation[] annotations = method.getDeclaredAnnotations();
//        StringBuilder annotationsSB=new StringBuilder();
//        for(int i=0;i<annotations.length;i++){
//            annotationsSB.append(annotations[i].getClass());
//        }
//        logger.info("Annotations : " + annotationsSB);
//        Signature signature = pjp.getSignature();
//        MethodSignature methodSignature = (MethodSignature)signature;
//        Method targetMethod = methodSignature.getMethod();
//        Class clazz = targetMethod.getClass();
        return result;
    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 记录下请求内容
        logger.info("-------------------↓REQ↓--------------------");
        logger.info("URL    : " + request.getRequestURL());
        logger.info("METHOD : " + request.getMethod());
        logger.info("IP     : " + request.getRemoteAddr());
        logger.info("CLASS  : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        Object[] argsObj = joinPoint.getArgs();
        logger.info("ARGS   : " + (argsObj.length > 0 ? (argsObj.length > 1 ? Arrays.toString(argsObj) : argsObj[0]) : ""));
        //获取所有参数方法一：
        Enumeration<String> enu = request.getParameterNames();
        StringBuilder paramSB = new StringBuilder();
        while (enu.hasMoreElements()) {
            String paraName = (String) enu.nextElement();
            paramSB.append(paraName);
            paramSB.append("=");
            paramSB.append(request.getParameter(paraName));
            paramSB.append("  ");
        }
        logger.info("PARAM  : " + paramSB.toString());
//        JSONObject paramJObj = JSON.parseObject(HttpKit.readData(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()));
//        logger.info("PARAM-JSON : " + (paramJObj == null ? "" : paramJObj.toString()));
//        try {
//            logger.info("PARAM-JSON : " + getRequestStr(request));
//        } catch (Exception e) {
//            logger.info("PARAM-JSON : 出错:" + e.getMessage());
//        }
        logger.info("-------------------↑REQ↑--------------------");
    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) {
        // 处理完请求，返回内容
        logger.info("-------------------↓RES↓--------------------");
        logger.info("RESPONSE: DATA: " + ret);
        logger.info("-------------------↑RES↑--------------------");
    }

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
