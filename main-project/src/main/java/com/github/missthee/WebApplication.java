package com.github.missthee;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
@SpringBootApplication//相当于@Configuration,@EnableAutoConfiguration和 @ComponentScan 并具有他们的默认属性值
//@EnableAsync//启用异步调用
public class WebApplication  extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(WebApplication.class);
    }
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }
}
