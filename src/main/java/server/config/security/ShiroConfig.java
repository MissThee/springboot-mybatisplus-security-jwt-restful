package server.config.security;

import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionManager;
import org.codehaus.janino.Java;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.filter.DelegatingFilterProxy;
import server.service.interf.login.LoginService;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

//    private class StatelessDefaultSubjectFactory extends DefaultWebSubjectFactory {
//        @Override
//        public Subject createSubject(SubjectContext context) {
//            //不创建session
//            context.setSessionCreationEnabled(false);
//            return super.createSubject(context);
//        }
//    }

    @Bean
    public DefaultWebSecurityManager securityManager(MyRealm myRealm,MyDefaultWebSessionManager myDefaultWebSessionManager) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(myRealm);//自定义认证器
        manager.setSessionManager(myDefaultWebSessionManager);
        manager.setRememberMeManager(null);//关闭RememberMe功能
//        manager.setSubjectFactory(new StatelessDefaultSubjectFactory());//不创建session
        return manager;
    }


    //修改后的shiro授权验证流程：
    //前端http请求 → MyJWTFilter过滤器: token验证通过，构建subject，放行；验证不通过，放行。
    // → [若请求controller带需验证注解]MyRealm
    // → 通过，放行；未通过，抛出异常。
    //注：
    //因未定制rememberMe功能，@RequireUser注解无专门对应用户，使用时作用同@RequiresAuthentication。
    @Bean
    public ShiroFilterFactoryBean shiroFilter(DefaultWebSecurityManager securityManager, JavaJWT javaJWT) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        Map<String, Filter> filterMap = new LinkedHashMap<String, Filter>() {{
            put("jwt", new MyJWTFilter(javaJWT));  // 添加自己的过滤器并且取名为jwt
        }};
        factoryBean.setFilters(filterMap);
        factoryBean.setSecurityManager(securityManager);
        Map<String, String> filterRuleMap = new LinkedHashMap<String, String>() {{
            put("/files", "anon");
            put("/**", "jwt");
        }};  //因路由拦截认证需保证设置的先后顺序，若有多个过滤规则，此处需使用可保证顺序的对象
        factoryBean.setFilterChainDefinitionMap(filterRuleMap);
        return factoryBean;
    }

    /**
     * 下面的代码是添加注解支持
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

//    /**
//     * DefaultAdvisorAutoProxyCreator，Spring的一个bean，由Advisor决定对哪些类的方法进行AOP代理。
//     */
//    @Bean
//    @DependsOn("lifecycleBeanPostProcessor")
//    @ConditionalOnMissingBean
//    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
//        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
//        // 强制使用cglib，防止重复代理和可能引起代理出错的问题
//        // https://zhuanlan.zhihu.com/p/29161098
//        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
//        return defaultAdvisorAutoProxyCreator;
//    }
//    @Bean
//    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
//        return new LifecycleBeanPostProcessor();
//    }
//
//    /**
//     * 配置shiroFilter过滤器到springboot中
//     */
//    @Bean
//    public FilterRegistrationBean<DelegatingFilterProxy> delegatingFilterProxy() {
//        FilterRegistrationBean<DelegatingFilterProxy> filterRegistrationBean = new FilterRegistrationBean<>();
//        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
//        proxy.setTargetFilterLifecycle(true);
//        proxy.setTargetBeanName("shiroFilter");
//        filterRegistrationBean.setFilter(proxy);
//        return filterRegistrationBean;
//    }
}
