package com.github.missthee.test.http;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class SimpleHttpServer {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8099), 0);
        server.createContext("/test", new TestHandler());
        server.start();
    }
}
