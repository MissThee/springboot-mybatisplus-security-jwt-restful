package server.controller.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@Slf4j
@Component
public class MqCController {
    @RabbitListener(queues = "direct-queue1")//说明这个类是配置类，这个rabbit监听器监听name 为 tea 的消息队列
    //    @RabbitHandler        //@RabbitListener 标注在类上面表示当有收到消息的时候，就交给 @RabbitHandler 的方法处理(此方式有时导致循环重试错误，暂未查询到有效解决方法)
    public void directQueue1(@Payload String body, @Headers Map<String, Object> headers) {
        log.info("direct-queue1: " + body);
    }

    @RabbitListener(queues = "fanout-queue1")
    public void fanoutQueue1(@Payload Map body, @Headers Map<String, Object> headers) {
        log.info("fanout-queue1: " + body);
    }

    @RabbitListener(queues = "fanout-queue2")
    public void fanoutQueue2(@Payload String body, @Headers Map<String, Object> headers) {
        log.info("fanout-queue2: " + body);
    }

    @RabbitListener(queues = "topic-queue1")
    public void topicQueue1(@Payload String body, @Headers Map<String, Object> headers) {
        log.info("topic-queue1: " + body);
    }
}
