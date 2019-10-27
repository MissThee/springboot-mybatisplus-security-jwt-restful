//package com.github.missthee.config.security.shiro;
//
//import com.github.missthee.config.security.jwt.JavaJWT;
//import org.apache.shiro.spring.LifecycleBeanPostProcessor;
//import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
//import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
//import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
//import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
//import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.DependsOn;
//import org.springframework.web.filter.DelegatingFilterProxy;
//
//import javax.servlet.Filter;
//import java.util.LinkedHashMap;
//
//@Configuration
//public class ShiroConfig {
//
//    @Bean
//    public DefaultWebSecurityManager securityManager(MyRealm myRealm) {
//        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
//        manager.setRealm(myRealm);//自定义认证器
//        manager.setSessionManager(new DefaultWebSessionManager() {
//            @Override
//            public boolean isServletContainerSessions() {
//                return true;
//            }
//
//            {
//                setSessionIdCookieEnabled(false);
//                setSessionValidationSchedulerEnabled(false);
//                setSessionIdUrlRewritingEnabled(false);
//            }
//        });
//        manager.setRememberMeManager(null);//关闭RememberMe功能
//        return manager;
//    }
//
//    //修改后的shiro授权验证流程：
//    //前端http请求 → MyJWTFilter过滤器: token验证通过，构建subject，放行；验证不通过，放行。
//    // → [若请求controller带需验证注解]MyRealm，验证subject
//    // → 通过，放行；未通过，抛出异常(未登录或无权限)。
//    //注：
//    //因未定制rememberMe功能，@RequireUser注解无专门对应用户，使用时作用同@RequiresAuthentication。
//    @Bean
//    public ShiroFilterFactoryBean shiroFilter(DefaultWebSecurityManager securityManager, JavaJWT javaJWT) {
//        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
//        LinkedHashMap<String, Filter> filterMap = new LinkedHashMap<String, Filter>() {{
//            put("jwt", new MyJWTFilter(javaJWT));  // 添加自己的过滤器并且取名为jwt
//        }};
//        factoryBean.setFilters(filterMap);
//        factoryBean.setSecurityManager(securityManager);
//        LinkedHashMap<String, String> filterRuleMap = new LinkedHashMap<String, String>() {{
//            put("/files/**", "anon");
//            put("/**", "jwt");
//        }};
//        factoryBean.setFilterChainDefinitionMap(filterRuleMap);
//        factoryBean.setLoginUrl("/login");
//        return factoryBean;
//    }
//
//    /**
//     * 下面的代码是添加注解支持
//     */
//    @Bean
//    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
//        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
//        advisor.setSecurityManager(securityManager);
//        return advisor;
//    }
////
//
////    /**
////     * DefaultAdvisorAutoProxyCreator，Spring的一个bean，由Advisor决定对哪些类的方法进行AOP代理。
////     */
////    @Bean
////    @DependsOn("lifecycleBeanPostProcessor")
////    @ConditionalOnMissingBean
////    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
////        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
////        // 强制使用cglib，防止重复代理和可能引起代理出错的问题
////        // https://zhuanlan.zhihu.com/p/29161098
////        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
////        return defaultAdvisorAutoProxyCreator;
////    }
//
////    @Bean
////    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
////        return new LifecycleBeanPostProcessor();
////    }
////
////    /**
////     * 配置shiroFilter过滤器到springboot中
////     */
////    @Bean
////    public FilterRegistrationBean<DelegatingFilterProxy> delegatingFilterProxy() {
////        FilterRegistrationBean<DelegatingFilterProxy> filterRegistrationBean = new FilterRegistrationBean<>();
////        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
////        proxy.setTargetFilterLifecycle(true);
////        proxy.setTargetBeanName("shiroFilter");
////        filterRegistrationBean.setFilter(proxy);
////        return filterRegistrationBean;
////    }
//}
