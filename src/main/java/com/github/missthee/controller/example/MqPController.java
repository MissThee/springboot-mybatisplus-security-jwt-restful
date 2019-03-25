package com.github.missthee.controller.example;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.missthee.config.mq.MqProductor;
import com.github.missthee.tool.Res;

import java.io.Serializable;
import java.time.LocalDateTime;

import static com.github.missthee.config.mq.init.ExchangeAndQueue.*;

@Slf4j
@RestController
@RequestMapping("/rabbitmq")
//@ConditionalOnProperty(name = "custom.rabbitmq.enable", havingValue = "true")
public class MqPController {


    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MqPController(MqProductor mqProductor) {
        this.rabbitTemplate = mqProductor.rabbitTemplate();
    }

    @RequestMapping("/simple")//通过默认交换机发送至队列，可使用队列名
    public Res sendMapMq() {
        MqData mqData = new MqData(LocalDateTime.now().toString(), "test-simple消息测试");
        rabbitTemplate.convertAndSend(QUEUE_DIRECT1, mqData);
        return Res.success(mqData);
    }

    @RequestMapping("/ps")//PubSub、发布/订阅、广播、fanout
    public Res sendPSMq() {
        MqData mqData = new MqData(LocalDateTime.now().toString(), "test-ps消息测试");
        rabbitTemplate.convertAndSend(EXCHANGE_FANOUT, mqData);
        return Res.success(mqData);
    }

    @RequestMapping(value = "/direct")//direct
    public Res sendDirectMq() {
        MqData mqData = new MqData(LocalDateTime.now().toString(), "test-direct消息测试");
        rabbitTemplate.convertAndSend(EXCHANGE_DIRECT, "orange", mqData);
        return Res.success(mqData);
    }

    @RequestMapping(value = "/topic")//topic
    public Res sendTopicMq() {
        MqData mqData = new MqData(LocalDateTime.now().toString(), "test-topic消息测试");
        rabbitTemplate.convertAndSend(EXCHANGE_TOPIC, "a.orange", mqData);
        return Res.success(mqData);
    }

    @Data
    @AllArgsConstructor
    @Accessors(chain = true)
    public class MqData implements Serializable {
        String time;
        String msg;
    }
}
 
