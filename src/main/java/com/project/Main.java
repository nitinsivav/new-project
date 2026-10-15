package com.project;

import java.net.InetSocketAddress;

import com.project.server.LoginHandler;
import com.project.server.RegistrationHandler;
import com.project.server.StaticFileHandler;
import com.sun.net.httpserver.HttpServer;


public class Main {
    public static void main(String[] args) throws Exception {
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        
        server.createContext("/api/register", new RegistrationHandler());

        server.createContext("/", new StaticFileHandler());
        server.createContext("/api/login", new LoginHandler());


        server.setExecutor(null);
        server.start();
        System.out.println("Backend server is running on http://localhost:" + port);
    }
}
