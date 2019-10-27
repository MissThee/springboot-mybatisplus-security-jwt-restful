1. 下载RabbitMQ服务端，https://www.rabbitmq.com/
2. 项目添加依赖
    ```xml
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-amqp</artifactId>
       </dependency>
    ```
3. 项目添加配置文件
    ```properties
       #自定义启用/禁用rabbitmq。注意！！
       # true 时：需将启动类中的 exclude = RabbitAutoConfiguration.class 删除，否则无法启动
       # false时：启动类中的 exclude = RabbitAutoConfiguration.class 可不添加，但启动后health check会抛出一个连接异常，不影响服务运行
       custom.rabbitmq.enable=false
       spring.rabbitmq.host=localhost
       spring.rabbitmq.port=5672
       spring.rabbitmq.username=demo
       spring.rabbitmq.password=demo
       #每个不同的virtual-host是相互隔离的，virtual-host需提前在rabbitmq中创建
       spring.rabbitmq.virtual-host=/
       #若拒绝连接，可在 AppData\Roaming\RabbitMQ\advanced.config 设置 [{rabbit,[{loopback_users,[demo]}]}].
       #http://localhost:15672默认查看控制台,自行添加用户demo
    ```
4. 添加配置Bean、初始队列、路由等配置文件
    