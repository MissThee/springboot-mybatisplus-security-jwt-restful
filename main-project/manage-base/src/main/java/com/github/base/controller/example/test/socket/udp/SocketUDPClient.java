package com.github.base.controller.example.test.socket.udp;

import java.net.DatagramSocket;
import java.net.InetAddress;

import static com.github.base.controller.example.test.socket.udp.SocketUDPTool.receive;
import static com.github.base.controller.example.test.socket.udp.SocketUDPTool.send;

public class SocketUDPClient {
    public static void main(String[] args) throws Exception {
        DatagramSocket ds = new DatagramSocket(9897);
        for (int i = 0; i < 10; i++) {
            send(ds, InetAddress.getByName("localhost"), 9898, "[test message]");
            receive(ds);
        }
        ds.close();
    }
}
