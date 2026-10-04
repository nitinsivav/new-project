package com.project.server;

import com.google.gson.Gson;
import com.project.model.User;
import com.project.repository.UserRepository;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.SQLException;

public class RegistrationHandler implements HttpHandler {
    private final Gson gson = new Gson();
    private final UserRepository repository = new UserRepository();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "POST, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        if (!"POST".equals(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1); 
            return;
        }

        try {
            InputStreamReader reader = new InputStreamReader(exchange.getRequestBody());
            User user = gson.fromJson(reader, User.class);

            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            user.setPassword(hashedPassword);

            repository.save(user);

            String response = "{\"message\": \"User registered successfully\"}";
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(201, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }

        } catch (SQLException e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(500, -1); 
        } catch (Exception e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(400, -1);
        }
    }
}
