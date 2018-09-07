package server.security;

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
     * 判断login方法参数中的认证信息类型是否支持
     * login方法中使用的是AuthenticationToken
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
//        String token = (String) auth.getCredentials();
//        String userId = JavaJWT.getId(token);
//        if (userId == null) {
//            throw new AuthenticationException("token invalid");// 抛出shiro的异常，统一由shiro异常捕获方法进行处理
//        }
//        ShiroUser user = userService.getUserById(Integer.parseInt(userId));
//        if (user == null) {
//            throw new AuthenticationException("user didn't existed!");
//        }
//
//        if (!JavaJWT.verifyToken(token, user.getPassword())) {
//            throw new AuthenticationException("password error or changed");
//        }
//        return new SimpleAuthenticationInfo(shiroUser.getLoginId(), shiroUser.getLoginId(), getName());
        return new SimpleAuthenticationInfo(auth.getPrincipal(), auth.getCredentials(), getName());
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
//        String userId = JavaJWT.getId(principals.toString());
//        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
//        if (!StringUtils.isNullOrEmpty(userId)) {
//            ShiroUser shiroUser = userService.getUserById(Integer.parseInt(userId));
//            List<ShiroRole> roleList = shiroUser.getRoles();
//            for (ShiroRole role : roleList) {
//                simpleAuthorizationInfo.addRole(role.getRole());
//                for (ShiroPermission permission : role.getPermissions()) {
//                    simpleAuthorizationInfo.addStringPermission(permission.getPermission());
//                }
//            }
//        }

        return (SimpleAuthorizationInfo)(principals.getPrimaryPrincipal());
    }
}
