//package server.config;
//
//import com.alibaba.fastjson.serializer.JavaBeanSerializer;
//import com.alibaba.fastjson.serializer.SerializerFeature;
//import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
//import com.alibaba.fastjson.support.config.FastJsonConfig;
//import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
//import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.MediaType;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Configuration
//public class SpringJsonConfig {
//    /**
//     * 替换框架json为fastjson
//     */
//    @Bean
//    public HttpMessageConverters fastjsonHttpMessageConverter() {
//        //1.需要定义一个convert转换消息的对象;
//        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
//        //2:添加fastJson的配置信息;
//        FastJsonConfig fastJsonConfig = new FastJsonConfig();
//        fastJsonConfig.setSerializerFeatures(//序列化设置
////                SerializerFeature.WriteNonStringKeyAsString,
////                SerializerFeature.WriteMapNullValue,     //输出null值
//                SerializerFeature.WriteNullListAsEmpty,  //输出List的null值为[]
//                SerializerFeature.WriteDateUseDateFormat, //设置时间格式为 yyyy-MM-dd HH:mm:ss
//                SerializerFeature.UseISO8601DateFormat
//        );
//        fastJsonConfig.setSerializeFilters();
//        //3处理中文乱码问题
//        List<MediaType> fastMediaTypes = new ArrayList<>();
//        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
//        //4.在convert中添加配置信息.
//        fastJsonHttpMessageConverter.setSupportedMediaTypes(fastMediaTypes);
//        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
//        return new HttpMessageConverters(fastJsonHttpMessageConverter);
//    }
//
//}
//fastJson过滤Date类型null值有bug，暂使用jackson