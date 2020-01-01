package com.github.common.config.security.springsecurity.filter;

import com.github.common.config.security.jwt.JavaJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
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
        //成功时，创建的UsernamePasswordAuthenticationToken附带了权限值，构造函数中会将isAuthenticated自动设置为true，认为此用户是登录的用户
        //失败时，创建的UsernamePasswordAuthenticationToken未权限值，isAuthenticated为false，认为此用户是未登录的用户
        if (verifyToken) {
            String userId = JavaJWT.getId();
            //此处选择UserDetails来传递用户信息，仅因为UserDetails的属性正好包含了要用的三个属性:username,password,authorities。所以直接用它了，不用自己再写一个新的类了
            UserDetails userDetails = userInfoForSecurity.loadUserById(userId);
            authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
            javaJWT.updateTokenAndSetHeaderWithAvailableMinute(token, 4 * 24 * 60);
        } else {
            authentication = new UsernamePasswordAuthenticationToken(null, null);
        }
        //将身份信息设置到Security当前上下文，即让配置的身份信息在本次请求中生效
        SecurityContextHolder.setContext(new SecurityContextImpl() {{
            setAuthentication(authentication);
        }});
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    @Override
    public void destroy() {

    }
}
