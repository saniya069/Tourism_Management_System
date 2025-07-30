package com.tourism.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.tourism.models.User;
import com.tourism.utils.FileManager;

public class LoginController {
    
    @FXML private Label titleLabel;
    @FXML private Label usernameLabel;
    @FXML private Label passwordLabel;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button registerButton;
    @FXML private Button languageToggle;
    @FXML private Label errorLabel;
    
    private boolean isNepali = false;
    
    @FXML
    private void initialize() {
        updateLanguage();
    }
    
    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        
        if (username.isEmpty() || password.isEmpty()) {
            showError(isNepali ? "कृपया सबै फिल्डहरू भर्नुहोस्" : "Please fill all fields");
            return;
        }
        
        try {
            User user = FileManager.authenticateUser(username, password);
            if (user != null) {
                openDashboard(user);
            } else {
                showError(isNepali ? "गलत प्रयोगकर्ता नाम वा पासवर्ड" : "Invalid username or password");
            }
        } catch (Exception e) {
            showError(isNepali ? "लगइन त्रुटि: " + e.getMessage() : "Login error: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/register.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) registerButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            showError("Error loading registration: " + e.getMessage());
        }
    }
    
    @FXML
    private void toggleLanguage() {
        isNepali = !isNepali;
        updateLanguage();
    }
    
    private void updateLanguage() {
        if (isNepali) {
            titleLabel.setText("नेपाल पर्यटन व्यवस्थापन प्रणाली");
            usernameLabel.setText("प्रयोगकर्ता नाम:");
            passwordLabel.setText("पासवर्ड:");
            loginButton.setText("लगइन");
            registerButton.setText("दर्ता");
            languageToggle.setText("English");
            usernameField.setPromptText("प्रयोगकर्ता नाम प्रविष्ट गर्नुहोस्");
            passwordField.setPromptText("पासवर्ड प्रविष्ट गर्नुहोस्");
        } else {
            titleLabel.setText("Nepal Tourism Management System");
            usernameLabel.setText("Username:");
            passwordLabel.setText("Password:");
            loginButton.setText("Login");
            registerButton.setText("Register");
            languageToggle.setText("नेपाली");
            usernameField.setPromptText("Enter username");
            passwordField.setPromptText("Enter password");
        }
    }
    
    private void openDashboard(User user) {
        try {
            String fxmlFile = "";
            switch (user.getRole().toLowerCase()) {
                case "admin":
                    fxmlFile = "/fxml/admin-dashboard.fxml";
                    break;
                case "tourist":
                    fxmlFile = "/fxml/tourist-dashboard.fxml";
                    break;
                case "guide":
                    fxmlFile = "/fxml/guide-dashboard.fxml";
                    break;
                default:
                    showError("Unknown user role: " + user.getRole());
                    return;
            }
            
            System.out.println("Loading FXML: " + fxmlFile);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            
            // Pass user data to the dashboard controller
            Object controller = loader.getController();
            if (controller instanceof TouristDashboardController) {
                ((TouristDashboardController) controller).setCurrentUser(user);
            } else if (controller instanceof GuideDashboardController) {
                ((GuideDashboardController) controller).setCurrentUser(user);
            } else if (controller instanceof AdminDashboardController) {
                ((AdminDashboardController) controller).setCurrentUser(user);
            }
            
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error opening dashboard: " + e.getMessage());
        }
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}
