package com.medcare.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class InventoryItem {
    private Long id;
    private String name;
    private Category category;
    private int quantity;
    private int minStock;
    private String unit;
    private String supplier;
    private LocalDate expiryDate;
    private BigDecimal cost;
    private String location;
    private LocalDateTime createdAt;
    
    public enum Category {
        MEDICATION, EQUIPMENT, SUPPLIES
    }
    
    // Constructors
    public InventoryItem() {}
    
    public InventoryItem(String name, Category category, int quantity, int minStock,
                        String unit, String supplier, BigDecimal cost, String location) {
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.minStock = minStock;
        this.unit = unit;
        this.supplier = supplier;
        this.cost = cost;
        this.location = location;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    
    public int getMinStock() { return minStock; }
    public void setMinStock(int minStock) { this.minStock = minStock; }
    
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    
    public String getSupplier() { return supplier; }
    public void setSupplier(String supplier) { this.supplier = supplier; }
    
    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    
    public BigDecimal getCost() { return cost; }
    public void setCost(BigDecimal cost) { this.cost = cost; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}