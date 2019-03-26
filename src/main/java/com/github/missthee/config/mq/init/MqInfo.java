package com.github.missthee.config.mq.init;

//交换机、队列定义名称集合，实际交换机类型和绑定关系在配置类中实现
public class ExchangeAndQueue {
    //性能排序：fanout > direct >> topic。比例大约为11：10：6
    //路由字符匹配    任何发送到Direct Exchange的消息都会被转发到RouteKey中指定的Queue。
    public static final String EXCHANGE_DIRECT = "exchange-direct";
    public static final String QUEUE_DIRECT1 = "queue-direct1";
    public static final String QUEUE_DIRECT2 = "queue-direct2";
    //广播    任何发送到Fanout Exchange的消息都会被转发到与该Exchange绑定(Binding)的所有Queue上
    public static final String EXCHANGE_FANOUT = "exchange-fanout";
    public static final String QUEUE_FANOUT1 = "queue-fanout1";
    public static final String QUEUE_FANOUT2 = "queue-fanout2";
    //路由通配符匹配   任何发送到Topic Exchange的消息都会被转发到所有关心RouteKey中指定话题的Queue上   //通配符模式
    public static final String EXCHANGE_TOPIC = "exchange-topic";
    public static final String QUEUE_TOPIC1 = "queue-topic1";
    public static final String QUEUE_TOPIC2 = "queue-topic2";
    // headers匹配（几乎不用）
    //private static final String HEADERS_EXCHANGE = "exchange-headers";
}
