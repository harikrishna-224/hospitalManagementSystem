package com.medcare.server;

import com.medcare.model.Patient;
import com.medcare.service.PatientService;
import com.medcare.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PatientController implements HttpHandler {
    private final PatientService patientService;
    
    public PatientController() {
        this.patientService = new PatientService();
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
            if (path.equals("/api/patients") && "GET".equals(method)) {
                handleGetAllPatients(exchange);
            } else if (path.equals("/api/patients") && "POST".equals(method)) {
                handleCreatePatient(exchange);
            } else if (path.matches("/api/patients/\\d+") && "GET".equals(method)) {
                Long id = extractIdFromPath(path);
                handleGetPatient(exchange, id);
            } else if (path.matches("/api/patients/\\d+") && "PUT".equals(method)) {
                Long id = extractIdFromPath(path);
                handleUpdatePatient(exchange, id);
            } else if (path.matches("/api/patients/\\d+") && "DELETE".equals(method)) {
                Long id = extractIdFromPath(path);
                handleDeletePatient(exchange, id);
            } else {
                sendResponse(exchange, 404, "{\"error\":\"Not Found\"}");
            }
        } catch (Exception e) {
            sendResponse(exchange, 500, "{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
    
    private void handleGetAllPatients(HttpExchange exchange) throws IOException {
        List<Patient> patients = patientService.getAllPatients();
        String response = JsonUtil.toJson(patients);
        sendResponse(exchange, 200, response);
    }
    
    private void handleGetPatient(HttpExchange exchange, Long id) throws IOException {
        Patient patient = patientService.getPatientById(id);
        if (patient != null) {
            String response = JsonUtil.toJson(patient);
            sendResponse(exchange, 200, response);
        } else {
            sendResponse(exchange, 404, "{\"error\":\"Patient not found\"}");
        }
    }
    
    private void handleCreatePatient(HttpExchange exchange) throws IOException {
        String requestBody = readRequestBody(exchange);
        Map<String, Object> patientData = JsonUtil.fromJson(requestBody, Map.class);
        
        Patient patient = mapToPatient(patientData);
        Patient createdPatient = patientService.createPatient(patient);
        
        String response = JsonUtil.toJson(createdPatient);
        sendResponse(exchange, 201, response);
    }
    
    private void handleUpdatePatient(HttpExchange exchange, Long id) throws IOException {
        String requestBody = readRequestBody(exchange);
        Map<String, Object> patientData = JsonUtil.fromJson(requestBody, Map.class);
        
        Patient patient = mapToPatient(patientData);
        Patient updatedPatient = patientService.updatePatient(id, patient);
        
        String response = JsonUtil.toJson(updatedPatient);
        sendResponse(exchange, 200, response);
    }
    
    private void handleDeletePatient(HttpExchange exchange, Long id) throws IOException {
        boolean deleted = patientService.deletePatient(id);
        if (deleted) {
            sendResponse(exchange, 200, "{\"message\":\"Patient deleted successfully\"}");
        } else {
            sendResponse(exchange, 404, "{\"error\":\"Patient not found\"}");
        }
    }
    
    private Patient mapToPatient(Map<String, Object> data) {
        Patient patient = new Patient();
        patient.setName((String) data.get("name"));
        patient.setEmail((String) data.get("email"));
        patient.setPhone((String) data.get("phone"));
        patient.setDateOfBirth(LocalDate.parse((String) data.get("dateOfBirth")));
        patient.setGender(Patient.Gender.valueOf(((String) data.get("gender")).toUpperCase()));
        patient.setAddress((String) data.get("address"));
        patient.setEmergencyContact((String) data.get("emergencyContact"));
        patient.setBloodType((String) data.get("bloodType"));
        
        if (data.get("allergies") != null) {
            patient.setAllergies(Arrays.asList(((String) data.get("allergies")).split(",")));
        }
        
        if (data.get("medications") != null) {
            patient.setMedications(Arrays.asList(((String) data.get("medications")).split(",")));
        }
        
        return patient;
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