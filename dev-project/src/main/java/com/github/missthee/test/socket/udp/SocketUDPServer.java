package com.github.missthee.test.socket.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import static com.github.missthee.test.socket.udp.SocketUDPTool.receive;
import static com.github.missthee.test.socket.udp.SocketUDPTool.send;

public class SocketUDPServer {
    public static void main(String[] args) throws Exception {
        DatagramSocket ds = new DatagramSocket(9898);
        while (true) {
            DatagramPacket dp = receive(ds);
            send(ds, dp.getAddress(), dp.getPort(), "[OK]");
        }
//        ds.close();
    }
}