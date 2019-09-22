package com.github.missthee.test.socket.tcp;


import java.io.*;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

import static com.github.missthee.test.socket.tcp.SocketTCPTool.*;

/**
 * 服务端
 */
public class SocketTCPServer {

    public static void main(String[] args) {
        startServer(9001);
    }

    private static void startServer(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);//创建一个ServerSocket，port是客户端的端口
            while (true) {
                try {
                    Socket socket = serverSocket.accept();//从请求队列中取出链接
                    receiveStart(socket, 61, "Server R");
                    sendStart(socket, "Server S", true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
