package server.socketio;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

@Component
@Slf4j
public class MessageEventHandler {

    private static SocketIOServer socketIoServer;
    private static ArrayList<UUID> listClient = new ArrayList<>();
    static final int limitSeconds = 60;

    @Autowired
    public MessageEventHandler(SocketIOServer server) {
        socketIoServer = server;
    }

    @OnConnect
    public void onConnect(SocketIOClient client) {
        listClient.add(client.getSessionId());
        log.info("客户端:  " + client.getSessionId() + "  已连接");
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        log.info("客户端:  " + client.getSessionId() + "  断开连接");
    }

    @OnEvent(value = "event")
    public void onEvent(SocketIOClient client, AckRequest request, MessageInfo data) {
        log.info("event发来消息：" + data.getContent());
        socketIoServer.getClient(client.getSessionId()).sendEvent("event", "event 成功");
    }

    @OnEvent(value = "message")
    public void onMessage(SocketIOClient client, AckRequest request, MessageInfo data) {
        log.info("message发来消息：" + data.getContent());
        socketIoServer.getClient(client.getSessionId()).sendEvent("message", "message 成功");
    }

    @OnEvent(value = "broadcast")
    public void onBroadcast(SocketIOClient client, AckRequest request, MessageInfo data) {
        log.info("broadcast发来消息：" + data.getContent());
        socketIoServer.getBroadcastOperations().sendEvent("broadcast", "广播消息："+data.getContent());
    }

    public static void sendBuyLogEvent() {   //这里就是向客户端推消息了
        String dateTime = new Date().toString();
        for (UUID clientId : listClient) {
            if (socketIoServer.getClient(clientId) == null) continue;
            socketIoServer.getClient(clientId).sendEvent("event", dateTime );
        }
    }
}
