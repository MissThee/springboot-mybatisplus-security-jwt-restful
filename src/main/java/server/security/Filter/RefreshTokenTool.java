package server.security.Filter;

import org.springframework.util.StringUtils;
import server.security.JavaJWT;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RefreshTokenTool {
    static void refreshToken(ServletRequest servletRequest, ServletResponse servletResponse) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String token = httpServletRequest.getHeader("Authorization");
        if (!StringUtils.isEmpty(token)) {
            try {
                //当前token 剩余有效时间小于360分钟时，返回新的token
                int tokenRemainingTime = JavaJWT.getTokenRemainingTime(token);
                if (tokenRemainingTime != -1 && tokenRemainingTime < 360) {
                    httpServletResponse.setHeader("Authorization", JavaJWT.updateToken(token));
                }
            } catch (Exception e) {
                System.out.println("~~~~token刷新失败~~~~~");
                e.printStackTrace();
            }
        }
    }
}
