package com.github.missthee;

import com.github.missthee.config.limiter.annotation.EnableRLimit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(exclude = RabbitAutoConfiguration.class)//@Configuration,@EnableAutoConfiguration,@ComponentScan
@EnableSwagger2//启用swagger2
@EnableAsync//启用异步调用，主要为开启定时任务异步及@Async注解
@EnableTransactionManagement//开启事务用的注解
@EnableRLimit //启用自定义限流注解，使@RLimit生效
public class WebApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(WebApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }
}
