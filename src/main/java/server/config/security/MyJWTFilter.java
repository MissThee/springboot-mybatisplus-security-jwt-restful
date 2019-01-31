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
    private final LoginService loginService;

    @Autowired
    public MyJWTFilter(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        executeLogin(request, response);
        return true;//使匿名用户可以通过验证
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
                long tokenRemainingTime = JavaJWT.getTokenRemainingTime(token);
                log.debug(String.valueOf(tokenRemainingTime));
                if (tokenRemainingTime >= 0 && tokenRemainingTime <= 360) {
                    httpServletResponse.setHeader("Authorization", JavaJWT.updateToken(token, 1));
                }
            } catch (Exception e) {
                log.error("token刷新失败！！");
                e.printStackTrace();
            }
        }


        if (StringUtils.isEmpty(token) || !JavaJWT.verifyToken(token)) {
            return false;
        } else {
            SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
//          于缓存中获取用户信息
            LoginDTO loginDTO = loginService.selectUserById(Integer.parseInt(JavaJWT.getId(token)));
            simpleAuthorizationInfo.addRoles(loginDTO.getRoleValueList());
            simpleAuthorizationInfo.addStringPermissions(loginDTO.getPermissionValueList());

//          使用token中存储的角色、权限信息，需在登录时将相关信息写入token中
//            simpleAuthorizationInfo.addRoles(JavaJWT.getRoleList(token));                           //将token中携带的角色信息写入shiro身份对象
//            simpleAuthorizationInfo.addStringPermissions(JavaJWT.getPermissionList(token));         //将token中携带的权限信息写入shiro身份对象
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
