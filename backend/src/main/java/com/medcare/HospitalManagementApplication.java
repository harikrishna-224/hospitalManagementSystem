package com.medcare;

import com.medcare.config.DatabaseConfig;
import com.medcare.server.HttpServer;

public class HospitalManagementApplication {
    public static void main(String[] args) {
        try {
            // Initialize database
            DatabaseConfig.initializeDatabase();
            
            // Start HTTP server
            HttpServer server = new HttpServer(8080);
            server.start();
            
            System.out.println("Hospital Management System started on port 8080");
            
            // Keep the application running
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Shutting down Hospital Management System...");
                server.stop();
            }));
            
        } catch (Exception e) {
            System.err.println("Failed to start Hospital Management System: " + e.getMessage());
            e.printStackTrace();
        }
    }
}