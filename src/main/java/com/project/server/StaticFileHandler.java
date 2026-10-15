package com.project.server;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class StaticFileHandler implements HttpHandler {
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        
        if (path.equals("/")) {
            path = "/index.html"; 
        } else if (path.equals("/register")) {
            path = "/register.html";
        }


        File frontendDir = new File("frontend");
        File requestedFile = new File(frontendDir, path);

        String canonicalFrontendPath = frontendDir.getCanonicalPath();
        String canonicalRequestedPath = requestedFile.getCanonicalPath();

        if (!canonicalRequestedPath.startsWith(canonicalFrontendPath)) {
            exchange.sendResponseHeaders(403, -1); 
            return;
        }

        if (!requestedFile.exists() || !requestedFile.isFile()) {
            exchange.sendResponseHeaders(404, -1); 
            return;
        }

        String contentType = "application/octet-stream";
        if (path.endsWith(".html")) contentType = "text/html";
        else if (path.endsWith(".css")) contentType = "text/css";
        else if (path.endsWith(".js")) contentType = "application/javascript";

        byte[] bytes = Files.readAllBytes(requestedFile.toPath());
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(200, bytes.length);
        
        try (var os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
