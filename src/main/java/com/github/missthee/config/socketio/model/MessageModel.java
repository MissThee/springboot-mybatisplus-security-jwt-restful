package com.github.missthee.config.socketio.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
