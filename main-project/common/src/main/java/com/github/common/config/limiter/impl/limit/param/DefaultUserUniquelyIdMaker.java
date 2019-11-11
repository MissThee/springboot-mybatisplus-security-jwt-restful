package com.github.common.config.limiter.impl.limit.param;

import com.github.common.config.security.jwt.JavaJWT;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class DefaultUserUniquelyIdMaker implements UserUniquelyIdMaker {
    @Override
    public String getUserUniquelyId() {
        StringBuilder stringBuilder = new StringBuilder();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            stringBuilder.append(request.getHeader(JavaJWT.JWT_TOKEN_KEY));//用户令牌
            stringBuilder.append(request.getRemoteAddr());//IP
            stringBuilder.append(request.getRequestURI());//URI
            stringBuilder.append(request.getMethod());//METHOD
        }
        return stringBuilder.toString();
    }
}
