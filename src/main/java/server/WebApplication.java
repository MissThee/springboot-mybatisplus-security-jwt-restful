package server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication//相当于@Configuration,@EnableAutoConfiguration和 @ComponentScan 并具有他们的默认属性值
@EnableSwagger2//启用swagger2
@EnableAsync//启用异步调用
public class WebApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }
}
