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
import java.util.HashMap;
import java.util.Map;

public class LoginHandler implements HttpHandler {
    private final UserRepository repository = new UserRepository();
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

        if("OPTIONS".equals(exchange.getRequestMethod())){
            exchange.sendResponseHeaders(204, -1);
        }

        if(!"POST".equals(exchange.getRequestMethod())){
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        try(InputStreamReader reader = new InputStreamReader(exchange.getRequestBody())){

            @SuppressWarnings("unchecked")
            Map<String,String> payload = gson.fromJson(reader, Map.class);
            
            String email = payload.get("email");
            String password = payload.get("password");

            if(email == null || password == null ){
                sendJsonResponse(exchange, 400, false, "Email and password are required.");
                return;
            }

            User user = repository.findByEmail(email);

            if(user != null && BCrypt.checkpw(password, user.getPassword())){
                sendJsonResponse(exchange, 200, true, "Login successful.");
            }else{
                sendJsonResponse(exchange, 401, false, "Invalid credentials.");
            }

        }catch(SQLException e){
            e.printStackTrace();
            sendJsonResponse(exchange, 500, false, "Internal Server error.");
        }

    }

    private void sendJsonResponse(HttpExchange exchange, int statusCode, boolean success, String message) throws IOException {
        Map<String,Object> response = new HashMap<>();
        response.put("success",success);
        response.put("message",message);

        String jsonResponse = gson.toJson(response);
        
        byte[] bytes = jsonResponse.getBytes();

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, bytes.length);

        try(OutputStream os = exchange.getResponseBody()){
            os.write(bytes);
        }

    }

}