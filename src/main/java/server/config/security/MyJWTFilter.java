package server.config.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
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
        if (JavaJWT.verifyToken(token)) {
            AuthenticationToken authenticationToken = new AuthenticationToken() {
                @Override
                public Object getPrincipal() { //将用户权限放置getPrincipal
                    return token;
                }//直接将构建的用户身份对象放置此处，供身份验证使用

                @Override
                public Object getCredentials() {
                    return "";//之后的验证未使用此值
                }
            };
            // 提交给realm进行登入，（仅在本次访问中有效，因为前后分离是无状态连接。故名为登入，实则是为此次访问填入权限），如果错误他会抛出异常并被捕获
            getSubject(request, response).login(authenticationToken);
            httpServletResponse.setHeader("Authorization", JavaJWT.updateToken(token, 15));
        }
        return true;
    }
}
