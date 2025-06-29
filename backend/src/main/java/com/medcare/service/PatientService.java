package com.medcare.service;

import com.medcare.dao.PatientDAO;
import com.medcare.model.Patient;
import java.util.List;

public class PatientService {
    private final PatientDAO patientDAO;
    
    public PatientService() {
        this.patientDAO = new PatientDAO();
    }
    
    public List<Patient> getAllPatients() {
        return patientDAO.findAll();
    }
    
    public Patient getPatientById(Long id) {
        return patientDAO.findById(id);
    }
    
    public List<Patient> searchPatients(String name) {
        return patientDAO.searchByName(name);
    }
    
    public Patient createPatient(Patient patient) {
        validatePatient(patient);
        return patientDAO.save(patient);
    }
    
    public Patient updatePatient(Long id, Patient patient) {
        Patient existingPatient = patientDAO.findById(id);
        if (existingPatient == null) {
            throw new RuntimeException("Patient not found");
        }
        
        patient.setId(id);
        validatePatient(patient);
        return patientDAO.save(patient);
    }
    
    public boolean deletePatient(Long id) {
        Patient patient = patientDAO.findById(id);
        if (patient == null) {
            throw new RuntimeException("Patient not found");
        }
        return patientDAO.delete(id);
    }
    
    private void validatePatient(Patient patient) {
        if (patient.getName() == null || patient.getName().trim().isEmpty()) {
            throw new RuntimeException("Patient name is required");
        }
        if (patient.getEmail() == null || patient.getEmail().trim().isEmpty()) {
            throw new RuntimeException("Patient email is required");
        }
        if (patient.getPhone() == null || patient.getPhone().trim().isEmpty()) {
            throw new RuntimeException("Patient phone is required");
        }
        if (patient.getDateOfBirth() == null) {
            throw new RuntimeException("Patient date of birth is required");
        }
    }
}