package com.medcare.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConfig {
    private static final String DB_URL = "jdbc:h2:mem:hospital_db;DB_CLOSE_DELAY=-1";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
    
    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Create Users table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    email VARCHAR(255) UNIQUE NOT NULL,
                    password VARCHAR(255) NOT NULL,
                    role VARCHAR(50) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);
            
            // Create Patients table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS patients (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    email VARCHAR(255) UNIQUE NOT NULL,
                    phone VARCHAR(20) NOT NULL,
                    date_of_birth DATE NOT NULL,
                    gender VARCHAR(10) NOT NULL,
                    address TEXT NOT NULL,
                    emergency_contact VARCHAR(20) NOT NULL,
                    blood_type VARCHAR(5),
                    allergies TEXT,
                    medications TEXT,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);
            
            // Create Appointments table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS appointments (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    patient_id BIGINT NOT NULL,
                    patient_name VARCHAR(255) NOT NULL,
                    doctor_id BIGINT NOT NULL,
                    doctor_name VARCHAR(255) NOT NULL,
                    appointment_date DATE NOT NULL,
                    appointment_time TIME NOT NULL,
                    duration INT NOT NULL,
                    type VARCHAR(50) NOT NULL,
                    status VARCHAR(50) NOT NULL,
                    notes TEXT,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (patient_id) REFERENCES patients(id)
                )
            """);
            
            // Create EHR Records table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS ehr_records (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    patient_id BIGINT NOT NULL,
                    record_date DATE NOT NULL,
                    type VARCHAR(50) NOT NULL,
                    title VARCHAR(255) NOT NULL,
                    description TEXT NOT NULL,
                    doctor_id BIGINT NOT NULL,
                    doctor_name VARCHAR(255) NOT NULL,
                    attachments TEXT,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (patient_id) REFERENCES patients(id)
                )
            """);
            
            // Create Inventory table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS inventory (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    category VARCHAR(50) NOT NULL,
                    quantity INT NOT NULL,
                    min_stock INT NOT NULL,
                    unit VARCHAR(50) NOT NULL,
                    supplier VARCHAR(255) NOT NULL,
                    expiry_date DATE,
                    cost DECIMAL(10,2) NOT NULL,
                    location VARCHAR(255) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);
            
            // Create Bills table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS bills (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    patient_id BIGINT NOT NULL,
                    patient_name VARCHAR(255) NOT NULL,
                    bill_date DATE NOT NULL,
                    subtotal DECIMAL(10,2) NOT NULL,
                    tax DECIMAL(10,2) NOT NULL,
                    total DECIMAL(10,2) NOT NULL,
                    status VARCHAR(50) NOT NULL,
                    due_date DATE NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (patient_id) REFERENCES patients(id)
                )
            """);
            
            // Create Bill Items table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS bill_items (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    bill_id BIGINT NOT NULL,
                    description VARCHAR(255) NOT NULL,
                    quantity INT NOT NULL,
                    unit_price DECIMAL(10,2) NOT NULL,
                    total DECIMAL(10,2) NOT NULL,
                    FOREIGN KEY (bill_id) REFERENCES bills(id)
                )
            """);
            
            // Insert sample data
            insertSampleData(stmt);
            
            System.out.println("Database initialized successfully");
            
        } catch (SQLException e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void insertSampleData(Statement stmt) throws SQLException {
        // Insert sample users
        stmt.execute("""
            INSERT INTO users (name, email, password, role) VALUES
            ('Dr. Harikrishna', 'gurijalaharikrishna4@gmail.com', 'admin123', 'ADMIN'),
            ('Dr. Michael Chen', 'michael@hospital.com', 'doctor123', 'DOCTOR'),
            ('Nurse Lisa ', 'lisa@hospital.com', 'nurse123', 'NURSE')
        """);
        
        // Insert sample patients
        stmt.execute("""
            INSERT INTO patients (name, email, phone, date_of_birth, gender, address, emergency_contact, blood_type, allergies, medications) VALUES
            ('John Smith', 'john.smith@email.com', '+1-555-0123', '1985-06-15', 'male', '123 Main St, City, State 12345', '+1-555-0124', 'O+', 'Penicillin,Peanuts', 'Lisinopril 10mg'),
            ('Emily Johnson', 'emily.johnson@email.com', '+1-555-0125', '1990-03-22', 'female', '456 Oak Ave, City, State 12345', '+1-555-0126', 'A-', 'Latex', '')
        """);
        
        // Insert sample appointments
        stmt.execute("""
            INSERT INTO appointments (patient_id, patient_name, doctor_id, doctor_name, appointment_date, appointment_time, duration, type, status, notes) VALUES
            (1, 'John Smith', 2, 'Dr. Michael Chen', '2024-01-25', '10:00:00', 30, 'consultation', 'scheduled', 'Regular checkup'),
            (2, 'Emily Johnson', 1, 'Dr. Sarah Johnson', '2024-01-25', '14:30:00', 45, 'follow-up', 'scheduled', 'Follow-up for previous consultation')
        """);
        
        // Insert sample EHR records
        stmt.execute("""
            INSERT INTO ehr_records (patient_id, record_date, type, title, description, doctor_id, doctor_name, attachments) VALUES
            (1, '2024-01-20', 'diagnosis', 'Hypertension', 'Patient diagnosed with stage 1 hypertension. Blood pressure readings consistently above 140/90.', 2, 'Dr. Michael Chen', ''),
            (1, '2024-01-20', 'prescription', 'Lisinopril Prescription', 'Prescribed Lisinopril 10mg once daily for hypertension management.', 2, 'Dr. Michael Chen', '')
        """);
        
        // Insert sample inventory
        stmt.execute("""
            INSERT INTO inventory (name, category, quantity, min_stock, unit, supplier, expiry_date, cost, location) VALUES
            ('Paracetamol 500mg', 'medication', 150, 50, 'tablets', 'PharmaCorp', '2025-12-31', 0.15, 'Pharmacy-A1'),
            ('Digital Thermometer', 'equipment', 25, 10, 'pieces', 'MedEquip Ltd', NULL, 45.00, 'Equipment-B2'),
            ('Disposable Gloves', 'supplies', 500, 100, 'boxes', 'SafeSupply Co', NULL, 12.50, 'Supplies-C1')
        """);
        
        // Insert sample bills
        stmt.execute("""
            INSERT INTO bills (patient_id, patient_name, bill_date, subtotal, tax, total, status, due_date) VALUES
            (1, 'John Smith', '2024-01-20', 205.00, 20.50, 225.50, 'pending', '2024-02-20')
        """);
        
        // Insert sample bill items
        stmt.execute("""
            INSERT INTO bill_items (bill_id, description, quantity, unit_price, total) VALUES
            (1, 'Consultation Fee', 1, 150.00, 150.00),
            (1, 'Blood Pressure Test', 1, 25.00, 25.00),
            (1, 'Prescription', 1, 30.00, 30.00)
        """);
    }
}
