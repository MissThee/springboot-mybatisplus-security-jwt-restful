package com.github.common.config.limiter.impl.limit;

import com.github.common.config.limiter.annotation.RLimit;
import com.github.common.config.limiter.impl.limit.param.UserUniquelyIdMaker;
import com.github.common.config.limiter.store.LimitInfoStore;
import com.github.common.tool.Res;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;

@Aspect
@Slf4j
public class LimitPointCut {
    private final UserUniquelyIdMaker userUniquelyIdMaker;
    private final LimitInfoStore limitInfoStore;

    public LimitPointCut(LimitInfoStore limitInfoStore, UserUniquelyIdMaker userUniquelyIdMaker) {
        this.limitInfoStore = limitInfoStore;
        this.userUniquelyIdMaker = userUniquelyIdMaker;
    }

    //切入点为使用了Limit注解的任何地方
    @Pointcut("@annotation(com.github.common.config.limiter.annotation.RLimit)")
    public void LimitPointCut() {
    }

    @Around(value = "LimitPointCut() && @annotation(rLimit)")
    public Object around(ProceedingJoinPoint joinPoint, RLimit rLimit) throws Throwable {
        if (joinPoint.getSignature().getDeclaringTypeName().endsWith("Controller")) {
            String userUniquelyIdStr = userUniquelyIdMaker.getUserUniquelyId();
            Object limitInfo = limitInfoStore.getLimitInfo(userUniquelyIdStr);
            long millis = System.currentTimeMillis() - ((Long) limitInfo);
            if (millis < rLimit.minTime()) {//判断访问间隔是否小于最小间隔
                Res<String> res;
                {//构建返回对象
                    String failureMsg;
                    if (StringUtils.isEmpty(rLimit.msg())) {
                        DecimalFormat df = new DecimalFormat("#.##");
                        failureMsg = "访问过于频繁，最小间隔:" + df.format(rLimit.minTime() / 1000D) + "秒";
                    } else {
                        failureMsg = rLimit.msg();
                    }
                    res = Res.failure(failureMsg);
                }
                {//修改状态码
                    ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                    if (servletRequestAttributes != null) {
                        HttpServletResponse response = servletRequestAttributes.getResponse();
                        if (response != null) {
                            response.setStatus(500);
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//                            直接使用流返回，在此返回可使@RLimit注解用在任何Controller以外的方法，且保证返回值的正确性。但日志无法捕获此返回值
//                            ServletOutputStream outputStream = response.getOutputStream();
//                            outputStream.write(objectMapper.writeValueAsString(res).getBytes());
//                            outputStream.flush();
//                            outputStream.close();
                        }
                    }
                }
                return res;
            }
            limitInfoStore.setLimitInfo(userUniquelyIdStr);
        }
        return joinPoint.proceed();//执行切点原方法，得到返回值
    }
}
