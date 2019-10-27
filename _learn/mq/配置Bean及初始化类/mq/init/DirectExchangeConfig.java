package com.github.missthee.config.mq.init;

import org.springframework.amqp.core.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static com.github.missthee.config.mq.init.MqInfo.*;

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
//  name          队列名
//  durable       持久化消息队列，rabbitmq重启的时候不需要重新创建队列。默认true
//  auto-delete   消息队列在没有使用的时候自动删除。默认false
//  exclusive     该消息队列是否只在当前connection生效。默认false
    @Bean
    public Queue directQueue1() {
        Map<String, Object> args = new HashMap<String, Object>() {{
            put("x-dead-letter-exchange", EXCHANGE_DIRECT_DL);
            put("x-dead-letter-routing-key", DEAD_LETTER_ROUTING);
        }};
        return QueueBuilder.durable(QUEUE_DIRECT1).withArguments(args).build();
    }

    @Bean
    public Queue directQueue2() {
        Map<String, Object> args = new HashMap<String, Object>() {{
            put("x-dead-letter-exchange", EXCHANGE_DIRECT_DL);
            put("x-dead-letter-routing-key", DEAD_LETTER_ROUTING);
        }};
        return QueueBuilder.durable(QUEUE_DIRECT2).withArguments(args).build();
//        return new Queue(QUEUE_DIRECT2);
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
        return BindingBuilder.bind(directQueue1).to(directExchange).with("allQueue");
    }

    @Bean
    public Binding bindingDirectQueue2All(DirectExchange directExchange, Queue directQueue2) {
        return BindingBuilder.bind(directQueue2).to(directExchange).with("allQueue");
    }

    //死信交换机
    @Bean
    public DirectExchange directExchangeDL() {
        return new DirectExchange(EXCHANGE_DIRECT_DL);
    }

    //死信队列
    @Bean
    public Queue directQueueDL() {
        return QueueBuilder.durable(QUEUE_DIRECT1_DL).build();
    }

    //死信交换机绑定死信队列
    @Bean
    public Binding bindingDirectQueueDLDL(DirectExchange directExchangeDL, Queue directQueueDL) {
        return BindingBuilder.bind(directQueueDL).to(directExchangeDL).with(DEAD_LETTER_ROUTING);
    }
    //在以下3种情况会进入死信队列
    //1.有消息被拒绝（basic.reject/ basic.nack）并且requeue=false
    //2.队列达到最大长度
    //3.消息TTL过期
    //死信队列、死信交换机与普通队列无需使用binding 绑定，由args中设置的"x-dead-letter-exchange"，"x-dead-letter-routing-key"确定死信转发规则
}
