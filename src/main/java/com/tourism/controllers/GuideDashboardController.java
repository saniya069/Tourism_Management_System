package com.tourism.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import com.tourism.models.User;
import com.tourism.models.Booking;
import com.tourism.utils.FileManager;
import java.util.List;

public class GuideDashboardController {
    
    @FXML private Label welcomeLabel;
    @FXML private Button languageToggle;
    @FXML private Button logoutButton;
    @FXML private Label earningsLabel;
    @FXML private Label languagesLabel;
    @FXML private Label experienceLabel;
    @FXML private TableView<Booking> treksTable;
    @FXML private TableColumn<Booking, String> touristColumn;
    @FXML private TableColumn<Booking, String> trekColumn;
    @FXML private TableColumn<Booking, String> dateColumn;
    @FXML private TableColumn<Booking, String> difficultyColumn;
    @FXML private TextArea updatesArea;
    
    private User currentUser;
    private boolean isNepali = false;
    private ObservableList<Booking> treksList = FXCollections.observableArrayList();
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
        initialize();
    }
    
    @FXML
    private void initialize() {
        if (currentUser != null) {
            welcomeLabel.setText("Welcome, " + currentUser.getFullName() + "!");
            
            // Initialize table columns
            touristColumn.setCellValueFactory(new PropertyValueFactory<>("touristName"));
            trekColumn.setCellValueFactory(new PropertyValueFactory<>("attractionName"));
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
            difficultyColumn.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
            
            // Load profile information
            loadProfileInfo();
            loadAssignedTreks();
            loadUpdates();
        }
    }
    
    private void loadProfileInfo() {
        // Calculate earnings from completed treks
        double totalEarnings = calculateEarnings();
        earningsLabel.setText("Earnings: $" + String.format("%.2f", totalEarnings));
        
        // Display languages and experience
        languagesLabel.setText("Languages: " + (currentUser.getLanguages() != null ? currentUser.getLanguages() : "Not specified"));
        experienceLabel.setText("Experience: " + (currentUser.getExperience() != null ? currentUser.getExperience() : "Not specified"));
    }
    
    private double calculateEarnings() {
        double earnings = 0.0;
        try {
            List<Booking> bookings = FileManager.loadBookings();
            for (Booking booking : bookings) {
                if ("Completed".equals(booking.getStatus())) {
                    // Guides get 20% commission
                    earnings += booking.getPrice() * 0.20;
                }
            }
        } catch (Exception e) {
            System.err.println("Error calculating earnings: " + e.getMessage());
        }
        return earnings;
    }
    
    private void loadAssignedTreks() {
        try {
            List<Booking> bookings = FileManager.loadBookings();
            treksList.clear();
            for (Booking booking : bookings) {
                if ("Confirmed".equals(booking.getStatus()) || "In Progress".equals(booking.getStatus())) {
                    treksList.add(booking);
                }
            }
            treksTable.setItems(treksList);
        } catch (Exception e) {
            showAlert("Error loading assigned treks: " + e.getMessage());
        }
    }
    
    private void loadUpdates() {
        StringBuilder updates = new StringBuilder();
        updates.append("Important Updates for Guides:\n\n");
        updates.append("• Weather Alert: Check weather conditions before each trek\n");
        updates.append("• Safety Protocol: Always carry first aid kit and emergency contacts\n");
        updates.append("• New Route: Annapurna Circuit has new rest stops available\n");
        updates.append("• Festival Season: Expect higher tourist volume during Dashain\n");
        updates.append("• Training: Mandatory safety training scheduled for next month\n");
        updates.append("• Equipment: New GPS devices available at the office\n");
        updates.append("• Emergency: Contact +977-1-4444444 for any emergencies\n");
        
        updatesArea.setText(updates.toString());
    }
    
    @FXML
    private void toggleLanguage() {
        isNepali = !isNepali;
        updateLanguage();
    }
    
    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            showAlert("Error logging out: " + e.getMessage());
        }
    }
    
    private void updateLanguage() {
        if (isNepali) {
            welcomeLabel.setText("स्वागतम्, " + currentUser.getFullName() + "!");
            languageToggle.setText("English");
            logoutButton.setText("लगआउट");
        } else {
            welcomeLabel.setText("Welcome, " + currentUser.getFullName() + "!");
            languageToggle.setText("नेपाली");
            logoutButton.setText("Logout");
        }
    }
    
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
