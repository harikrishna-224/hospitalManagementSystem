package com.medcare.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class HttpServer {
    private com.sun.net.httpserver.HttpServer server;
    private final int port;
    
    public HttpServer(int port) {
        this.port = port;
    }
    
    public void start() throws IOException {
        server = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(port), 0);
        
        // Set up routes
        server.createContext("/api/auth", new AuthController());
        server.createContext("/api/patients", new PatientController());
        server.createContext("/api/appointments", new AppointmentController());
        server.createContext("/api/ehr", new EHRController());
        server.createContext("/api/inventory", new InventoryController());
        server.createContext("/api/billing", new BillingController());
        
        // CORS handler
        server.createContext("/", new CorsHandler());
        
        server.setExecutor(Executors.newFixedThreadPool(10));
        server.start();
        
        System.out.println("Server started on port " + port);
    }
    
    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }
    
    // CORS Handler
    static class CorsHandler implements HttpHandler {
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
            
            // Default 404 response
            String response = "{\"error\":\"Not Found\"}";
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(404, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}