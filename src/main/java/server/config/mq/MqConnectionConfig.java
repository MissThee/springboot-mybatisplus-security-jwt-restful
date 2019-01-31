package server.config.mq;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqConnectionConfig {
    private static String host;
    private static int port;
    private static String username;
    private static String password;
    private static String virtualHost;

    @Value("${mq.rabbit.host}")
    public void setHost(String a) {
        host = a;
    }

    @Value("${mq.rabbit.port}")
    public void setPort(int a) {
        port = a;
    }

    @Value("${mq.rabbit.username}")
    public void setUsername(String a) {
        username = a;
    }

    @Value("${mq.rabbit.password}")
    public void setPassword(String a) {
        password = a;
    }

    @Value("${mq.rabbit.virtualHost}")
    public void setVirtualHost(String a) {
        virtualHost = a;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host, port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
//        自动创建的ConnectionFactory无法完成事件的回调，即没有设置下面的代码
        connectionFactory.setPublisherConfirms(true);
        return connectionFactory;
    }
}
