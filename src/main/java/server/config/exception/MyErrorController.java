package server.config.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

//自定义错误返回（主要用于修改404错误的返回格式）
@Controller
@Slf4j
public class MyErrorController extends BasicErrorController {

    public MyErrorController(ServerProperties serverProperties) {
        super(new DefaultErrorAttributes(), serverProperties.getError());
    }

    /**
     * 覆盖默认的Json响应
     */
    @Override
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        Map<String, Object> body = getErrorAttributes(request, isIncludeStackTrace(request, MediaType.ALL));
        HttpStatus status = getStatus(request);
        Map<String, Object> map = new HashMap<>();
        String uri, eStr;
        if (status.is4xxClientError()) {
            uri = body.getOrDefault("path", "").toString();
            eStr = body.getOrDefault("error", "").toString();
            map.put("msg", body.getOrDefault("error", ""));
        } else {
            uri = "";
            eStr = body.toString();
            map.put("msg", body);
        }
        String stringBuilder =
                "\r\n-------------------↓ERROR↓--------------------" +
                        "\r\nURI    : " + (uri == null ? request.getRequestURI() : uri) +
                        "\r\nMETHOD : " + request.getMethod() +
                        "\r\nIP     : " + request.getRemoteAddr() +
                        "\r\nPARAMS : " + request.getQueryString() +
                        "\r\nERROR  : " + (eStr == null ? "" : eStr) +
                        "\r\n-------------------↑ERROR↑--------------------";
        log.error(stringBuilder);
        return new ResponseEntity<>(map, status);
    }

    /**
     * 覆盖默认的HTML响应
     */
    @Override
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        HttpStatus status = getStatus(request);
        response.setStatus(getStatus(request).value());
        Map<String, Object> model = getErrorAttributes(request, isIncludeStackTrace(request, MediaType.TEXT_HTML));
        ModelAndView modelAndView = resolveErrorView(request, response, status, model);
        return (modelAndView == null ? new ModelAndView("error", model) : modelAndView);
    }
}
