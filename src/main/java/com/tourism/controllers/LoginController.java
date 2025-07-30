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

public class LoginController {
    
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button registerButton;
    @FXML private Button languageToggle;
    @FXML private Label titleLabel;
    @FXML private Label usernameLabel;
    @FXML private Label passwordLabel;
    @FXML private Label errorLabel;
    
    private LanguageManager languageManager = LanguageManager.getInstance();
    
    @FXML
    private void initialize() {
        updateLanguage();
        errorLabel.setVisible(false);
    }
    
    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        
        if (username.isEmpty() || password.isEmpty()) {
            showError("Please fill in all fields!");
            return;
        }
        
        System.out.println("Login attempt - Username: '" + username + "', Password: '" + password + "'");
        
        // Check admin credentials
        if (username.equals("saniya") && password.equals("saniya123")) {
            System.out.println("Admin login successful");
            openDashboard("admin", username);
            return;
        }
        
        // Check tourist credentials
        User tourist = FileManager.authenticateUser(username, password, "tourists.txt");
        if (tourist != null) {
            System.out.println("Tourist login successful");
            openDashboard("tourist", username);
            return;
        }
        
        // Check guide credentials
        User guide = FileManager.authenticateUser(username, password, "guides.txt");
        if (guide != null) {
            System.out.println("Guide login successful");
            openDashboard("guide", username);
            return;
        }
        
        System.out.println("Login failed for username: " + username);
        showError("Invalid username or password!");
    }
    
    @FXML
    private void handleRegister() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/register.fxml"));
            Stage stage = (Stage) registerButton.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error loading registration page: " + e.getMessage());
        }
    }
    
    @FXML
    private void toggleLanguage() {
        languageManager.toggleLanguage();
        updateLanguage();
    }
    
    private void updateLanguage() {
        titleLabel.setText(languageManager.getText("login.title"));
        usernameLabel.setText(languageManager.getText("login.username"));
        passwordLabel.setText(languageManager.getText("login.password"));
        loginButton.setText(languageManager.getText("login.button"));
        registerButton.setText(languageManager.getText("register.button"));
        languageToggle.setText(languageManager.getCurrentLanguage().equals("EN") ? "नेपाली" : "English");
    }
    
    private void openDashboard(String role, String username) {
        try {
            String fxmlFile = "/fxml/" + role + "-dashboard.fxml";
            System.out.println("Attempting to load FXML file: " + fxmlFile);
            
            if (getClass().getResource(fxmlFile) == null) {
                System.out.println("FXML file not found: " + fxmlFile);
                showError("Dashboard file not found: " + fxmlFile);
                return;
            }
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            
            // Pass username to controller
            if (role.equals("tourist")) {
                TouristDashboardController controller = loader.getController();
                controller.setUsername(username);
            } else if (role.equals("guide")) {
                GuideDashboardController controller = loader.getController();
                controller.setUsername(username);
            } else if (role.equals("admin")) {
                AdminDashboardController controller = loader.getController();
                controller.setUsername(username);
            }
            
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root, 1200, 800));
            stage.setTitle("Nepal Tourism Management System - " + role.substring(0, 1).toUpperCase() + role.substring(1) + " Dashboard");
            
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading dashboard: " + e.getMessage());
            showError("Error loading dashboard: " + e.getMessage());
        }
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setStyle("-fx-text-fill: red;");
    }
}
