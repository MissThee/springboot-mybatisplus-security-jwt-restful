//package server.aspect;
//
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.Arrays;
//
//@Aspect
//    @Component
//    public class FormDataFormat {
//        private Logger logger = LoggerFactory.getLogger(this.getClass());
//
//        @Pointcut("execution(public * server.controller.reportform..*.getData(..))")
//        public void formatFunc(){}
//
//        @Before("formatFunc()")
//        public void doBefore(JoinPoint joinPoint)   {
//            // 接收到请求，记录请求内容
//            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//            HttpServletRequest request = attributes.getRequest();
//
//            // 记录下请求内容
//            logger.info("URL : " + request.getRequestURL().toString());
//            logger.info("HTTP_METHOD : " + request.getMethod());
//            logger.info("IP : " + request.getRemoteAddr());
//            logger.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
//            logger.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));
//        }
//
//        @AfterReturning(returning = "ret", pointcut = "formatFunc()")
//        public void doAfterReturning(Object ret) {
//            logger.info("RESPONSE : " + ret);
//        }
//
//}
