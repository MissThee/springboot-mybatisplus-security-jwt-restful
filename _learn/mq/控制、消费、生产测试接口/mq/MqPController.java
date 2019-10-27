package com.github.missthee.controller.example.mq;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;
import com.github.missthee.tool.Res;

import java.util.UUID;

import static com.github.missthee.config.mq.init.MqInfo.*;

@RestController
@RequestMapping("/rabbitmq")
@ConditionalOnProperty(name = "custom.rabbitmq.enable", havingValue = "true")
public class MqPController {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MqPController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @RequestMapping("/simple")//通过默认交换机发送至队列，可使用队列名
    public Res sendSimpleMq() {
        CustomModel mqData = new CustomModel().setValue("test-simple消息测试");
        rabbitTemplate.convertAndSend(QUEUE_DIRECT1, mqData, getCorrelationData());
        return Res.success(mqData);
    }

    @RequestMapping("/direct")//direct
    public Res sendDirectMq(@RequestParam(value = "routingKey", required = false) String routingKey) {
        routingKey = routingKey == null ? "orange" : routingKey;
        CustomModel mqData = new CustomModel().setValue("test-direct消息测试");
        rabbitTemplate.convertAndSend(EXCHANGE_DIRECT, routingKey, mqData, getCorrelationData());
        return Res.success(mqData);
    }

    @RequestMapping("/ps")//PubSub、发布/订阅、广播、fanout
    public Res sendPSMq() {
        CustomModel mqData = new CustomModel().setValue("test-ps消息测试");
        rabbitTemplate.convertAndSend(EXCHANGE_FANOUT, "", mqData, getCorrelationData());
        return Res.success(mqData);
    }

    @RequestMapping("/topic")//topic
    public Res sendTopicMq(@RequestParam(value = "routingKey", required = false) String routingKey) {
        routingKey = routingKey == null ? "a.orange" : routingKey;
        CustomModel mqData = new CustomModel().setValue("test-topic消息测试");
        rabbitTemplate.convertAndSend(EXCHANGE_TOPIC, routingKey, mqData, getCorrelationData());
        return Res.success(mqData);
    }

    private static CorrelationData getCorrelationData() {
        return new CorrelationData("自定义id: " + UUID.randomUUID().toString());
    }
}
 
