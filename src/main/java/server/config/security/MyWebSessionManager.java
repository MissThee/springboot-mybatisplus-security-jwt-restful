package server.config.security;

import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.stereotype.Component;

@Component
public class MyWebSessionManager extends DefaultWebSessionManager {
   public MyWebSessionManager(){
        super();
        setSessionValidationSchedulerEnabled(false);
        setSessionIdCookieEnabled(false);
        setSessionIdUrlRewritingEnabled(false);
    }
}
