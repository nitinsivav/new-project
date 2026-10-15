package com.project.server;

import com.google.gson.Gson;
import com.project.model.User;
import com.project.repository.UserRepository;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginHandlerTest {
    private HttpServer server;
    private UserRepository repository;
    private Gson gson;
    
    @BeforeEach
    public void setUp() throws Exception{
        repository = new UserRepository();
        gson = new Gson();
        server = HttpServer.create(new InetSocketAddress(8083),0);
        server.createContext("/api/login", new LoginHandler());
        server.setExecutor(null);
        server.start();
    }

    @AfterEach
    public void tearDown() {
        server.stop(0);
    }

    
    @Test
    public void testSuccessfulLogin() throws Exception {
        String email = "login" + System.currentTimeMillis() + "@test.com";
        String plaintextPassword = "mypassword";
        String hashedPassword = BCrypt.hashpw(plaintextPassword, BCrypt.gensalt());

        
        repository.save(new User("Login Test","123456", email, hashedPassword));

        Map<String, String> payload = new HashMap<>();
        payload.put("email", email);
        payload.put("password", plaintextPassword);

        URL url = new URL("http://localhost:8083/api/login");
        
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");

        try(OutputStream os = conn.getOutputStream()){
            os.write(gson.toJson(payload).getBytes());
        }

        assertEquals(200, conn.getResponseCode(), "Successful Login should return 200 OK");
        
    }

}