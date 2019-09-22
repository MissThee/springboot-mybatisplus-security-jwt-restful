package com.github.missthee.socketio.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
//发送消息实体类
@Data
public class MessageModel {
    String fromId;
    String fromNickname;
    String content;
    String toId;
    String toNickname;
    String msg;
    String time= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
}
