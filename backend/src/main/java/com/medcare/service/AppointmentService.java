package com.medcare.service;

import com.medcare.dao.AppointmentDAO;
import com.medcare.model.Appointment;
import java.time.LocalDate;
import java.util.List;

public class AppointmentService {
    private final AppointmentDAO appointmentDAO;
    
    public AppointmentService() {
        this.appointmentDAO = new AppointmentDAO();
    }
    
    public List<Appointment> getAllAppointments() {
        return appointmentDAO.findAll();
    }
    
    public Appointment getAppointmentById(Long id) {
        return appointmentDAO.findById(id);
    }
    
    public List<Appointment> getAppointmentsByPatient(Long patientId) {
        return appointmentDAO.findByPatientId(patientId);
    }
    
    public List<Appointment> getAppointmentsByDate(LocalDate date) {
        return appointmentDAO.findByDate(date);
    }
    
    public Appointment createAppointment(Appointment appointment) {
        validateAppointment(appointment);
        return appointmentDAO.save(appointment);
    }
    
    public Appointment updateAppointment(Long id, Appointment appointment) {
        Appointment existingAppointment = appointmentDAO.findById(id);
        if (existingAppointment == null) {
            throw new RuntimeException("Appointment not found");
        }
        
        appointment.setId(id);
        validateAppointment(appointment);
        return appointmentDAO.save(appointment);
    }
    
    public Appointment updateAppointmentStatus(Long id, Appointment.AppointmentStatus status) {
        Appointment appointment = appointmentDAO.findById(id);
        if (appointment == null) {
            throw new RuntimeException("Appointment not found");
        }
        
        appointment.setStatus(status);
        return appointmentDAO.save(appointment);
    }
    
    public boolean deleteAppointment(Long id) {
        Appointment appointment = appointmentDAO.findById(id);
        if (appointment == null) {
            throw new RuntimeException("Appointment not found");
        }
        return appointmentDAO.delete(id);
    }
    
    private void validateAppointment(Appointment appointment) {
        if (appointment.getPatientId() == null) {
            throw new RuntimeException("Patient ID is required");
        }
        if (appointment.getDoctorId() == null) {
            throw new RuntimeException("Doctor ID is required");
        }
        if (appointment.getAppointmentDate() == null) {
            throw new RuntimeException("Appointment date is required");
        }
        if (appointment.getAppointmentTime() == null) {
            throw new RuntimeException("Appointment time is required");
        }
        if (appointment.getDuration() <= 0) {
            throw new RuntimeException("Appointment duration must be positive");
        }
    }
}