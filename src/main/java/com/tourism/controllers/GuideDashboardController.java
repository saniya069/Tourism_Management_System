package com.tourism.controllers;

import com.tourism.models.Booking;
import com.tourism.models.User;
import com.tourism.utils.FileManager;
import com.tourism.utils.LanguageManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.util.List;

public class GuideDashboardController {
    
    @FXML private Label welcomeLabel;
    @FXML private Label earningsLabel;
    @FXML private Label languagesLabel;
    @FXML private Label experienceLabel;
    @FXML private TableView<Booking> treksTable;
    @FXML private TableColumn<Booking, String> touristColumn;
    @FXML private TableColumn<Booking, String> trekColumn;
    @FXML private TableColumn<Booking, String> dateColumn;
    @FXML private TableColumn<Booking, String> difficultyColumn;
    @FXML private TextArea updatesArea;
    @FXML private Button languageToggle;
    @FXML private Button logoutButton;
    
    private String username;
    private LanguageManager languageManager = LanguageManager.getInstance();
    private ObservableList<Booking> treksList = FXCollections.observableArrayList();
    
    public void setUsername(String username) {
        this.username = username;
        loadGuideData();
        loadUpcomingTreks();
        loadImportantUpdates();
    }
    
    @FXML
    private void initialize() {
        setupTable();
        updateLanguage();
    }
    
    private void setupTable() {
        touristColumn.setCellValueFactory(new PropertyValueFactory<>("touristName"));
        trekColumn.setCellValueFactory(new PropertyValueFactory<>("attractionName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        difficultyColumn.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        
        treksTable.setItems(treksList);
    }
    
    private void loadGuideData() {
        User guide = FileManager.getUserByUsername(username, "guides.txt");
        if (guide != null) {
            welcomeLabel.setText("Welcome, " + guide.getFullName() + "!");
            languagesLabel.setText("Languages: " + guide.getLanguages());
            experienceLabel.setText("Experience: " + guide.getExperience());
            
            double totalEarnings = calculateEarnings();
            earningsLabel.setText("Earnings: $" + String.format("%.2f", totalEarnings));
        }
    }
    
    private void loadUpcomingTreks() {
        List<Booking> assignedTreks = FileManager.loadGuideBookings(username);
        treksList.clear();
        treksList.addAll(assignedTreks);
    }
    
    private void loadImportantUpdates() {
        StringBuilder updates = new StringBuilder();
        updates.append("Weather Alerts:\n");
        updates.append("• Heavy snow expected on Everest trek routes\n");
        updates.append("• Clear weather conditions in Annapurna region\n\n");
        
        updates.append("Emergency Notices:\n");
        updates.append("• Rescue helicopter services available 24/7\n");
        updates.append("• Medical checkup required for high altitude treks\n");
        
        updatesArea.setText(updates.toString());
    }
    
    private double calculateEarnings() {
        List<Booking> guideBookings = FileManager.loadGuideBookings(username);
        double totalEarnings = 0.0;
        
        for (Booking booking : guideBookings) {
            totalEarnings += booking.getPrice() * 0.12; // 12% commission
        }
        
        return totalEarnings;
    }
    
    @FXML
    private void handleLogout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void toggleLanguage() {
        languageManager.toggleLanguage();
        updateLanguage();
        loadGuideData();
        loadImportantUpdates();
    }
    
    private void updateLanguage() {
        logoutButton.setText("Logout");
        languageToggle.setText(languageManager.getCurrentLanguage().equals("EN") ? "नेपाली" : "English");
    }
}
