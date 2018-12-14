package server.socketio.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageInfo {
    String fromId;
    String content;
    String toId;
    String msg;
    String time;
}
