package com.project.server;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegistrationHandlerTest {
    private HttpServer server;

    @BeforeEach
    public void setUp() throws Exception {
        server = HttpServer.create(new InetSocketAddress(8081), 0);
        server.createContext("/api/register", new RegistrationHandler());
        server.setExecutor(null);
        server.start();
    }

    @AfterEach
    public void tearDown() {
        server.stop(0);
    }

    @Test
    public void testRegistration_Returns201() throws Exception {
        URL url = new URL("http://localhost:8081/api/register");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String uniqueEmail = "alice" + System.currentTimeMillis() + "@project.com";
        String jsonPayload = "{\"name\":\"Alice\", \"phone\":\"5551234\", \"email\":\"" + uniqueEmail + "\", \"password\":\"secret123\"}";
        
        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonPayload.getBytes());
            os.flush();
        }

        assertEquals(201, conn.getResponseCode(), "Should return HTTP 201 Created");
    }
}
