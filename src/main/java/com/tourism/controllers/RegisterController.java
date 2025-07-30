package com.tourism.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.tourism.models.User;
import com.tourism.utils.FileManager;

public class RegisterController {
    
    @FXML private Label titleLabel;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private Label languagesLabel;
    @FXML private TextField languagesField;
    @FXML private Label experienceLabel;
    @FXML private TextField experienceField;
    @FXML private Button registerButton;
    @FXML private Button backButton;
    @FXML private Button languageToggle;
    @FXML private Label errorLabel;
    
    private boolean isNepali = false;
    
    @FXML
    private void initialize() {
        // Initialize role combo box
        roleComboBox.getItems().addAll("Tourist", "Guide");
        roleComboBox.setValue("Tourist");
        
        // Show/hide guide-specific fields based on role selection
        roleComboBox.setOnAction(e -> updateFieldVisibility());
        
        updateLanguage();
        updateFieldVisibility();
    }
    
    @FXML
    private void handleRegister() {
        if (!validateFields()) {
            return;
        }
        
        try {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            String fullName = fullNameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String role = roleComboBox.getValue().toLowerCase();
            String languages = languagesField.getText().trim();
            String experience = experienceField.getText().trim();
            
            // Check if username already exists
            if (FileManager.userExists(username)) {
                showError(isNepali ? "प्रयोगकर्ता नाम पहिले नै अवस्थित छ" : "Username already exists");
                return;
            }
            
            // Create new user
            User newUser = new User(username, password, fullName, email, phone, role, languages, experience);
            
            // Save user
            if (FileManager.saveUser(newUser)) {
                showSuccess(isNepali ? "सफलतापूर्वक दर्ता भयो!" : "Registration successful!");
                
                // Go back to login after 2 seconds
                new Thread(() -> {
                    try {
                        Thread.sleep(2000);
                        javafx.application.Platform.runLater(this::goBack);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            } else {
                showError(isNepali ? "दर्ता असफल" : "Registration failed");
            }
            
        } catch (Exception e) {
            showError("Registration error: " + e.getMessage());
        }
    }
    
    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            showError("Error going back: " + e.getMessage());
        }
    }
    
    @FXML
    private void toggleLanguage() {
        isNepali = !isNepali;
        updateLanguage();
    }
    
    private void updateLanguage() {
        if (isNepali) {
            titleLabel.setText("नयाँ प्रयोगकर्ता दर्ता");
            registerButton.setText("दर्ता");
            backButton.setText("फिर्ता");
            languageToggle.setText("English");
            languagesLabel.setText("भाषाहरू:");
            experienceLabel.setText("अनुभव:");
        } else {
            titleLabel.setText("Register New User");
            registerButton.setText("Register");
            backButton.setText("Back");
            languageToggle.setText("नेपाली");
            languagesLabel.setText("Languages:");
            experienceLabel.setText("Experience:");
        }
    }
    
    private void updateFieldVisibility() {
        boolean isGuide = "Guide".equals(roleComboBox.getValue());
        languagesLabel.setVisible(isGuide);
        languagesField.setVisible(isGuide);
        experienceLabel.setVisible(isGuide);
        experienceField.setVisible(isGuide);
    }
    
    private boolean validateFields() {
        if (usernameField.getText().trim().isEmpty() ||
            passwordField.getText().trim().isEmpty() ||
            fullNameField.getText().trim().isEmpty() ||
            emailField.getText().trim().isEmpty() ||
            phoneField.getText().trim().isEmpty()) {
            
            showError(isNepali ? "कृपया सबै आवश्यक फिल्डहरू भर्नुहोस्" : "Please fill all required fields");
            return false;
        }
        
        if ("Guide".equals(roleComboBox.getValue()) && 
            (languagesField.getText().trim().isEmpty() || experienceField.getText().trim().isEmpty())) {
            showError(isNepali ? "गाइडका लागि भाषा र अनुभव आवश्यक छ" : "Languages and experience required for guides");
            return false;
        }
        
        return true;
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: red;");
        errorLabel.setVisible(true);
    }
    
    private void showSuccess(String message) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: green;");
        errorLabel.setVisible(true);
    }
}
