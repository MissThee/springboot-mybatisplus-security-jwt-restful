package com.github.base.controller.example.test.socket.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SocketUDPTool {
    public static DatagramPacket receive(DatagramSocket ds) throws IOException {
        byte[] buf = new byte[1024];
        DatagramPacket dp = new DatagramPacket(buf, buf.length);
        ds.receive(dp);//此方法阻塞，直到有数据包
        String data = new String(dp.getData()); //数据
        System.out.println(dp.getAddress().toString() + ":" + data);
        return dp;
    }

    public static void send(DatagramSocket ds, InetAddress inetAddress, int port, String data) throws IOException {
        byte[] buf = data.getBytes();
        DatagramPacket dp = new DatagramPacket(buf, buf.length, inetAddress, port);
        ds.send(dp);
    }
}
