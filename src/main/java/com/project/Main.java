package com.project;

import com.project.server.RegistrationHandler;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws Exception {
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        
        // Mount our registration endpoint
        server.createContext("/api/register", new RegistrationHandler());
        
        server.setExecutor(null);
        server.start();
        System.out.println("Backend server is running on http://localhost:" + port);
    }
}
