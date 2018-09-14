package server.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
        return true;
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String token = httpServletRequest.getHeader("Authorization");
        //若当前用户有将过期token，刷新token
        if (!StringUtils.isEmpty(token)) {
            try {
                //当前token 剩余有效时间小于360分钟时，返回新的token
                int tokenRemainingTime = JavaJWT.getTokenRemainingTime(token);
                if (tokenRemainingTime != -1 && tokenRemainingTime > 0 && tokenRemainingTime < 360) {
                    httpServletResponse.setHeader("Authorization", JavaJWT.updateToken(token));
                }
            } catch (Exception e) {
                log.error("token刷新失败！！");
                e.printStackTrace();
            }
        }

        if (!JavaJWT.verifyTokenResult(token)) {
            return false;
        } else {
            SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
            simpleAuthorizationInfo.addRoles(JavaJWT.getRoleList(token));                           //将token中携带的角色信息写入shiro身份对象
            simpleAuthorizationInfo.addStringPermissions(JavaJWT.getPermissionList(token));         //将token中携带的权限信息写入shiro身份对象
            AuthenticationToken authenticationToken = new AuthenticationToken() {
                @Override
                public Object getPrincipal() { //将用户权限放置getPrincipal
                    return simpleAuthorizationInfo;
                }

                @Override
                public Object getCredentials() { //将用户jwt放置getCredentials
                    return token;
                }
            };
            // 提交给realm进行登入，（仅在本次访问中有效，因为前后分离是无状态连接。故名为登入，实则是为此次访问填入权限），如果错误他会抛出异常并被捕获
            getSubject(request, response).login(authenticationToken);
            // System.out.println(" SecurityUtils.getSubject()==getSubject(request, response):" + (SecurityUtils.getSubject() == getSubject(request, response)));
            // 如果没有抛出异常则代表登入成功，返回true
            return true;
        }
    }
}
