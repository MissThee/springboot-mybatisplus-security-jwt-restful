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
import server.config.security.JavaJWT;
import server.db.primary.model.basic.User;
import server.service.UserService;
import server.socketio.model.AckModel;
import server.socketio.model.MessageModel;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class MessageEventHandler {
    //此示例暂未与数据库中数据做任何关联
    private static SocketIOServer socketIoServer;
    private static Map<String, List<UUID>> userIdUUIDsMap = new ConcurrentHashMap<>();
    private static Map<UUID, String> UUIDUserIdMap = new ConcurrentHashMap<>();
    private static Map<String, String> userIdNicknameMap = new ConcurrentHashMap<>();

    private final UserService userService;

    @Autowired
    public MessageEventHandler(SocketIOServer server, UserService userService) {
        socketIoServer = server;
        this.userService = userService;
    }

    @OnConnect
    public void onConnect(SocketIOClient client) {
        log.info("客户端:  " + client.getSessionId() + "  已连接");
        String userId = JavaJWT.getId(client.getHandshakeData().getSingleUrlParam("token"));
//        String userId=client.getHandshakeData().getHttpHeaders().get("Authorization");
        User user = userService.selectOneById(Integer.parseInt(userId));
        addUserInfo(user, client.getSessionId());
        updateUserListForWeb();
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        log.info("客户端:  " + client.getSessionId() + "  断开连接");
        removeUserInfo(client.getSessionId());
        updateUserListForWeb();
    }

    @OnEvent(value = "message")
    public void onMessage(SocketIOClient client, AckRequest ackRequest, MessageModel data) {
        log.info("message触发");
        //当前端send/emit有回调函数时，ackRequest.isAckRequested()==true
        if (ackRequest.isAckRequested()) {
            ackRequest.sendAckData(AckModel.success());
        }
    }

    @OnEvent(value = "broadcast")
    public void onBroadcast(SocketIOClient client, AckRequest ackRequest, MessageModel data) {
        log.info("broadcast触发");
        //当前端send/emit有回调函数时，ackRequest.isAckRequested()==true
        if (ackRequest.isAckRequested()) {
            ackRequest.sendAckData(AckModel.success());
        }
        data.setFromId(UUIDUserIdMap.get(client.getSessionId()));
        data.setFromNickname(userIdNicknameMap.get(data.getFromId()));
        socketIoServer.getBroadcastOperations().sendEvent("broadcast", data);
    }

    @OnEvent(value = "toOneUserByUserId")
    public static void toOneUserByUserId(SocketIOClient client, AckRequest ackRequest, MessageModel data) {   //向客户端推消息
        log.info("toOneUserByUserId触发：" + data.getContent() + "；" + String.valueOf(UUIDUserIdMap.get(client.getSessionId())) + "→" + String.valueOf(data.getToId()) + ":" + data.getMsg());
        //当前端send/emit有回调函数时，ackRequest.isAckRequested()==true
        if (ackRequest.isAckRequested()) {
            ackRequest.sendAckData(AckModel.success());
        }
        data.setFromId(UUIDUserIdMap.get(client.getSessionId()));
        data.setFromNickname(userIdNicknameMap.get(data.getFromId()));
        data.setToNickname(userIdNicknameMap.get(data.getToId()));
        if (data.getToId() != null) {
            List<UUID> uuidList = userIdUUIDsMap.get(data.getToId());
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
    }

    private void updateUserListForWeb() {
        socketIoServer.getBroadcastOperations().sendEvent("onlineUser", userIdNicknameMap);
    }

    //在线用户信息更新
    private void addUserInfo(User user, UUID uuid) {
        String userId = String.valueOf(user.getId());
        String userNickname = user.getNickname();
        //更新UUIDUserIdMap
        UUIDUserIdMap.put(uuid, userId);
        //更新userIdUUIDsMap
        {
            if (userIdUUIDsMap.containsKey(userId)) {
                userIdUUIDsMap.get(userId).add(uuid);
            } else {
                userIdUUIDsMap.put(userId, new ArrayList<UUID>() {{
                    add(uuid);
                }});
            }
        }
        //更新userIdNicknameMap
        userIdNicknameMap.put(userId, userNickname);
        System.out.println("userIdUUIDsMap:" + userIdUUIDsMap);
    }

    private void removeUserInfo(UUID uuid) {
        String userId = UUIDUserIdMap.get(uuid);
        //更新UUIDUserIdMap
        UUIDUserIdMap.remove(uuid);
        //更新userIdUUIDsMap
        {
            List UUIDList = userIdUUIDsMap.get(userId);
            UUIDList.remove(uuid);
            if (UUIDList.size() == 0) {
                userIdUUIDsMap.remove(userId);
            }
        }
        //更新userIdNicknameMap
        userIdNicknameMap.remove(userId);
        System.out.println("userIdUUIDsMap:" + userIdUUIDsMap);
    }
}
