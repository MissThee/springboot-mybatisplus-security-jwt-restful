package server.security;

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

//    public DefaultWebSessionManager MyWebSessionManager() {
//        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
//        defaultWebSessionManager.setSessionValidationSchedulerEnabled(false);
//        defaultWebSessionManager.setSessionIdCookieEnabled(false);
//        defaultWebSessionManager.setSessionIdUrlRewritingEnabled(false);
//        return defaultWebSessionManager;
//    }

}
