package com.project.server;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StaticFileHandlerTest {
    
    private HttpServer server;
    
    @BeforeEach
    public void setUp() throws Exception {
        server = HttpServer.create(new InetSocketAddress(8082), 0);
        server.createContext("/", new StaticFileHandler());
        server.start();
    }

    @AfterEach
    public void tearDown() {
        server.stop(0);
    }

    @Test
    public void testServeIndexHtml() throws Exception{
        URL url = new URL("http://localhost:8082/");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        assertEquals(200, conn.getResponseCode(), "Should return HTTP 200 OK for root path");
        assertEquals("text/html", conn.getContentType(), "Should return text/html content type");    
    }
}