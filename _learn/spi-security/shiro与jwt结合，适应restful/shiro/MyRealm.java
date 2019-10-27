//package com.github.missthee.config.security.shiro;
//
//import org.apache.shiro.authc.AuthenticationException;
//import org.apache.shiro.authc.AuthenticationInfo;
//import org.apache.shiro.authc.AuthenticationToken;
//import org.apache.shiro.authc.SimpleAuthenticationInfo;
//import org.apache.shiro.authz.AuthorizationInfo;
//import org.apache.shiro.realm.AuthorizingRealm;
//import org.apache.shiro.subject.PrincipalCollection;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class MyRealm extends AuthorizingRealm {
//    private final UserInfoForShiro userInfoForShiro;
//
//    @Autowired
//    public MyRealm(UserInfoForShiro userInfoForShiro) {
//        this.userInfoForShiro = userInfoForShiro;
//    }
//
//    /**
//     * 身份信息是否是支持的类型
//     */
//    @Override
//    public boolean supports(AuthenticationToken token) {
//        return true;
//    }
//
//    /**
//     * 登录验证
//     */
//    @Override
//    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
//        return new SimpleAuthenticationInfo(auth.getPrincipal(), auth.getCredentials(), getName());
//    }
//
//    /**
//     * 角色权限验证
//     */
//    @Override
//    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
//        //获取用户信息(尽量由缓存中获取，避免频繁读取数据库)
//        String userId = principals.getPrimaryPrincipal().toString();
//        return userInfoForShiro.getSimpleAuthorizationInfo(userId);
//    }
//}
