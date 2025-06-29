package com.medcare.dao;

import com.medcare.model.Appointment;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO extends BaseDAO {
    
    public Appointment findById(Long id) {
        String sql = "SELECT * FROM appointments WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToAppointment(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Appointment> findAll() {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments ORDER BY appointment_date DESC, appointment_time DESC";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }
    
    public List<Appointment> findByPatientId(Long patientId) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE patient_id = ? ORDER BY appointment_date DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, patientId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }
    
    public List<Appointment> findByDate(LocalDate date) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE appointment_date = ? ORDER BY appointment_time";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(date));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }
    
    public Appointment save(Appointment appointment) {
        if (appointment.getId() == null) {
            return insert(appointment);
        } else {
            return update(appointment);
        }
    }
    
    private Appointment insert(Appointment appointment) {
        String sql = """
            INSERT INTO appointments (patient_id, patient_name, doctor_id, doctor_name, 
                                    appointment_date, appointment_time, duration, type, status, notes) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setLong(1, appointment.getPatientId());
            stmt.setString(2, appointment.getPatientName());
            stmt.setLong(3, appointment.getDoctorId());
            stmt.setString(4, appointment.getDoctorName());
            stmt.setDate(5, Date.valueOf(appointment.getAppointmentDate()));
            stmt.setTime(6, Time.valueOf(appointment.getAppointmentTime()));
            stmt.setInt(7, appointment.getDuration());
            stmt.setString(8, appointment.getType().name().toLowerCase());
            stmt.setString(9, appointment.getStatus().name().toLowerCase());
            stmt.setString(10, appointment.getNotes());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    appointment.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointment;
    }
    
    private Appointment update(Appointment appointment) {
        String sql = """
            UPDATE appointments SET patient_id = ?, patient_name = ?, doctor_id = ?, doctor_name = ?, 
                                  appointment_date = ?, appointment_time = ?, duration = ?, type = ?, 
                                  status = ?, notes = ? 
            WHERE id = ?
        """;
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, appointment.getPatientId());
            stmt.setString(2, appointment.getPatientName());
            stmt.setLong(3, appointment.getDoctorId());
            stmt.setString(4, appointment.getDoctorName());
            stmt.setDate(5, Date.valueOf(appointment.getAppointmentDate()));
            stmt.setTime(6, Time.valueOf(appointment.getAppointmentTime()));
            stmt.setInt(7, appointment.getDuration());
            stmt.setString(8, appointment.getType().name().toLowerCase());
            stmt.setString(9, appointment.getStatus().name().toLowerCase());
            stmt.setString(10, appointment.getNotes());
            stmt.setLong(11, appointment.getId());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointment;
    }
    
    public boolean delete(Long id) {
        String sql = "DELETE FROM appointments WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private Appointment mapResultSetToAppointment(ResultSet rs) throws SQLException {
        Appointment appointment = new Appointment();
        appointment.setId(rs.getLong("id"));
        appointment.setPatientId(rs.getLong("patient_id"));
        appointment.setPatientName(rs.getString("patient_name"));
        appointment.setDoctorId(rs.getLong("doctor_id"));
        appointment.setDoctorName(rs.getString("doctor_name"));
        appointment.setAppointmentDate(rs.getDate("appointment_date").toLocalDate());
        appointment.setAppointmentTime(rs.getTime("appointment_time").toLocalTime());
        appointment.setDuration(rs.getInt("duration"));
        appointment.setType(Appointment.AppointmentType.valueOf(rs.getString("type").toUpperCase()));
        appointment.setStatus(Appointment.AppointmentStatus.valueOf(rs.getString("status").toUpperCase()));
        appointment.setNotes(rs.getString("notes"));
        appointment.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return appointment;
    }
}