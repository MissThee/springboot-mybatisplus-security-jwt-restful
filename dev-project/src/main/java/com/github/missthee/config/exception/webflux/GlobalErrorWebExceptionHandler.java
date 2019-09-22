//package com.github.missthee.config.exception.webflux;
//
//
//import org.springframework.boot.autoconfigure.web.ResourceProperties;
//import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
//import org.springframework.boot.web.reactive.error.ErrorAttributes;
//import org.springframework.context.ApplicationContext;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.MediaType;
//import org.springframework.http.codec.ServerCodecConfigurer;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.BodyInserters;
//import org.springframework.web.reactive.function.server.*;
//import reactor.core.publisher.Mono;
//
//import java.util.HashMap;
//import java.util.Map;
////webflux异常处理
//@Component
//@Order(-2)
//public class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {
//
//    public GlobalErrorWebExceptionHandler(GlobalErrorAttributes globalErrorAttributes, ApplicationContext applicationContext,
//                                          ServerCodecConfigurer serverCodecConfigurer) {
//        super(globalErrorAttributes, new ResourceProperties(), applicationContext);
//        super.setMessageWriters(serverCodecConfigurer.getWriters());
//        super.setMessageReaders(serverCodecConfigurer.getReaders());
//    }
//
//    @Override
//    protected RouterFunction<ServerResponse> getRoutingFunction(final ErrorAttributes errorAttributes) {
//        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
//    }
//
//    private Mono<ServerResponse> renderErrorResponse(final ServerRequest request) {
//
//        final Map<String, Object> errorPropertiesMap = getErrorAttributes(request, false);
//        int status = Integer.parseInt(errorPropertiesMap.getOrDefault("status", 500).toString());
//        return ServerResponse
//                .status(status)
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .body(BodyInserters.fromObject(new HashMap<String, Object>() {{
//                    put("msg", errorPropertiesMap.get("message"));
//                }}));
//    }
//
//}