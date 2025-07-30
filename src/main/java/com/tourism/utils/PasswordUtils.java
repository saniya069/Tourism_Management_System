package com.tourism.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtils {
    
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return password; // Fallback to plain text (not recommended for production)
        }
    }
    
    public static boolean verifyPassword(String password, String hashedPassword) {
        return hashPassword(password).equals(hashedPassword);
    }
}
