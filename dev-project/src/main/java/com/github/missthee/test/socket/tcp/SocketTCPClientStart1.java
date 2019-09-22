package com.github.missthee.test.socket.tcp;

import static com.github.missthee.test.socket.tcp.SocketTCPClient.start;

/**
 * 服务端
 */
public class SocketTCPClientStart1 {
    public static void main(String[] args) {
        start("127.0.0.1", 9001, "1");
    }
}
