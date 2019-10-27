package com.github.base.controller.example.test.socket.tcp;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

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
                    SocketTCPTool.receiveStart(socket, 61, "Server R");
                    SocketTCPTool.sendStart(socket, "Server S", true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
