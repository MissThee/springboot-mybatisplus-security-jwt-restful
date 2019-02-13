package server.config.security;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import server.db.primary.dto.login.LoginDTO;
import server.service.interf.login.LoginService;

@Component
public class MyRealm extends AuthorizingRealm {
    private final LoginService loginService;

    @Autowired
    public MyRealm(LoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * 身份信息是否是支持的类型
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return true;
    }

    /**
     * Authentication认证   Authorization授权
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        return new SimpleAuthenticationInfo(auth.getPrincipal(), "", getName());
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String token = principals.getPrimaryPrincipal().toString();
//          于缓存中获取用户信息
        LoginDTO loginDTO = loginService.selectUserById(Integer.parseInt(JavaJWT.getId(token)));
        return new SimpleAuthorizationInfo() {{
            addRoles(loginDTO.getRoleValueList());
            addStringPermissions(loginDTO.getPermissionValueList());
        }};
    }
}
