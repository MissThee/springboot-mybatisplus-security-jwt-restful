//package com.github.common.config.json;
//
//import com.alibaba.fastjson.serializer.SerializerFeature;
//import com.alibaba.fastjson.support.config.FastJsonConfig;
//import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
//import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.MediaType;
//import org.springframework.http.converter.HttpMessageConverter;
//
//import java.util.ArrayList;
//import java.util.List;
//
////fastJson配置
//@Configuration
//public class SpringFastJsonConfig {
//    @Bean
//    public HttpMessageConverters fastJsonHttpMessageConverters() {
//        FastJsonHttpMessageConverter fastConvert = new FastJsonHttpMessageConverter();
//        FastJsonConfig fastJsonConfig = new FastJsonConfig();
//        fastJsonConfig.setSerializerFeatures(
//                SerializerFeature.PrettyFormat,
////                SerializerFeature.WriteNullStringAsEmpty,
////                SerializerFeature.WriteNullListAsEmpty,
//                SerializerFeature.WriteMapNullValue,
//                SerializerFeature.DisableCircularReferenceDetect
//        );
//        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
//        //处理中文乱码问题
//        List<MediaType> fastMediaTypes = new ArrayList<>();
//        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
//        fastConvert.setSupportedMediaTypes(fastMediaTypes);
//        fastConvert.setFastJsonConfig(fastJsonConfig);
//        return new HttpMessageConverters((HttpMessageConverter<?>) fastConvert);
//    }
//}
