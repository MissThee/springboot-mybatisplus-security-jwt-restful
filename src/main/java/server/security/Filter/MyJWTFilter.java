package server.security.Filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.stereotype.Component;
import server.security.JavaJWT;
import server.security.ResponseOut;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
@Component
public class MyJWTFilter extends BasicHttpAuthenticationFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return executeLogin(request, response);
//        return true;
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String token = httpServletRequest.getHeader("Authorization");

        Map<String,Object> verifyTokenResultMap= JavaJWT.verifyTokenResult(token);

//        if (!JavaJWT.verifyToken(token)) {
//            ResponseOut.out401(httpServletResponse);
//            return false;
//        }
        if (!((Boolean)verifyTokenResultMap.get("result"))) {
            ResponseOut.out401(httpServletResponse,verifyTokenResultMap.get("msg").toString());
            return false;
        }

        RefreshTokenTool.refreshToken(request, response);
//        //当前token 剩余有效时间小于360分钟时，返回新的token
//        int tokenRemainingTime = JavaJWT.getTokenRemainingTime(authorization);
//        if (tokenRemainingTime != -1 && tokenRemainingTime < 360) {
//            httpServletResponse.setHeader("Authorization", JavaJWT.updateToken(authorization));
//        }
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
//        simpleAuthorizationInfo.addRoles(JavaJWT.getRoleList(authorization));
//        simpleAuthorizationInfo.addStringPermissions(JavaJWT.getPermissionList(authorization));
        AuthenticationToken authenticationToken = new AuthenticationToken() {
            @Override
            public Object getPrincipal() {
                return simpleAuthorizationInfo;     //将用户权限放置getPrincipal
            }

            @Override
            public Object getCredentials() {
                return token;               //将用户jwt放置getCredentials
            }
        };
        // Subject subject = SecurityUtils.getSubject();
        // 提交给realm进行登入，（仅在本次访问中有效，因为前后分离是无状态连接。故名为登入，实则是为此次访问填入权限），如果错误他会抛出异常并被捕获
        getSubject(request, response).login(authenticationToken);
        // System.out.println(" SecurityUtils.getSubject()==getSubject(request, response):" + (SecurityUtils.getSubject() == getSubject(request, response)));
        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }
}
