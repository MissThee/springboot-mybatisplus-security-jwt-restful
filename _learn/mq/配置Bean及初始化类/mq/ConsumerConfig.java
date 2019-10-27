package com.github.missthee.config.mq;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "custom.rabbitmq.enable", havingValue = "true")
public class ConsumerConfig {
    //配置此bean会覆盖yml中配置
    @Bean
    public RabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(1);//消费端最小并发数    spring.rabbitmq.listener.simple.concurrency=1
        factory.setMaxConcurrentConsumers(1);//消费端最大并发数 spring.rabbitmq.listener.simple.max-concurrency=1
        factory.setPrefetchCount(1);//消费端一次请求中预处理的消息数量  spring.rabbitmq.listener.simple.prefetch=1
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);//采用手动应答 spring.rabbitmq.listener.simple.acknowledge-mode=manual
        factory.setMessageConverter(new Jackson2JsonMessageConverter());//使用此转换器做反序列化，对象必须有空构造器
        return factory;
    }


}
