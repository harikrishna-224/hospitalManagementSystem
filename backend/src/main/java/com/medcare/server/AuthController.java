package com.medcare.server;

import com.medcare.model.User;
import com.medcare.service.AuthService;
import com.medcare.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public class AuthController implements HttpHandler {
    private final AuthService authService;
    
    public AuthController() {
        this.authService = new AuthService();
    }
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Add CORS headers
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");
        
        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(200, -1);
            return;
        }
        
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        
        try {
            if (path.endsWith("/login") && "POST".equals(method)) {
                handleLogin(exchange);
            } else if (path.endsWith("/register") && "POST".equals(method)) {
                handleRegister(exchange);
            } else {
                sendResponse(exchange, 404, "{\"error\":\"Not Found\"}");
            }
        } catch (Exception e) {
            sendResponse(exchange, 500, "{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
    
    private void handleLogin(HttpExchange exchange) throws IOException {
        String requestBody = readRequestBody(exchange);
        Map<String, Object> loginData = JsonUtil.fromJson(requestBody, Map.class);
        
        String email = (String) loginData.get("email");
        String password = (String) loginData.get("password");
        
        User user = authService.authenticate(email, password);
        if (user != null) {
            String token = authService.generateToken(user);
            String response = String.format(
                "{\"user\":{\"id\":%d,\"name\":\"%s\",\"email\":\"%s\",\"role\":\"%s\"},\"token\":\"%s\"}",
                user.getId(), user.getName(), user.getEmail(), user.getRole(), token
            );
            sendResponse(exchange, 200, response);
        } else {
            sendResponse(exchange, 401, "{\"error\":\"Invalid credentials\"}");
        }
    }
    
    private void handleRegister(HttpExchange exchange) throws IOException {
        String requestBody = readRequestBody(exchange);
        Map<String, Object> registerData = JsonUtil.fromJson(requestBody, Map.class);
        
        String name = (String) registerData.get("name");
        String email = (String) registerData.get("email");
        String password = (String) registerData.get("password");
        String roleStr = (String) registerData.get("role");
        
        User.Role role = User.Role.valueOf(roleStr.toUpperCase());
        
        try {
            User user = authService.register(name, email, password, role);
            String token = authService.generateToken(user);
            String response = String.format(
                "{\"user\":{\"id\":%d,\"name\":\"%s\",\"email\":\"%s\",\"role\":\"%s\"},\"token\":\"%s\"}",
                user.getId(), user.getName(), user.getEmail(), user.getRole(), token
            );
            sendResponse(exchange, 201, response);
        } catch (RuntimeException e) {
            sendResponse(exchange, 400, "{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
    
    private String readRequestBody(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        return new String(inputStream.readAllBytes());
    }
    
    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}