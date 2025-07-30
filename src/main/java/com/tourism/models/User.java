package com.tourism.models;

public class User {
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String phone;
    private String role;
    private String languages;
    private String experience;
    
    public User(String username, String password, String fullName, String email, 
                String phone, String role, String languages, String experience) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.languages = languages;
        this.experience = experience;
    }
    
    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public String getLanguages() { return languages; }
    public void setLanguages(String languages) { this.languages = languages; }
    
    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }
    
    @Override
    public String toString() {
        return username + "," + password + "," + fullName + "," + email + "," + 
               phone + "," + role + "," + (languages != null ? languages : "") + "," + 
               (experience != null ? experience : "");
    }
    
    public static User fromString(String line) {
        String[] parts = line.split(",", 8);
        if (parts.length >= 6) {
            return new User(
                parts[0], parts[1], parts[2], parts[3], parts[4], parts[5],
                parts.length > 6 ? parts[6] : "",
                parts.length > 7 ? parts[7] : ""
            );
        }
        return null;
    }
}
