package server.config.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.codehaus.janino.Java;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import server.db.primary.dto.login.LoginDTO;
import server.service.interf.login.LoginService;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class MyJWTFilter extends BasicHttpAuthenticationFilter {
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
                // 提交给realm进行登入，（仅在本次访问中有效，因为前后分离是无状态连接。故名为登入，实则是为此次访问填入权限），如果错误他会抛出异常并被捕获
                getSubject(request, response).login(authenticationToken);
                // 刷新token
                javaJWT.updateTokenAndSetHeader(token, httpServletResponse);
            }
        }
        return true;
    }
}

