package com.tourism.controllers;

import com.tourism.models.User;
import com.tourism.utils.FileManager;
import com.tourism.utils.LanguageManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;

public class RegisterController {
    
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private TextField languagesField;
    @FXML private TextField experienceField;
    @FXML private Button registerButton;
    @FXML private Button backButton;
    @FXML private Button languageToggle;
    @FXML private Label titleLabel;
    @FXML private Label errorLabel;
    @FXML private Label languagesLabel;
    @FXML private Label experienceLabel;
    
    private LanguageManager languageManager = LanguageManager.getInstance();
    
    @FXML
    private void initialize() {
        roleComboBox.getItems().addAll("Tourist", "Guide");
        roleComboBox.setOnAction(e -> toggleGuideFields());
        updateLanguage();
        hideGuideFields();
        errorLabel.setVisible(false);
    }
    
    @FXML
    private void handleRegister() {
        if (!validateFields()) {
            return;
        }
        
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String role = roleComboBox.getValue();
        
        if (FileManager.userExists(username)) {
            showError("Username already exists!");
            return;
        }
        
        User user = new User(username, password, fullName, email, phone, role);
        
        if (role.equals("Guide")) {
            user.setLanguages(languagesField.getText().trim());
            user.setExperience(experienceField.getText().trim());
        }
        
        String fileName = role.equals("Tourist") ? "tourists.txt" : "guides.txt";
        
        System.out.println("Registering user: " + username + " with password: " + password + " in file: " + fileName);
        
        if (FileManager.saveUser(user, fileName)) {
            showSuccess("Registration successful!");
            
            Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
            infoAlert.setTitle("Registration Successful");
            infoAlert.setHeaderText("Account Created Successfully!");
            infoAlert.setContentText("Username: " + username + "\nPassword: " + password + "\nRole: " + role + 
                                   "\n\nYou can now login with these credentials.");
            infoAlert.showAndWait();
            
            clearFields();
            goBack();
        } else {
            showError("Registration failed!");
        }
    }
    
    @FXML
    private void goBack() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void toggleLanguage() {
        languageManager.toggleLanguage();
        updateLanguage();
    }
    
    private void toggleGuideFields() {
        boolean isGuide = "Guide".equals(roleComboBox.getValue());
        languagesField.setVisible(isGuide);
        experienceField.setVisible(isGuide);
        languagesLabel.setVisible(isGuide);
        experienceLabel.setVisible(isGuide);
    }
    
    private void hideGuideFields() {
        languagesField.setVisible(false);
        experienceField.setVisible(false);
        languagesLabel.setVisible(false);
        experienceLabel.setVisible(false);
    }
    
    private boolean validateFields() {
        if (usernameField.getText().trim().isEmpty() ||
            passwordField.getText().isEmpty() ||
            fullNameField.getText().trim().isEmpty() ||
            emailField.getText().trim().isEmpty() ||
            phoneField.getText().trim().isEmpty() ||
            roleComboBox.getValue() == null) {
            
            showError("Please fill in all required fields!");
            return false;
        }
        
        String username = usernameField.getText().trim();
        if (username.length() < 3) {
            showError("Username must be at least 3 characters long!");
            return false;
        }
        
        String password = passwordField.getText();
        if (password.length() < 3) {
            showError("Password must be at least 3 characters long!");
            return false;
        }
        
        if ("Guide".equals(roleComboBox.getValue())) {
            if (languagesField.getText().trim().isEmpty() ||
                experienceField.getText().trim().isEmpty()) {
                showError("Languages and experience are required for guides!");
                return false;
            }
        }
        
        return true;
    }
    
    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
        fullNameField.clear();
        emailField.clear();
        phoneField.clear();
        roleComboBox.setValue(null);
        languagesField.clear();
        experienceField.clear();
        hideGuideFields();
    }
    
    private void updateLanguage() {
        titleLabel.setText(languageManager.getText("register.title"));
        registerButton.setText(languageManager.getText("register.button"));
        backButton.setText(languageManager.getText("back.button"));
        languageToggle.setText(languageManager.getCurrentLanguage().equals("EN") ? "नेपाली" : "English");
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
