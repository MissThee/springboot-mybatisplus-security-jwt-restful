package com.github.missthee.socketio;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

//SocketIOServer启动类
@Component
@ConditionalOnProperty(name = "socket.io.enable", havingValue = "true")
public class MyCommandLineRunner implements CommandLineRunner {
    private final SocketIOServer socketIOServer;

    @Autowired
    public MyCommandLineRunner(SocketIOServer server) {
        this.socketIOServer = server;
    }

    @Override
    public void run(String... args) {
        socketIOServer.start();
    }
}
