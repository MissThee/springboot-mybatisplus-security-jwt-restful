package com.github.common.config.security.springsecurity.filter;

import com.github.common.config.security.jwt.JavaJWT;
import jdk.nashorn.internal.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import springfox.documentation.spring.web.json.Json;

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
//        if (verifyToken == null) {
//            httpServletResponse.setStatus(500);
//            httpServletResponse.getWriter().write("{\"msg\":\"身份验证出现错误\"}");
//            httpServletResponse.flushBuffer();
//            return;
//        } else
            if (verifyToken) {
            String userId = JavaJWT.getId();
            UserDetails userDetails = userInfoForSecurity.loadUserById(userId);
            authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
            javaJWT.updateTokenAndSetHeaderRemainDays(token, 4 * 24 * 60);
        } else {
            authentication = new UsernamePasswordAuthenticationToken(null, null);
        }
        SecurityContext securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    @Override
    public void destroy() {

    }
}
