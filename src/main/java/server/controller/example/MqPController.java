package server.controller.example;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import server.config.mq.MqProductor;
import server.tool.Res;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@Api(value = "rabbitMqContronller", description = "rabbitmq测试类")
@RequestMapping("/rabbitmq")
public class MqPController {
    private static final String QUEUE_NAME = "direct-queue1";//简单发送队列
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


    @RequestMapping("/directsend/s")
    public Res sendStringMq(@RequestParam(required = false) String message, @RequestHeader HttpHeaders headers) {   //这里用于测试，key值可以自定义实现
        message = "test msg[" + LocalDateTime.now() + "]：" + message;
        rabbitTemplate.convertAndSend(QUEUE_NAME, message);
        return Res.success(message);
    }


    @RequestMapping("/directsend/m")
    public Res sendMapMq(@RequestParam(required = false) String message, @RequestHeader HttpHeaders headers) {   //这里用于测试，key值可以自定义实现
        Map<String, String> map = new HashMap<>();
        map.put("time", LocalDateTime.now().toString());
        map.put("msg", "test");
        rabbitTemplate.convertAndSend(QUEUE_NAME, map);
        return Res.success(map);
    }


    @RequestMapping("/ps")
    public Res sendPSMq(@RequestParam(required = false) String message, @RequestHeader HttpHeaders headers) {   //这里用于测试，key值可以自定义实现
        message = "test msg[" + LocalDateTime.now() + "]：" + message;
        rabbitTemplate.convertAndSend(FANOUT_EXCHANGE, "[no routingKey]", message);
        return Res.success(message);
    }

    @RequestMapping(value = "/direct", produces = "application/json;charset=UTF-8")
    public Res sendDirectMq(@RequestBody(required = false) JSONArray messageJA, @RequestHeader HttpHeaders headers) {   //这里用于测试，key值可以自定义实现
        if (messageJA == null || messageJA.size() == 0) {
            messageJA = new JSONArray() {{
                add("direct1");
                add("direct2");
                add(LocalDateTime.now());
            }};
        }
        messageJA.forEach((json) -> rabbitTemplate.convertAndSend(DIRECT_EXCHANGE, DIRECT_EXCHANGE_ROUTINGKY_ORANGE, JSONObject.toJSONString(json)));
        return Res.success(messageJA);
    }

    @RequestMapping(value = "/topic", produces = "application/json;charset=UTF-8")
    public Res sendTopicMq(@RequestBody(required = false) JSONArray messageJA, @RequestHeader HttpHeaders headers) {   //这里用于测试，key值可以自定义实现
        if (messageJA == null || messageJA.size() == 0) {
            messageJA = new JSONArray() {{
                add("topic1");
                add("topic2");
                add(LocalDateTime.now());
            }};
        }
        messageJA.forEach((json) -> rabbitTemplate.convertAndSend(TOPIC_EXCHANGE, "a.orange", JSONObject.toJSONString(json)));
        return Res.success(messageJA);
    }
}
 
