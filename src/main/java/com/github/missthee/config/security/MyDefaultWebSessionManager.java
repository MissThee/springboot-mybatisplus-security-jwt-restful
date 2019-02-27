package server.config.security;

import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.stereotype.Component;

@Component
public class MyDefaultWebSessionManager extends DefaultWebSessionManager {
    MyDefaultWebSessionManager() {
        setSessionIdCookieEnabled(false);
        setSessionValidationSchedulerEnabled(false);
        setSessionIdUrlRewritingEnabled(false);
    }

    //设置不让Shiro把request包装成ShiroHttpServletRequest
    @Override
    public boolean isServletContainerSessions() {
        return true;
    }
}
