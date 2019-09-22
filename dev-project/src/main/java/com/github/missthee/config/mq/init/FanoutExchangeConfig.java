package com.github.missthee.config.mq.init;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.github.missthee.config.mq.init.MqInfo.*;

//发布订阅模式的配置,包括两个队列和对应的订阅者,发布者的交换机类型使用fanout(子网广播),两根网线binding用来绑定队列到交换机
@Configuration
@ConditionalOnProperty(name = "custom.rabbitmq.enable", havingValue = "true")
public class FanoutExchangeConfig {
    //交换机
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(EXCHANGE_FANOUT);
    }

    //队列
    @Bean
    public Queue fanoutQueue1() {
        return new Queue(QUEUE_FANOUT1);
    }

    @Bean
    public Queue fanoutQueue2() {
        return new Queue(QUEUE_FANOUT2);
    }

    //绑定关系
    @Bean
    public Binding bindingFanoutQueue1(FanoutExchange fanoutExchange, Queue fanoutQueue1) {
        return BindingBuilder.bind(fanoutQueue1).to(fanoutExchange);
    }

    @Bean
    public Binding bindingFanoutQueue2(FanoutExchange fanoutExchange, Queue fanoutQueue2) {
        return BindingBuilder.bind(fanoutQueue2).to(fanoutExchange);
    }
}