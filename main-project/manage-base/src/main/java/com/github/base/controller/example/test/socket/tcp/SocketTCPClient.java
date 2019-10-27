package com.github.base.controller.example.test.socket.tcp;

import java.io.IOException;
import java.net.Socket;

/**
 * 服务端
 */
public class SocketTCPClient {
    public static void start(String host, int port, String name) {
        while (true) {
            Socket socket;
            try {
                socket = new Socket(host, port);
                Thread receiveStart = SocketTCPTool.receiveStart(socket, 61, "Client R" + name);//接收
                Thread sendStart = SocketTCPTool.sendStart(socket, "Client S" + name, false);//发送
                try {
                    receiveStart.join();
                    sendStart.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                System.out.println("连接失败,重试:" + e.toString());
            } finally {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
