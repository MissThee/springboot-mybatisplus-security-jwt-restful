package com.github.common.config.exception;

import com.github.common.config.log.builder.LogBuilder;
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
import java.util.*;

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
        Map<String, Object> resultMap = new HashMap<>();
        String uri, eStr;
        if (status.is4xxClientError()) {
            uri = String.valueOf(body.getOrDefault("path", ""));
            eStr = String.valueOf(body.getOrDefault("error", ""));
            resultMap.put("msg", body.getOrDefault("error", ""));
        } else {
            uri = "";
            eStr = String.valueOf(body);
            resultMap.put("msg", body);
        }
        String str = LogBuilder.requestLogBuilder(request, new Exception(eStr), new HashMap<String, Object>() {{
            put("PATH", uri);
        }});
        log.error(str);
        return new ResponseEntity<>(resultMap, status);
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
