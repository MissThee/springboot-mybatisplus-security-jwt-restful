1. 创建src/main/resources/META-INF/spring.factories文件
2. 增加配置，指向自己的配置bean  
    ```
    org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
    com.jar.demo.config
    ```