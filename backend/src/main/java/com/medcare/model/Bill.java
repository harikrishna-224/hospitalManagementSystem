package com.medcare.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Bill {
    private Long id;
    private Long patientId;
    private String patientName;
    private LocalDate billDate;
    private List<BillItem> items;
    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal total;
    private BillStatus status;
    private LocalDate dueDate;
    private LocalDateTime createdAt;
    
    public enum BillStatus {
        PENDING, PAID, OVERDUE
    }
    
    // Constructors
    public Bill() {}
    
    public Bill(Long patientId, String patientName, List<BillItem> items, 
               BigDecimal subtotal, BigDecimal tax, LocalDate dueDate) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.billDate = LocalDate.now();
        this.items = items;
        this.subtotal = subtotal;
        this.tax = tax;
        this.total = subtotal.add(tax);
        this.status = BillStatus.PENDING;
        this.dueDate = dueDate;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    
    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    
    public LocalDate getBillDate() { return billDate; }
    public void setBillDate(LocalDate billDate) { this.billDate = billDate; }
    
    public List<BillItem> getItems() { return items; }
    public void setItems(List<BillItem> items) { this.items = items; }
    
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    
    public BigDecimal getTax() { return tax; }
    public void setTax(BigDecimal tax) { this.tax = tax; }
    
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    
    public BillStatus getStatus() { return status; }
    public void setStatus(BillStatus status) { this.status = status; }
    
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}