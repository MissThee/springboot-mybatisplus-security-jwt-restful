package com.github.base.controller.example.test.socket.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class SocketUDPServer {
    public static void main(String[] args) throws Exception {
        DatagramSocket ds = new DatagramSocket(9898);
        while (true) {
            DatagramPacket dp = SocketUDPTool.receive(ds);
            SocketUDPTool.send(ds, dp.getAddress(), dp.getPort(), "[OK]");
        }
//        ds.close();
    }
}