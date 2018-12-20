package server.config.exception;

import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

//controller异常捕捉返回
@ApiIgnore
@RestControllerAdvice

public class ExceptionController {

    //访问无权限接口
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public Object unauthorizedException(HttpServletRequest request, Exception e) {
        e.printStackTrace();
//        ErrorLogPrinter.logOutPut(request, e);
        JSONObject jO = new JSONObject();
        jO.put("msg","UnauthorizedException:"+ HttpStatus.FORBIDDEN.getReasonPhrase());
        return jO;
    }

    //需要登录
    @ExceptionHandler(UnauthenticatedException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public Object unauthenticatedException(HttpServletRequest request, Exception e) {
        e.printStackTrace();
//        ErrorLogPrinter.logOutPut(request, e);
        JSONObject jO = new JSONObject();
        jO.put("msg","UnauthenticatedException:"+ HttpStatus.UNAUTHORIZED.getReasonPhrase());
        return jO;
    }

    //参数格式错误
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public Object httpMessageNotReadableException(HttpServletRequest request, Exception e) {
        e.printStackTrace();
//        ErrorLogPrinter.logOutPut(request, e);
        JSONObject jO = new JSONObject();
        jO.put("msg","HttpMessageNotReadableException: 参数格式有误，请检查参数格式。"+ e);
        return jO;
    }

    //运行时所有异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public Object exceptionHandler(HttpServletRequest request, Exception e) {
        e.printStackTrace();
//        ErrorLogPrinter.logOutPut(request, e);
        JSONObject jO = new JSONObject();
        jO.put("msg", "Exception: " + e);
        return jO;
    }
}