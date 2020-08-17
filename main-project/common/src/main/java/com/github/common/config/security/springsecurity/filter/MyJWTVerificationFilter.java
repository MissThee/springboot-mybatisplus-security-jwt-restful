package com.github.common.config.security.springsecurity.filter;

import com.github.common.config.security.jwt.JavaJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//这个filter则是每次验证身份的过滤器
@Component
public class MyJWTVerificationFilter extends OncePerRequestFilter {
    private final UserInfoForSecurity userInfoForSecurity;
    private final JavaJWT javaJWT;

    @Autowired
    public MyJWTVerificationFilter(JavaJWT javaJWT, UserInfoForSecurity userInfoForSecurity) {
        this.javaJWT = javaJWT;
        this.userInfoForSecurity = userInfoForSecurity;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token = httpServletRequest.getHeader(JavaJWT.JWT_TOKEN_KEY);
        Authentication authentication;
        Boolean verifyToken = javaJWT.verifyToken(token);

        //验证token成功与失败时，都会给当前用户创建一个UsernamePasswordAuthenticationToken对象，此处不阻止访问继续进行，而是仅构建存有用户权限描述的对象，供security后续逻辑使用
        //成功时，创建的UsernamePasswordAuthenticationToken附带了权限值，isAuthenticated为true（见UsernamePasswordAuthenticationToken构造函数源码），认为此用户是登录的用户
        //失败时，创建的UsernamePasswordAuthenticationToken未赋予权限值，isAuthenticated为false，认为此用户是未登录的用户
        if (verifyToken) {
            String userId = JavaJWT.getId();
            UserDetails userDetails = userInfoForSecurity.loadUserById(userId);
            authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
            //检查请求中携带的token是否快过期了，如果是就返回新的到header中
            javaJWT.updateTokenAndSetHeader(4 * 60 * 24);
        } else {
            authentication = new UsernamePasswordAuthenticationToken(null, null);
        }

        //将身份信息设置到Security当前上下文，即让配置的身份信息在本次请求中生效
        SecurityContextImpl securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    @Override
    public void destroy() {

    }
}
