package com.github.socketio;

import com.corundumstudio.socketio.AckCallback;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.github.base.dto.login.AuthDTO;
import com.github.base.service.interf.login.AuthInfoService;
import com.github.base.service.interf.manage.SysUserService;
import com.github.common.config.security.jwt.JavaJWT;
import com.github.common.tool.Res;
import com.github.socketio.model.MessageModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

//SocketIOServer自定义事件（功能实现）
@Component
@Slf4j
public class MessageEventHandler {

    private static SocketIOServer socketIoServer;
    private static Map<String, List<UUID>> userIdUUIDsMap = new ConcurrentHashMap<>();  //存储已登录的<用户id,连接时产生的多个uuid>对应关系
    private static Map<UUID, String> UUIDUserIdMap = new ConcurrentHashMap<>();         //存储已登录的<uuid,用户id>。（仅为方便查询，可直接遍历userIdUUIDsMap，得到对应值）
    private static Map<String, String> userIdNicknameMap = new ConcurrentHashMap<>();   //存储已登录的<用户id,昵称>。（仅为方便查询，可直接查询数据库得到值，但直接数据库读取开销大，尽量做成缓存）
    private final AuthInfoService authInfoService;

    @Autowired
    public MessageEventHandler(SocketIOServer server, SysUserService sysUserService, AuthInfoService authInfoService) {
        socketIoServer = server;
        this.authInfoService = authInfoService;
    }

    //服务端监听事件。（客户端触发服务端事件）
    @OnConnect
    public void connect(SocketIOClient client) {
        log.debug("客户端:  " + client.getSessionId() + "  已连接");
        String userId = JavaJWT.getId(client.getHandshakeData().getSingleUrlParam("token"));
//        String userId=client.getHandshakeData().getHttpHeaders().get("token");
        if (userId != null) {
            AuthDTO authDTO = authInfoService.selectUserById(userId);
            addUserInfo(authDTO, client.getSessionId());
            broadcastUserList();
        }
    }

    @OnDisconnect
    public void disconnect(SocketIOClient client) {
        log.debug("客户端:  " + client.getSessionId() + "  断开连接");
        removeUserInfo(client.getSessionId());
        broadcastUserList();
    }

    @OnEvent(value = "message")
    public void message(SocketIOClient client, AckRequest ackRequest, MessageModel data) {
        log.debug("message触发");
        //当前端send/emit有回调函数时，ackRequest.isAckRequested()==true
        if (ackRequest.isAckRequested()) {
            ackRequest.sendAckData(Res.success());
        }
    }

    @OnEvent(value = "broadcast")
    public void broadcast(SocketIOClient client, AckRequest ackRequest, MessageModel data) {
        log.debug("broadcast触发: " + data.getContent());
        //当前端send/emit有回调函数时，ackRequest.isAckRequested()==true
        if (ackRequest.isAckRequested()) {
            ackRequest.sendAckData(Res.success());
        }
        data.setFromId(UUIDUserIdMap.get(client.getSessionId()));
        data.setFromNickname(userIdNicknameMap.get(data.getFromId()));
        broadcastData(data);
    }

    @OnEvent(value = "toOneUserByUserId")
    public void toOneUserByUserId(SocketIOClient client, AckRequest ackRequest, MessageModel data) {   //向客户端推消息
        log.debug("toOneUserByUserId触发：" + data.getContent() + "；" + UUIDUserIdMap.get(client.getSessionId()) + "→" + data.getToId() + ":" + data.getMsg());
        //当前端send/emit有回调函数时，ackRequest.isAckRequested()==true
        //ackRequest.sendAckData()，回传回调参数
        if (data.getToId() == null) {
            if (ackRequest.isAckRequested()) {
                ackRequest.sendAckData(Res.failure());
            }
        } else {
            data.setFromId(UUIDUserIdMap.get(client.getSessionId()));
            data.setFromNickname(userIdNicknameMap.get(data.getFromId()));
            data.setToNickname(userIdNicknameMap.get(data.getToId()));
            List<UUID> uuidList = userIdUUIDsMap.get(data.getToId());
            boolean result = false;
            for (UUID uuid : uuidList) {
                result = sendToOneUser(client, uuid, data);
            }
            if (ackRequest.isAckRequested()) {
                ackRequest.sendAckData(Res.res(result, data));
            }
        }
    }

    //服务端发送事件。（服务端触发客户端事件）
    //广播在线用户列表
    private void broadcastUserList() {
        socketIoServer.getBroadcastOperations().sendEvent("onlineUser", userIdNicknameMap);
    }

    //广播消息
    private void broadcastData(MessageModel data) {
        socketIoServer.getBroadcastOperations().sendEvent("broadcast", data);
    }

    //发送消息至指定用户
    private boolean sendToOneUser(SocketIOClient client, UUID uuid, MessageModel data) {
        Boolean[] isSuccess = {false};
        if (socketIoServer.getClient(uuid) != null) {
            socketIoServer.getClient(uuid).sendEvent(
                    "msgToOneUser",
                    new AckCallback<String>(String.class) {
                        @Override
                        public void onSuccess(String result) { //接收客户端回执的回调函数
                            isSuccess[0] = true;
                            log.debug("客户端回执: " + client.getSessionId() + " data: " + result);
                        }
                    },
                    data);
        }
        return isSuccess[0];
    }

    //用户列表维护方法
    //在线用户信息更新
    private void addUserInfo(AuthDTO loginDTO, UUID uuid) {
        String userId = String.valueOf(loginDTO.getId());
        String userNickname = loginDTO.getNickname();
        //更新UUIDUserIdMap
        UUIDUserIdMap.put(uuid, userId);
        //更新userIdUUIDsMap
        {
            if (userIdUUIDsMap.containsKey(userId)) {
                userIdUUIDsMap.get(userId).add(uuid);
            } else {
                List<UUID> list = new ArrayList<>();
                list.add(uuid);
                userIdUUIDsMap.put(userId, list);
            }
        }
        //更新userIdNicknameMap
        userIdNicknameMap.put(userId, userNickname);
        log.debug("userIdUUIDsMap:" + userIdUUIDsMap);
    }

    //在线用户信息更新
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
        log.debug("userIdUUIDsMap:" + userIdUUIDsMap);
    }
}
