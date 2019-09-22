package com.github.missthee.config.mq.init;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.github.missthee.config.mq.init.MqInfo.*;

//topic交换机模型，需要一个topic交换机，两个队列和三个binding
@Configuration
@ConditionalOnProperty(name = "custom.rabbitmq.enable", havingValue = "true")
public class TopicExchangeConfig {
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(EXCHANGE_TOPIC);
    }

    @Bean
    public Queue topicQueue1() {
        return new Queue(QUEUE_TOPIC1);
    }

    @Bean
    public Queue topicQueue2() {
        return new Queue(QUEUE_TOPIC2);
    }

    //binding将交换机和相应队列连起来
    @Bean
    public Binding bindingTopicQueue1(TopicExchange topicExchange, Queue topicQueue1) {
        return BindingBuilder.bind(topicQueue1).to(topicExchange).with("*.orange");
    }

    @Bean
    public Binding bindingTopicQueue2(TopicExchange topicExchange, Queue topicQueue2) {
        return BindingBuilder.bind(topicQueue2).to(topicExchange).with("color.black");
    }
}