package server.config.mq.init;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//发布订阅模式的配置,包括两个队列和对应的订阅者,发布者的交换机类型使用fanout(子网广播),两根网线binding用来绑定队列到交换机
@Configuration
public class FanoutExchangeConfig {
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("exchange-fanout");
    }
    @Bean
    public Queue myQueue1() {
        return new Queue("fanout-queue1");
    }

    @Bean
    public Queue myQueue2() {
        return new Queue("fanout-queue2");
    }



    @Bean
    public Binding bindingFanout1() {
        return BindingBuilder.bind(myQueue1()).to(fanoutExchange());
    }

    @Bean
    public Binding bindingFanout2() {
        return BindingBuilder.bind(myQueue2()).to(fanoutExchange());
    }

}