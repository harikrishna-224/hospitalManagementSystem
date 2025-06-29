package com.medcare.dao;

import com.medcare.model.Patient;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PatientDAO extends BaseDAO {
    
    public Patient findById(Long id) {
        String sql = "SELECT * FROM patients WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToPatient(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Patient> findAll() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients ORDER BY created_at DESC";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                patients.add(mapResultSetToPatient(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }
    
    public List<Patient> searchByName(String name) {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients WHERE LOWER(name) LIKE ? ORDER BY name";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + name.toLowerCase() + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                patients.add(mapResultSetToPatient(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }
    
    public Patient save(Patient patient) {
        if (patient.getId() == null) {
            return insert(patient);
        } else {
            return update(patient);
        }
    }
    
    private Patient insert(Patient patient) {
        String sql = """
            INSERT INTO patients (name, email, phone, date_of_birth, gender, address, 
                                emergency_contact, blood_type, allergies, medications) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, patient.getName());
            stmt.setString(2, patient.getEmail());
            stmt.setString(3, patient.getPhone());
            stmt.setDate(4, Date.valueOf(patient.getDateOfBirth()));
            stmt.setString(5, patient.getGender().name().toLowerCase());
            stmt.setString(6, patient.getAddress());
            stmt.setString(7, patient.getEmergencyContact());
            stmt.setString(8, patient.getBloodType());
            stmt.setString(9, patient.getAllergies() != null ? String.join(",", patient.getAllergies()) : "");
            stmt.setString(10, patient.getMedications() != null ? String.join(",", patient.getMedications()) : "");
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    patient.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patient;
    }
    
    private Patient update(Patient patient) {
        String sql = """
            UPDATE patients SET name = ?, email = ?, phone = ?, date_of_birth = ?, 
                              gender = ?, address = ?, emergency_contact = ?, blood_type = ?, 
                              allergies = ?, medications = ? 
            WHERE id = ?
        """;
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, patient.getName());
            stmt.setString(2, patient.getEmail());
            stmt.setString(3, patient.getPhone());
            stmt.setDate(4, Date.valueOf(patient.getDateOfBirth()));
            stmt.setString(5, patient.getGender().name().toLowerCase());
            stmt.setString(6, patient.getAddress());
            stmt.setString(7, patient.getEmergencyContact());
            stmt.setString(8, patient.getBloodType());
            stmt.setString(9, patient.getAllergies() != null ? String.join(",", patient.getAllergies()) : "");
            stmt.setString(10, patient.getMedications() != null ? String.join(",", patient.getMedications()) : "");
            stmt.setLong(11, patient.getId());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patient;
    }
    
    public boolean delete(Long id) {
        String sql = "DELETE FROM patients WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private Patient mapResultSetToPatient(ResultSet rs) throws SQLException {
        Patient patient = new Patient();
        patient.setId(rs.getLong("id"));
        patient.setName(rs.getString("name"));
        patient.setEmail(rs.getString("email"));
        patient.setPhone(rs.getString("phone"));
        patient.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
        patient.setGender(Patient.Gender.valueOf(rs.getString("gender").toUpperCase()));
        patient.setAddress(rs.getString("address"));
        patient.setEmergencyContact(rs.getString("emergency_contact"));
        patient.setBloodType(rs.getString("blood_type"));
        
        String allergies = rs.getString("allergies");
        if (allergies != null && !allergies.trim().isEmpty()) {
            patient.setAllergies(Arrays.asList(allergies.split(",")));
        }
        
        String medications = rs.getString("medications");
        if (medications != null && !medications.trim().isEmpty()) {
            patient.setMedications(Arrays.asList(medications.split(",")));
        }
        
        patient.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return patient;
    }
}