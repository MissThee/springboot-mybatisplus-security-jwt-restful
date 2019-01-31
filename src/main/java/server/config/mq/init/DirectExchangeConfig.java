package server.config.mq.init;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//direct直连模式的交换机配置,包括一个direct交换机，两个队列，三根网线binding
@Configuration
public class DirectExchangeConfig {

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("exchange-direct");
    }

    @Bean
    public Queue directQueue1() {
        return new Queue("direct-queue1");
    }

    @Bean
    public Queue directQueue2() {
        return new Queue("direct-queue2");
    }

    //3个binding将交换机和相应队列连起来
    @Bean
    public Binding bindingdDirect1() {
        return BindingBuilder.bind(directQueue1()).to(directExchange()).with("orange");
    }

    @Bean
    public Binding bindingDirect2() {
        return BindingBuilder.bind(directQueue2()).to(directExchange()).with("black");
    }

    @Bean
    public Binding bindingDirect1All() {
        return BindingBuilder.bind(directQueue1()).to(directExchange()).with("all");
    }

    @Bean
    public Binding bindingDirect2All() {
        return BindingBuilder.bind(directQueue2()).to(directExchange()).with("all");
    }

}
