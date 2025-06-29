package com.medcare.server;

import com.medcare.model.Appointment;
import com.medcare.service.AppointmentService;
import com.medcare.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public class AppointmentController implements HttpHandler {
    private final AppointmentService appointmentService;
    
    public AppointmentController() {
        this.appointmentService = new AppointmentService();
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
            if (path.equals("/api/appointments") && "GET".equals(method)) {
                handleGetAllAppointments(exchange);
            } else if (path.equals("/api/appointments") && "POST".equals(method)) {
                handleCreateAppointment(exchange);
            } else if (path.matches("/api/appointments/\\d+") && "GET".equals(method)) {
                Long id = extractIdFromPath(path);
                handleGetAppointment(exchange, id);
            } else if (path.matches("/api/appointments/\\d+") && "PUT".equals(method)) {
                Long id = extractIdFromPath(path);
                handleUpdateAppointment(exchange, id);
            } else if (path.matches("/api/appointments/\\d+") && "DELETE".equals(method)) {
                Long id = extractIdFromPath(path);
                handleDeleteAppointment(exchange, id);
            } else {
                sendResponse(exchange, 404, "{\"error\":\"Not Found\"}");
            }
        } catch (Exception e) {
            sendResponse(exchange, 500, "{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
    
    private void handleGetAllAppointments(HttpExchange exchange) throws IOException {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        String response = JsonUtil.toJson(appointments);
        sendResponse(exchange, 200, response);
    }
    
    private void handleGetAppointment(HttpExchange exchange, Long id) throws IOException {
        Appointment appointment = appointmentService.getAppointmentById(id);
        if (appointment != null) {
            String response = JsonUtil.toJson(appointment);
            sendResponse(exchange, 200, response);
        } else {
            sendResponse(exchange, 404, "{\"error\":\"Appointment not found\"}");
        }
    }
    
    private void handleCreateAppointment(HttpExchange exchange) throws IOException {
        String requestBody = readRequestBody(exchange);
        Map<String, Object> appointmentData = JsonUtil.fromJson(requestBody, Map.class);
        
        Appointment appointment = mapToAppointment(appointmentData);
        Appointment createdAppointment = appointmentService.createAppointment(appointment);
        
        String response = JsonUtil.toJson(createdAppointment);
        sendResponse(exchange, 201, response);
    }
    
    private void handleUpdateAppointment(HttpExchange exchange, Long id) throws IOException {
        String requestBody = readRequestBody(exchange);
        Map<String, Object> appointmentData = JsonUtil.fromJson(requestBody, Map.class);
        
        Appointment appointment = mapToAppointment(appointmentData);
        Appointment updatedAppointment = appointmentService.updateAppointment(id, appointment);
        
        String response = JsonUtil.toJson(updatedAppointment);
        sendResponse(exchange, 200, response);
    }
    
    private void handleDeleteAppointment(HttpExchange exchange, Long id) throws IOException {
        boolean deleted = appointmentService.deleteAppointment(id);
        if (deleted) {
            sendResponse(exchange, 200, "{\"message\":\"Appointment deleted successfully\"}");
        } else {
            sendResponse(exchange, 404, "{\"error\":\"Appointment not found\"}");
        }
    }
    
    private Appointment mapToAppointment(Map<String, Object> data) {
        Appointment appointment = new Appointment();
        appointment.setPatientId(Long.valueOf(data.get("patientId").toString()));
        appointment.setPatientName((String) data.get("patientName"));
        appointment.setDoctorId(Long.valueOf(data.get("doctorId").toString()));
        appointment.setDoctorName((String) data.get("doctorName"));
        appointment.setAppointmentDate(LocalDate.parse((String) data.get("date")));
        appointment.setAppointmentTime(LocalTime.parse((String) data.get("time")));
        appointment.setDuration(Integer.valueOf(data.get("duration").toString()));
        appointment.setType(Appointment.AppointmentType.valueOf(((String) data.get("type")).toUpperCase()));
        appointment.setNotes((String) data.get("notes"));
        
        if (data.get("status") != null) {
            appointment.setStatus(Appointment.AppointmentStatus.valueOf(((String) data.get("status")).toUpperCase()));
        }
        
        return appointment;
    }
    
    private Long extractIdFromPath(String path) {
        String[] parts = path.split("/");
        return Long.parseLong(parts[parts.length - 1]);
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