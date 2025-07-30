package com.tourism.controllers;

import com.tourism.models.Booking;
import com.tourism.models.User;
import com.tourism.utils.FileManager;
import com.tourism.utils.LanguageManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
            welcomeLabel.setText(languageManager.getText("welcome") + ", " + guide.getFullName() + "!");
            languagesLabel.setText(languageManager.getText("languages") + ": " + guide.getLanguages());
            experienceLabel.setText(languageManager.getText("experience") + ": " + guide.getExperience());
            
            // Calculate earnings (10-15% commission)
            double totalEarnings = calculateEarnings();
            earningsLabel.setText(languageManager.getText("earnings") + ": $" + String.format("%.2f", totalEarnings));
        }
    }
    
    private void loadUpcomingTreks() {
        List<Booking> assignedTreks = FileManager.loadGuideBookings(username);
        treksList.clear();
        treksList.addAll(assignedTreks);
    }
    
    private void loadImportantUpdates() {
        StringBuilder updates = new StringBuilder();
        updates.append(languageManager.getText("weather.alert")).append("\n");
        updates.append("• ").append(languageManager.getText("heavy.snow.everest")).append("\n");
        updates.append("• ").append(languageManager.getText("clear.weather.annapurna")).append("\n\n");
        
        updates.append(languageManager.getText("emergency.notices")).append("\n");
        updates.append("• ").append(languageManager.getText("rescue.helicopter.available")).append("\n");
        updates.append("• ").append(languageManager.getText("medical.checkup.required")).append("\n");
        
        updatesArea.setText(updates.toString());
    }
    
    private double calculateEarnings() {
        List<Booking> guideBookings = FileManager.loadGuideBookings(username);
        double totalEarnings = 0.0;
        
        for (Booking booking : guideBookings) {
            // 12% commission on each booking
            totalEarnings += booking.getPrice() * 0.12;
        }
        
        return totalEarnings;
    }
    
    @FXML
    private void handleLogout() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = (javafx.stage.Stage) logoutButton.getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(root, 800, 600));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void toggleLanguage() {
        languageManager.toggleLanguage();
        updateLanguage();
        loadGuideData(); // Reload to update language-dependent content
        loadImportantUpdates();
    }
    
    private void updateLanguage() {
        logoutButton.setText(languageManager.getText("logout.button"));
        languageToggle.setText(languageManager.getCurrentLanguage().equals("EN") ? "नेपाली" : "English");
    }
}
