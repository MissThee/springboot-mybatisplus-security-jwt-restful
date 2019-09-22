package com.github.missthee.controller.example.mq;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.github.missthee.config.mq.init.MqInfo.*;


@Slf4j
@Component
@ConditionalOnProperty(name = "custom.rabbitmq.enable", havingValue = "true")
public class MqCController {
    private boolean aParam = false;
    @RabbitListener(queues = QUEUE_DIRECT1)//rabbit监听器监听name 为 QUEUE_DIRECT1 的消息队列。若出现多个相同监听，默认轮流处理消息。
    //@RabbitHandler //@RabbitListener标注在类上面表示当有收到消息的时候，就交给 @RabbitHandler 的方法处理，根据 MessageConverter 转换后的参数类型选择执行方法
    public void directQueue1(Message message, Channel channel) throws IOException {
        log.debug("{}: {}", QUEUE_DIRECT1, new String(message.getBody()));
        if (aParam = !aParam) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);          // deliveryTag:该消息的index; multiple：是否批量处理.true:将一次性ack所有小于deliveryTag的消息。
        } else {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);    // deliveryTag:该消息的index; multiple：是否批量.true:将一次性拒绝所有小于deliveryTag的消息。; requeue：被拒绝的是否重新入队列
        }
        //channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);       // deliveryTag:该消息的index; requeue：被拒绝的是否重新入队列。与basicNack区别为，此方法仅能拒绝一条
    }

    @RabbitListener(queues = QUEUE_DIRECT1_DL)//死信队列
    public void directQueue1DL(Message message, Channel channel) throws IOException {
        log.debug("{}: {}", QUEUE_DIRECT1_DL, new String(message.getBody()));
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    @RabbitListener(queues = QUEUE_FANOUT1)
    public void fanoutQueue1(Message message, Channel channel) throws IOException {
        log.debug("{}: {}", QUEUE_FANOUT1, new String(message.getBody()));
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    @RabbitListener(queues = QUEUE_TOPIC1)
    public void topicQueue1(Message message, Channel channel) throws IOException {
        log.debug("{}: {}", QUEUE_TOPIC1, new String(message.getBody()));
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    ////    //bean方式配置消费者监听器
////    @Bean
////    public SimpleMessageListenerContainer messageContainer() {
////        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory );
////        container.setQueueNames(QUEUE_DIRECT1);
////        container.setExposeListenerChannel(true); //监听通道打开
////        container.setMaxConcurrentConsumers(3);//最大消费者也就是接收者设置为3
////        container.setConcurrentConsumers(1);//每次消费者接收时，都是当前的消费者去接
////        container.setAcknowledgeMode(AcknowledgeMode.MANUAL); //设置确认模式手工确认
////        //设置监听通道
////        container.setMessageListener((ChannelAwareMessageListener) (message, channel) -> {
////            byte[] body = message.getBody();
////            log.debug("消费端接收到消息{} : {}", QUEUE_DIRECT1, new String(body));
////            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
////        });
////        return container;
////    }
}
