package com.medcare.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class EHRRecord {
    private Long id;
    private Long patientId;
    private LocalDate recordDate;
    private RecordType type;
    private String title;
    private String description;
    private Long doctorId;
    private String doctorName;
    private List<String> attachments;
    private LocalDateTime createdAt;
    
    public enum RecordType {
        DIAGNOSIS, TREATMENT, TEST_RESULT, PRESCRIPTION
    }
    
    // Constructors
    public EHRRecord() {}
    
    public EHRRecord(Long patientId, RecordType type, String title, String description,
                    Long doctorId, String doctorName) {
        this.patientId = patientId;
        this.recordDate = LocalDate.now();
        this.type = type;
        this.title = title;
        this.description = description;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    
    public LocalDate getRecordDate() { return recordDate; }
    public void setRecordDate(LocalDate recordDate) { this.recordDate = recordDate; }
    
    public RecordType getType() { return type; }
    public void setType(RecordType type) { this.type = type; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
    
    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    
    public List<String> getAttachments() { return attachments; }
    public void setAttachments(List<String> attachments) { this.attachments = attachments; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}