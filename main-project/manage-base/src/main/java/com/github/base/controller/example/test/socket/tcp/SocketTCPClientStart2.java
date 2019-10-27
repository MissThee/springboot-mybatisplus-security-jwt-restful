package com.github.base.controller.example.test.socket.tcp;

import static com.github.base.controller.example.test.socket.tcp.SocketTCPClient.start;

/**
 * 服务端
 */
public class SocketTCPClientStart2 {
    public static void main(String[] args) {
        start("127.0.0.1", 9001, "2");
    }
}
