package server.config.security;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import org.springframework.stereotype.Component;

@Component
public class MyRealm extends AuthorizingRealm {

    /**
     * 身份信息是否是支持的类型
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token != null;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。验证role或permisson时先调用此方法，再调用doGetAuthorizationInfo
     * Authentication认证   Authorization授权
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        return new SimpleAuthenticationInfo(auth.getPrincipal(), auth.getCredentials(), getName());
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return (SimpleAuthorizationInfo)(principals.getPrimaryPrincipal());
    }
}
