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
import server.socketio.model.InitInfo;
import server.socketio.model.MessageInfo;

import java.util.*;

@Component
@Slf4j
public class MessageEventHandler {

    private static SocketIOServer socketIoServer;
    private static Map<String, List<UUID>> clientMap = new HashMap<>();

    @Autowired
    public MessageEventHandler(SocketIOServer server) {
        socketIoServer = server;
    }

    @OnConnect
    public void onConnect(SocketIOClient client) {
        log.info("客户端:  " + client.getSessionId() + "  已连接");
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        log.info("客户端:  " + client.getSessionId() + "  断开连接");
        UUID sessionId = client.getSessionId();
        removeUserInfo(sessionId);
    }

    @OnEvent(value = "initInfo")
    public void initInfo(SocketIOClient client, AckRequest request, InitInfo data) {
        log.info("initInfo用户信息：" + data.getUserId());
        String userId = data.getUserId();
        UUID sessionId = client.getSessionId();
        addUserInfo(userId, sessionId);

        messageReceipt(client, "initInfo succeed");
    }

    @OnEvent(value = "event")
    public void onEvent(SocketIOClient client, AckRequest request, MessageInfo data) {
        log.info("event发来消息：" + data.getContent());
        messageReceipt(client, "event succeed");
    }

    @OnEvent(value = "message")
    public void onMessage(SocketIOClient client, AckRequest request, MessageInfo data) {
        log.info("message发来消息：" + data.getContent());
        messageReceipt(client, "message succeed");
    }

    @OnEvent(value = "broadcast")
    public void onBroadcast(SocketIOClient client, AckRequest request, MessageInfo data) {
        log.info("broadcast发来消息：" + data.getContent());
        messageReceipt(client, "broadcast succeed");
        socketIoServer.getBroadcastOperations().sendEvent("broadcast", data.getContent());
    }

    public static void sendBuyLogEvent() {   //这里就是向客户端推消息了
        String dateTime = new Date().toString();
//        for (UUID clientId : listClient) {
//            if (socketIoServer.getClient(clientId) == null) continue;
//            socketIoServer.getClient(clientId).sendEvent("event", dateTime);
//        }
    }

    private void messageReceipt(SocketIOClient client, String msg) {
        socketIoServer.getClient(client.getSessionId()).sendEvent("messageReceipt", msg);
    }

    private void addUserInfo(String userId, UUID sessionId) {
        if (clientMap.containsKey(userId)) {
            clientMap.get(userId).add(sessionId);
        } else {
            clientMap.put(userId, new ArrayList<UUID>() {{
                add(sessionId);
            }});
        }
        System.out.println("clientMap:" + clientMap);
    }

    private void removeUserInfo(UUID sessionId) {
        for (String key : clientMap.keySet()) {
            List UUIDList = clientMap.get(key);
            UUIDList.remove(sessionId);
            if (UUIDList.size() == 0) {
                clientMap.remove(key);
            }
        }
        System.out.println("clientMap:" + clientMap);
    }
}
