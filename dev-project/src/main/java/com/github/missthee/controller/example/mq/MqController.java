package com.github.missthee.controller.example.mq;

import com.github.missthee.tool.Res;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.UUID;

import static com.github.missthee.config.mq.init.MqInfo.*;

@RestController
@RequestMapping("/rabbitmq/mana")
@ConditionalOnProperty(name = "custom.rabbitmq.enable", havingValue = "true")
public class MqController {
    @Resource
    private RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;

    @RequestMapping("/start")
    public Res start() {//启动mq监听
        rabbitListenerEndpointRegistry.start();
        return Res.success();
    }

    @RequestMapping("/stop")
    public Res sendDirectMq() {//停止mq监听
        rabbitListenerEndpointRegistry.stop();
        return Res.success();
    }
}
 
