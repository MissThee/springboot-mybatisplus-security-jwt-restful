package server.socketio;

import com.corundumstudio.socketio.AckCallback;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class MessageEventHandler {
    //此示例暂未与数据库中数据做任何关联
    private static SocketIOServer socketIoServer;
    private static Map<String, List<UUID>> userUUIDsMap = new HashMap<>();
    private static Map<UUID, String> UUIDUserMap = new HashMap<>();

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
        updateUserListForWeb();

    }

    @OnEvent(value = "initInfo")
    public void initInfo(SocketIOClient client, AckRequest ackRequest, InitInfo data) {
        log.info("initInfo用户信息：" + data.getUserId());
        String userId = data.getUserId();
        UUID sessionId = client.getSessionId();
        addUserInfo(userId, sessionId);
        if (ackRequest.isAckRequested()) {
            ackRequest.sendAckData("服务器已接收");
        }
        updateUserListForWeb();
    }

    @OnEvent(value = "event")
    public void onEvent(SocketIOClient client, AckRequest ackRequest, MessageInfo data) {
        log.info("event发来消息：" + data.getContent());
        //当前端send/emit有回调函数时，ackRequest.isAckRequested()==true
        if (ackRequest.isAckRequested()) {
            ackRequest.sendAckData("服务器已接收");
        }
    }

    @OnEvent(value = "message")
    public void onMessage(SocketIOClient client, AckRequest ackRequest, MessageInfo data) {
        log.info("message发来消息：" + data.getContent());
        if (ackRequest.isAckRequested()) {
            ackRequest.sendAckData("服务器已接收");
        }
    }

    @OnEvent(value = "broadcast")
    public void onBroadcast(SocketIOClient client, AckRequest ackRequest, MessageInfo data) {
        log.info("broadcast发来消息：" + data.getContent());
        if (ackRequest.isAckRequested()) {
            ackRequest.sendAckData("服务器已接收");
        }
        socketIoServer.getBroadcastOperations().sendEvent("broadcast", data.getContent());
    }

    @OnEvent(value = "toOneUserByUserId")
    public static void toOneUserByUserId(SocketIOClient client, AckRequest ackRequest, MessageInfo data) {   //向客户端推消息
        log.info("toOneUserByUserId发来消息：" + data.getContent());
        if (ackRequest.isAckRequested()) {
            ackRequest.sendAckData("服务器已接收");
        }

        data.setFromId(UUIDUserMap.get(client.getSessionId()));
        data.setTime(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));

        System.out.println(data.getFromId() + "→" + data.getToId() + ":" + data.getMsg());

        List<UUID> uuidList = userUUIDsMap.getOrDefault(data.getToId(), new ArrayList<>());
        for (UUID uuid : uuidList) {
            if (socketIoServer.getClient(uuid) != null) {
                socketIoServer.getClient(uuid).sendEvent(
                        "msgToMe",
                        new AckCallback<String>(String.class) {
                            @Override
                            public void onSuccess(String result) {
                                System.out.println("客户端回执: " + client.getSessionId() + " data: " + result);
                            }
                        },
                        data);
            }
        }
    }

    private void updateUserListForWeb() {
        socketIoServer.getBroadcastOperations().sendEvent("onlineUser", UUIDUserMap.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toSet()));
    }

    private void addUserInfo(String userId, UUID uuid) {
        UUIDUserMap.put(uuid, userId);
        {
            if (userUUIDsMap.containsKey(userId)) {
                userUUIDsMap.get(userId).add(uuid);
            } else {
                userUUIDsMap.put(userId, new ArrayList<UUID>() {{
                    add(uuid);
                }});
            }
        }
        System.out.println("userUUIDsMap:" + userUUIDsMap);
    }

    private void removeUserInfo(UUID uuid) {
        String userId = UUIDUserMap.get(uuid);
        UUIDUserMap.remove(uuid);
        {
            List UUIDList = userUUIDsMap.get(userId);
            UUIDList.remove(uuid);
            if (UUIDList.size() == 0) {
                userUUIDsMap.remove(userId);
            }
        }
        System.out.println("userUUIDsMap:" + userUUIDsMap);
    }
}
