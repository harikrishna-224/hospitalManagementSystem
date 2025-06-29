package com.medcare.service;

import com.medcare.dao.UserDAO;
import com.medcare.model.User;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AuthService {
    private final UserDAO userDAO;
    
    public AuthService() {
        this.userDAO = new UserDAO();
    }
    
    public User authenticate(String email, String password) {
        User user = userDAO.findByEmail(email);
        if (user != null && verifyPassword(password, user.getPassword())) {
            return user;
        }
        return null;
    }
    
    public User register(String name, String email, String password, User.Role role) {
        // Check if user already exists
        if (userDAO.findByEmail(email) != null) {
            throw new RuntimeException("User with email already exists");
        }
        
        String hashedPassword = hashPassword(password);
        User user = new User(name, email, hashedPassword, role);
        return userDAO.save(user);
    }
    
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
    
    private boolean verifyPassword(String password, String hashedPassword) {
        // For demo purposes, we'll do simple string comparison
        // In production, you should use proper password hashing
        return password.equals(hashedPassword);
    }
    
    public String generateToken(User user) {
        // Simple token generation for demo
        // In production, use JWT or similar
        return Base64.getEncoder().encodeToString(
            (user.getId() + ":" + user.getEmail() + ":" + System.currentTimeMillis()).getBytes()
        );
    }
    
    public User validateToken(String token) {
        try {
            String decoded = new String(Base64.getDecoder().decode(token));
            String[] parts = decoded.split(":");
            if (parts.length >= 2) {
                Long userId = Long.parseLong(parts[0]);
                return userDAO.findById(userId);
            }
        } catch (Exception e) {
            // Invalid token
        }
        return null;
    }
}