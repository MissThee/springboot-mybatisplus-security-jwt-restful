package com.github.missthee.config.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ConditionalOnProperty(name = "custom.rabbitmq.enable", havingValue = "true")
public class MqProductor {

    private final ConnectionFactory connectionFactory;

    @Autowired
    public MqProductor(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    /**
     * 定制amqp模版
     * ConfirmCallback接口用于实现   即消息发送到exchange  ack
     * ReturnCallback接口用于实现消息发送到RabbitMQ 交换器，但无相应队列与交换器绑定时的回调  即消息发送不到任何一个队列中  ack
     */
    @Bean
    public RabbitTemplate rabbitTemplate() {
        // 以下回调均需要设置publisher-returns: true
        // 作用为：若消息发送到交换机失败，返回到队列中不被丢弃

        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        // 使用return-callback时必须设置mandatory为true
        // 消息发送到RabbitMQ 交换器，但无相应队列与交换器绑定时的回调
        rabbitTemplate.setMandatory(true);
        //当mandatory标志位设置为true时，如果exchange根据自身类型和消息routingKey无法找到一个合适的queue存储消息，那么broker会调用basic.return方法将消息返还给生产者;当mandatory设置为false时，出现上述情况broker会直接将消息丢弃;通俗的讲，mandatory标志告诉broker代理服务器至少将消息route到一个队列中，否则就将消息return给发送者;
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            String correlationId = message.getMessageProperties().getCorrelationId();
            log.debug("TO QUEUE[FAILURE]：message: {}; replyCode：{}; replyText：{}; exchange: {};  routingKey: {}", correlationId, replyCode, replyText, exchange, routingKey);
        });

        //消息发送到RabbitMQ交换器后接收ack回调
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                log.debug("TO EXCHANGE[SUCCESS]: correlationData: {}", correlationData);
            } else {
                log.debug("TO EXCHANGE[FAILURE]: cause: {}", cause);
            }
        });
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
}
