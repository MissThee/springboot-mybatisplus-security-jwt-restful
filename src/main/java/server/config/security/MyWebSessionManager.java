package server.config.security;

import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MyWebSessionManager extends DefaultWebSessionManager implements BeanPostProcessor {
   public MyWebSessionManager(){
        super();
        setSessionValidationSchedulerEnabled(false);
        setSessionIdCookieEnabled(false);
        setSessionIdUrlRewritingEnabled(false);
    }
}
