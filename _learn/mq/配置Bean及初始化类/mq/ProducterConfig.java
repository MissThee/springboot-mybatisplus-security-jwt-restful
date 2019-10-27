package com.github.missthee.config.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.HashMap;

@Slf4j
@Configuration
@ConditionalOnProperty(name = "custom.rabbitmq.enable", havingValue = "true")
public class ProducterConfig {
    @Bean(name = "rabbitRetry")
    public RetryTemplate attemptsRetry() {
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(new SimpleRetryPolicy(3, new HashMap<Class<? extends Throwable>, Boolean>() {{
            //设置重试异常和是否重试
            put(AmqpException.class, true);
        }}));
        retryTemplate.setBackOffPolicy(new FixedBackOffPolicy() {{
            //设置重试间隔 mills
            setBackOffPeriod(5000);
        }});
        return retryTemplate;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)//若为每个类中引用的RabbitTemplate单独配置回调方法，需使用PROTOTYPE，每次创建新的RabbitTemplate对象
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,RetryTemplate attemptsRetry) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);//生产者发送的消息，已到exchange，但无法到queue的消息，回退给生产者，而不是丢弃   spring.rabbitmq.template.mandatory=true
        rabbitTemplate.setRetryTemplate(attemptsRetry);//生产者支持重试
        // 以下回调均需要设置publisher-returns: true，作用为：若消息发送到交换机失败，返回到队列中不被丢弃
        //消息发送到exchange，但无法发送到任何queue时，将消息返回给生产者时，触发的事件
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                log.debug("TO EXCHANGE[SUCCESS]: correlationData: {}", correlationData);
            } else {
                log.warn("TO EXCHANGE[FAILURE]: cause: {}", cause);
            }
        });
        //mandatory:true 使无法发送到queue的消息，返回，而不是丢弃。固此值为true时才能触发setReturnCallback。已在yml中配置
        //消息发送到exchange，但无法发送到任何queue时，将消息返回给生产者时，触发的事件
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            String correlationId = message.getMessageProperties().getCorrelationId();
            log.warn("TO QUEUE[FAILURE]：message: {}; replyCode：{}; replyText：{}; exchange: {};  routingKey: {}", correlationId, replyCode, replyText, exchange, routingKey);
        });
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
}
