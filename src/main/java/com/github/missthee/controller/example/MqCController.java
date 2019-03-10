package com.github.missthee.controller.example;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Slf4j
@Component
@ConditionalOnProperty(name = "custom.rabbitmq.enable", havingValue = "true")
public class MqCController {
    @RabbitListener(queues = "direct-queue1")//说明这个类是配置类，这个rabbit监听器监听name 为 tea 的消息队列。若出现多个相同监听，默认轮流处理消息。
    //    @RabbitHandler        //@RabbitListener 标注在类上面表示当有收到消息的时候，就交给 @RabbitHandler 的方法处理(此方式有时导致循环重试错误，暂未查询到有效解决方法)
    public void directQueue1(Message message, Channel channel) throws IOException {
        log.info("direct-queue1: " + new String(message.getBody()));
//        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);          // deliveryTag:该消息的index; multiple：是否批量处理.true:将一次性ack所有小于deliveryTag的消息。
//        channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);    // deliveryTag:该消息的index; multiple：是否批量.true:将一次性拒绝所有小于deliveryTag的消息。; requeue：被拒绝的是否重新入队列
//        channel.basicReject(message.getMessageProperties().getDeliveryTag(), ture);       // deliveryTag:该消息的index; requeue：被拒绝的是否重新入队列
//        ( channel.basicNack 与 channel.basicReject 的区别在于basicNack可以拒绝多条消息，而basicReject一次只能拒绝一条消息)
    }

    @RabbitListener(queues = "fanout-queue1")
    public void fanoutQueue2(Message message, Channel channel) throws IOException {
        log.info("direct-queue1: " + new String(message.getBody()));
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    @RabbitListener(queues = "topic-queue1")
    public void topicQueue1(Message message, Channel channel) throws IOException {
        log.info("direct-queue1: " + new String(message.getBody()));
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
