package com.axxes.demo;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        HttpServer server = createServer();
        server.setExecutor(Executors.newFixedThreadPool(10));
        server.createContext("/hello", helloWorldHandler());
        server.start();
        System.out.println("Server started");
    }

    private static HttpServer createServer() {
        try {
            return HttpServer.create(new InetSocketAddress("localhost", 8001), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static HttpHandler helloWorldHandler() {
        return exchange -> {
            if (!"GET".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(404, 0);
            }

            try (OutputStream os = exchange.getResponseBody()) {
                Message message = new Message("Hello World", LocalDateTime.now());

                String response = "{\n" +
                        "\"content:\": \"" + message.content() + "\"," +
                        "\"time:\": \"" + message.time() + "\"" +
                        "}";
                exchange.sendResponseHeaders(200, response.length());
                os.write(response.getBytes());
                os.flush();
            }
        };
    }

    private record Message(String content, LocalDateTime time) {
    }
}
