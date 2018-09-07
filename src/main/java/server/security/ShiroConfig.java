package server.security;

import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import server.security.Filter.MyJWTFilter;
import server.security.Filter.RefreshTokenFilter;
//import server.shiro.myfunction.MySessionManager;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Bean("securityManager")
    public DefaultWebSecurityManager getManager() {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(new MyRealm());
        manager.setSessionManager(new MyWebSessionManager());
        return manager;
    }

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean factory(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        // 添加自己的过滤器并且取名为jwt
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("jwt", new MyJWTFilter());
        filterMap.put("refreshToken", new RefreshTokenFilter());//token刷新。有token且快到期时，刷新token；无token，或token已过期无动作。
        factoryBean.setFilters(filterMap);
        factoryBean.setSecurityManager(securityManager);
        Map<String, String> filterRuleMap = new LinkedHashMap<>();

        //可在此处添加路由拦截，若无需身份认证则也无法获取当前操作的用户信息
        //jwt  ： 需登录访问
        filterRuleMap.put("/auth/**", "jwt");//             /main/路径的请求需通过验证

        //系统管理相关接口加入身份验证
        filterRuleMap.put("/group/**", "jwt");
        filterRuleMap.put("/obj/**", "jwt");
        filterRuleMap.put("/role/**", "jwt");
        filterRuleMap.put("/user/**", "jwt");
        filterRuleMap.put("/meterStation/**", "jwt");
        filterRuleMap.put("/valve/**", "jwt");
        filterRuleMap.put("/waterwell/**", "jwt");
        filterRuleMap.put("/area/**", "jwt");
        filterRuleMap.put("/factory/**", "jwt");
        filterRuleMap.put("/unionStation/**", "jwt");
        filterRuleMap.put("/wellDataMaintian/**", "jwt");
        filterRuleMap.put("/wellGtConfig/**", "jwt");
        filterRuleMap.put("/wellInfo/**", "jwt");
        filterRuleMap.put("/wellPipe/**", "jwt");
//        filterRuleMap.put("/reportForm/**", "jwt");
        filterRuleMap.put("/**","refreshToken");

        factoryBean.setFilterChainDefinitionMap(filterRuleMap);
        return factoryBean;
    }

    /**
     * 下面的代码是添加注解支持
     */
//    @Bean
//    @DependsOn("lifecycleBeanPostProcessor")
//    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
//        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
//        // 强制使用cglib，防止重复代理和可能引起代理出错的问题
//        // https://zhuanlan.zhihu.com/p/29161098
//        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
//        return defaultAdvisorAutoProxyCreator;
//    }
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    /**
     * DefaultAdvisorAutoProxyCreator，Spring的一个bean，由Advisor决定对哪些类的方法进行AOP代理。
     */
    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
        defaultAAP.setProxyTargetClass(true);
        return defaultAAP;
    }
}
