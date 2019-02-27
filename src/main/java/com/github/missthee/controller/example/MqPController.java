package server.controller.example;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import server.config.mq.MqProductor;
import server.tool.Res;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@Api(value = "rabbitMqContronller", description = "rabbitmq测试类")
@RequestMapping("/rabbitmq")
public class MqPController {
    private static final String QUEUE_NAME = "direct-queue1";//队列
    //性能排序：fanout > direct >> topic。比例大约为11：10：6
    private static final String FANOUT_EXCHANGE = "exchange-fanout";//   任何发送到Fanout Exchange的消息都会被转发到与该Exchange绑定(Binding)的所有Queue上

    private static final String DIRECT_EXCHANGE = "exchange-direct";//   任何发送到Direct Exchange的消息都会被转发到RouteKey中指定的Queue。
    private static final String DIRECT_EXCHANGE_ROUTINGKY_ORANGE = "orange";
    private static final String DIRECT_EXCHANGE_ROUTINGKY_BLACK = "black";
    private static final String DIRECT_EXCHANGE_ROUTINGKY_ALL = "all";

    private static final String TOPIC_EXCHANGE = "exchange-topic";//     任何发送到Topic Exchange的消息都会被转发到所有关心RouteKey中指定话题的Queue上   //通配符模式

    //private static final String HEADERS_EXCHANGE = "exchange-headers";// headers匹配


    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MqPController(MqProductor mqProductor) {
        this.rabbitTemplate = mqProductor.rabbitTemplate();
    }

    @RequestMapping("/simple")//通过默认交换机发送至队列，使用队列名
    public Res sendMapMq() {
        MqData mqData = new MqData(LocalDateTime.now().toString(), "test-simple");
        rabbitTemplate.convertAndSend(QUEUE_NAME, mqData);
        return Res.success(mqData);
    }

    @RequestMapping("/ps")
    public Res sendPSMq() {
        MqData mqData = new MqData(LocalDateTime.now().toString(), "test-ps");
        rabbitTemplate.convertAndSend(FANOUT_EXCHANGE, "[no routingKey]", mqData);
        return Res.success(mqData);
    }

    @RequestMapping(value = "/direct")
    public Res sendDirectMq() {
        MqData mqData = new MqData(LocalDateTime.now().toString(), "test-direct");
        rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, DIRECT_EXCHANGE_ROUTINGKY_ORANGE, mqData);
        return Res.success(mqData);
    }

    @RequestMapping(value = "/topic")
    public Res sendTopicMq() {
        MqData mqData = new MqData(LocalDateTime.now().toString(), "test-topic");
        rabbitTemplate.convertAndSend(TOPIC_EXCHANGE, "a.orange", mqData);
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
 
