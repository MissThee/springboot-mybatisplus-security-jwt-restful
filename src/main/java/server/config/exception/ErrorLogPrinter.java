package server.config.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

//日志输出工具类
@Slf4j
@Component
public class ErrorLogPrinter {
    private static String mode;

    @Value("${custom-config.log.mode: }")
    public void setRootPath(String a) {
        mode = a;
    }

    static void logOutPut(HttpServletRequest request, Exception e) {
        logOutPut(request, null, e.toString());
    }

    static void logOutPut(HttpServletRequest request, String e) {
        logOutPut(request, null, e);
    }

    static void logOutPut(HttpServletRequest request, String url, String eStr) {
        if (mode.equals("release")) {
            String logSB = "[ERROR]" +
                    "; URI:" + (url == null ? request.getRequestURI() : url) +
                    "; METHOD:" + request.getMethod() +
                    "; IP:" + request.getRemoteAddr() +
                    "; PARAMS:" + request.getQueryString() +
                    "; ERROR:" + (eStr == null ? "" : eStr);
            log.error(logSB);
        } else {
            String stringBuilder =
                    "\r\n-------------------↓ERROR↓--------------------" +
                    "\r\nURI    : " + (url == null ? request.getRequestURI() : url) +
                    "\r\nMETHOD : " + request.getMethod() +
                    "\r\nIP     : " + request.getRemoteAddr() +
                    "\r\nPARAMS : " + request.getQueryString() +
                    "\r\nERROR  : " + (eStr == null ? "" : eStr) +
                    "\r\n-------------------↑ERROR↑--------------------";
            log.error(stringBuilder);
        }
    }
}
