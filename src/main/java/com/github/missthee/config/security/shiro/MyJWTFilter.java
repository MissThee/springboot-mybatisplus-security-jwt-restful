package com.github.missthee.config.security.shiro;

import com.github.missthee.config.security.jwt.JavaJWT;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class MyJWTFilter extends AuthenticatingFilter {
    private final JavaJWT javaJWT;

    @Autowired
    public MyJWTFilter(JavaJWT javaJWT) {
        this.javaJWT = javaJWT;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        executeLogin(request, response);
        return true;//使匿名用户可以通过此次用户身份构建
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        return false;
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String token = httpServletRequest.getHeader("Authorization");
        if (javaJWT.verifyToken(token)) {
            String userId = javaJWT.getId(token);
            if (!StringUtils.isEmpty(userId)) {
                AuthenticationToken authenticationToken = new AuthenticationToken() {
                    @Override
                    public Object getPrincipal() {
                        return userId;//将用户权限放置getPrincipal
                    }

                    @Override
                    public Object getCredentials() {
                        return "";//之后的验证未使用此值,仅使用principal
                    }
                };
                // 提交给realm为本次访问进行登入
                SecurityUtils.getSubject().login(authenticationToken);
                // 刷新token
                javaJWT.updateTokenAndSetHeader(token, httpServletResponse);
            }
        }
        return true;
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        return null;
    }
}

