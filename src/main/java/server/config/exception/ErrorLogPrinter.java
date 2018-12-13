package server.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
//日志输出工具类
@Slf4j
@Component
public class ErrorLogPrinter {
    private static String mode ;

    @Value("${custom-config.log.mode: }")
    public void setRootPath(String a) {
        mode = a;
    }

    static void logOutPut(HttpServletRequest request, Exception e) {
        StringBuilder logSB = new StringBuilder();
        logSB.append("[ERROR]");
        logSB.append("; URL:").append(request.getRequestURL());
        logSB.append("; METHOD:").append(request.getMethod());
        logSB.append("; URI:").append(request.getRequestURI());
        logSB.append("; IP:").append(request.getRemoteAddr());
        logSB.append("; PARAMS:").append(request.getQueryString());
        if (e != null) {
            logSB.append("; ERROR:").append(e);
        }
        log.warn(logFormat(logSB));
    }

    static void logOutPut(HttpServletRequest request) {
        logOutPut(request, null);
    }

    private static String logFormat(StringBuilder logSB) {
        String logStr;
        if (mode.equals("dev")) {
            logStr = logSB.insert(0, "\n").toString().replaceAll(";", "\n");
        } else {
            logStr = logSB.toString();
        }
        return logStr;
    }
}
