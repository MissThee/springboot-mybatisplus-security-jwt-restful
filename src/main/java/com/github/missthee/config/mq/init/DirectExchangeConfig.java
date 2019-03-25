package com.github.missthee.config.mq.init;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.github.missthee.config.mq.init.ExchangeAndQueue.*;

//direct直连模式的交换机配置,包括一个direct交换机，两个队列
@Configuration
@ConditionalOnProperty(name = "custom.rabbitmq.enable", havingValue = "true")
public class DirectExchangeConfig {

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(EXCHANGE_DIRECT);
    }

    //定义队列
//  Queue可有4个参数
//      1、队列名
//      2、durable       持久化消息队列，rabbitma重启的时候不需要重新创建队列。默认true
//      3、auto-delete   消息队列在没有使用的时候自动删除。默认false
//      4、exclusive     该消息队列是否只在当前connection生效。默认false
    @Bean
    public Queue directQueue1() {
        return new Queue(QUEUE_DIRECT1);
    }

    @Bean
    public Queue directQueue2() {
        return new Queue(QUEUE_DIRECT2);
    }

    //3个binding将交换机和相应队列连起来
    @Bean
    public Binding bindingDirectQueue1(DirectExchange directExchange, Queue directQueue1) {
        return BindingBuilder.bind(directQueue1).to(directExchange).with("orange");
    }

    @Bean
    public Binding bindingDirectQueue2(DirectExchange directExchange, Queue directQueue2) {
        return BindingBuilder.bind(directQueue2).to(directExchange).with("black");
    }

    @Bean
    public Binding bindingDirectQueue1All(DirectExchange directExchange, Queue directQueue1) {
        return BindingBuilder.bind(directQueue1).to(directExchange).with("all");
    }

    @Bean
    public Binding bindingDirectQueue2All(DirectExchange directExchange, Queue directQueue2) {
        return BindingBuilder.bind(directQueue2).to(directExchange).with("all");
    }

}
