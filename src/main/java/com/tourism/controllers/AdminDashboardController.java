package com.tourism.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import com.tourism.models.User;
import com.tourism.models.Booking;
import com.tourism.models.Attraction;
import com.tourism.utils.FileManager;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class AdminDashboardController {
    
    @FXML private Label welcomeLabel;
    @FXML private Button languageToggle;
    @FXML private Button logoutButton;
    @FXML private TabPane mainTabPane;
    
    // Guide Management
    @FXML private TextField guideUsernameField;
    @FXML private TextField guideNameField;
    @FXML private PasswordField guidePasswordField;
    @FXML private TextField guideEmailField;
    @FXML private TextField guidePhoneField;
    @FXML private TextField guideLanguagesField;
    @FXML private TextField guideExperienceField;
    @FXML private Button addGuideButton;
    @FXML private Button updateGuideButton;
    @FXML private Button deleteGuideButton;
    @FXML private TableView<User> guidesTable;
    @FXML private TableColumn<User, String> guideUsernameColumn;
    @FXML private TableColumn<User, String> guideNameColumn;
    @FXML private TableColumn<User, String> guideLanguagesColumn;
    @FXML private TableColumn<User, String> guideExperienceColumn;
    
    // Attraction Management
    @FXML private TextField attractionNameField;
    @FXML private ComboBox<String> attractionAltitudeCombo;
    @FXML private ComboBox<String> attractionDifficultyCombo;
    @FXML private TextField attractionPriceField;
    @FXML private Button addAttractionButton;
    @FXML private Button updateAttractionButton;
    @FXML private Button deleteAttractionButton;
    @FXML private TableView<Attraction> attractionsTable;
    @FXML private TableColumn<Attraction, String> attractionNameColumn;
    @FXML private TableColumn<Attraction, String> attractionAltitudeColumn;
    @FXML private TableColumn<Attraction, String> attractionDifficultyColumn;
    @FXML private TableColumn<Attraction, Double> attractionPriceColumn;
    
    // Booking Management
    @FXML private Button cancelBookingButton;
    @FXML private TableView<Booking> bookingsTable;
    @FXML private TableColumn<Booking, String> bookingIdColumn;
    @FXML private TableColumn<Booking, String> bookingTouristColumn;
    @FXML private TableColumn<Booking, String> bookingAttractionColumn;
    @FXML private TableColumn<Booking, String> bookingDateColumn;
    @FXML private TableColumn<Booking, String> bookingStatusColumn;
    
    // Analytics
    @FXML private Label totalRevenueLabel;
    @FXML private PieChart nationalityChart;
    @FXML private BarChart<String, Number> popularAttractionsChart;
    
    private User currentUser;
    private boolean isNepali = false;
    private ObservableList<User> guidesList = FXCollections.observableArrayList();
    private ObservableList<Attraction> attractionsList = FXCollections.observableArrayList();
    private ObservableList<Booking> bookingsList = FXCollections.observableArrayList();
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
        initialize();
    }
    
    @FXML
    private void initialize() {
        if (currentUser != null) {
            welcomeLabel.setText("Welcome, " + currentUser.getFullName() + "!");
            
            initializeTableColumns();
            initializeComboBoxes();
            loadAllData();
        }
    }
    
    private void initializeTableColumns() {
        // Guide table columns
        guideUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        guideNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        guideLanguagesColumn.setCellValueFactory(new PropertyValueFactory<>("languages"));
        guideExperienceColumn.setCellValueFactory(new PropertyValueFactory<>("experience"));
        
        // Attraction table columns
        attractionNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        attractionAltitudeColumn.setCellValueFactory(new PropertyValueFactory<>("altitude"));
        attractionDifficultyColumn.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        attractionPriceColumn.setCellValueFactory(new PropertyValueFactory<>("basePrice"));
        
        // Booking table columns
        bookingIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        bookingTouristColumn.setCellValueFactory(new PropertyValueFactory<>("touristName"));
        bookingAttractionColumn.setCellValueFactory(new PropertyValueFactory<>("attractionName"));
        bookingDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        bookingStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }
    
    private void initializeComboBoxes() {
        attractionAltitudeCombo.setItems(FXCollections.observableArrayList(
            "Low (< 3000m)", "Medium (3000-5000m)", "High (5000-7000m)", "Extreme (> 7000m)"
        ));
        attractionDifficultyCombo.setItems(FXCollections.observableArrayList(
            "Easy", "Medium", "Hard", "Extreme"
        ));
    }
    
    private void loadAllData() {
        loadGuides();
        loadAttractions();
        loadBookings();
        updateAnalytics();
    }
    
    private void loadGuides() {
        try {
            List<User> users = FileManager.loadUsers();
            guidesList.clear();
            for (User user : users) {
                if ("guide".equals(user.getRole().toLowerCase())) {
                    guidesList.add(user);
                }
            }
            guidesTable.setItems(guidesList);
        } catch (Exception e) {
            showAlert("Error loading guides: " + e.getMessage());
        }
    }
    
    private void loadAttractions() {
        try {
            List<Attraction> attractions = FileManager.loadAttractions();
            attractionsList.clear();
            attractionsList.addAll(attractions);
            attractionsTable.setItems(attractionsList);
        } catch (Exception e) {
            showAlert("Error loading attractions: " + e.getMessage());
        }
    }
    
    private void loadBookings() {
        try {
            List<Booking> bookings = FileManager.loadBookings();
            bookingsList.clear();
            bookingsList.addAll(bookings);
            bookingsTable.setItems(bookingsList);
        } catch (Exception e) {
            showAlert("Error loading bookings: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleAddGuide() {
        if (!validateGuideForm()) {
            return;
        }
        
        try {
            String username = guideUsernameField.getText().trim();
            if (FileManager.userExists(username)) {
                showAlert("Username already exists");
                return;
            }
            
            User newGuide = new User(
                username,
                guidePasswordField.getText().trim(),
                guideNameField.getText().trim(),
                guideEmailField.getText().trim(),
                guidePhoneField.getText().trim(),
                "guide",
                guideLanguagesField.getText().trim(),
                guideExperienceField.getText().trim()
            );
            
            if (FileManager.saveUser(newGuide)) {
                showAlert("Guide added successfully!");
                loadGuides();
                clearGuideForm();
            } else {
                showAlert("Failed to add guide");
            }
        } catch (Exception e) {
            showAlert("Error adding guide: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleUpdateGuide() {
        User selectedGuide = guidesTable.getSelectionModel().getSelectedItem();
        if (selectedGuide == null) {
            showAlert("Please select a guide to update");
            return;
        }
        
        if (!validateGuideForm()) {
            return;
        }
        
        try {
            selectedGuide.setFullName(guideNameField.getText().trim());
            selectedGuide.setEmail(guideEmailField.getText().trim());
            selectedGuide.setPhone(guidePhoneField.getText().trim());
            selectedGuide.setLanguages(guideLanguagesField.getText().trim());
            selectedGuide.setExperience(guideExperienceField.getText().trim());
            
            if (FileManager.updateUser(selectedGuide)) {
                showAlert("Guide updated successfully!");
                loadGuides();
                clearGuideForm();
            } else {
                showAlert("Failed to update guide");
            }
        } catch (Exception e) {
            showAlert("Error updating guide: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleDeleteGuide() {
        User selectedGuide = guidesTable.getSelectionModel().getSelectedItem();
        if (selectedGuide == null) {
            showAlert("Please select a guide to delete");
            return;
        }
        
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Delete Guide");
        confirmAlert.setContentText("Are you sure you want to delete this guide?");
        
        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            try {
                if (FileManager.deleteUser(selectedGuide.getUsername())) {
                    showAlert("Guide deleted successfully!");
                    loadGuides();
                    clearGuideForm();
                } else {
                    showAlert("Failed to delete guide");
                }
            } catch (Exception e) {
                showAlert("Error deleting guide: " + e.getMessage());
            }
        }
    }
    
    @FXML
    private void handleAddAttraction() {
        if (!validateAttractionForm()) {
            return;
        }
        
        try {
            Attraction newAttraction = new Attraction(
                attractionNameField.getText().trim(),
                attractionAltitudeCombo.getValue(),
                attractionDifficultyCombo.getValue(),
                Double.parseDouble(attractionPriceField.getText().trim())
            );
            
            if (FileManager.saveAttraction(newAttraction)) {
                showAlert("Attraction added successfully!");
                loadAttractions();
                clearAttractionForm();
            } else {
                showAlert("Failed to add attraction");
            }
        } catch (Exception e) {
            showAlert("Error adding attraction: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleUpdateAttraction() {
        Attraction selectedAttraction = attractionsTable.getSelectionModel().getSelectedItem();
        if (selectedAttraction == null) {
            showAlert("Please select an attraction to update");
            return;
        }
        
        if (!validateAttractionForm()) {
            return;
        }
        
        try {
            selectedAttraction.setName(attractionNameField.getText().trim());
            selectedAttraction.setAltitude(attractionAltitudeCombo.getValue());
            selectedAttraction.setDifficulty(attractionDifficultyCombo.getValue());
            selectedAttraction.setBasePrice(Double.parseDouble(attractionPriceField.getText().trim()));
            
            if (FileManager.updateAttraction(selectedAttraction)) {
                showAlert("Attraction updated successfully!");
                loadAttractions();
                clearAttractionForm();
            } else {
                showAlert("Failed to update attraction");
            }
        } catch (Exception e) {
            showAlert("Error updating attraction: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleDeleteAttraction() {
        Attraction selectedAttraction = attractionsTable.getSelectionModel().getSelectedItem();
        if (selectedAttraction == null) {
            showAlert("Please select an attraction to delete");
            return;
        }
        
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Delete Attraction");
        confirmAlert.setContentText("Are you sure you want to delete this attraction?");
        
        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            try {
                if (FileManager.deleteAttraction(selectedAttraction.getName())) {
                    showAlert("Attraction deleted successfully!");
                    loadAttractions();
                    clearAttractionForm();
                } else {
                    showAlert("Failed to delete attraction");
                }
            } catch (Exception e) {
                showAlert("Error deleting attraction: " + e.getMessage());
            }
        }
    }
    
    @FXML
    private void handleCancelBooking() {
        Booking selectedBooking = bookingsTable.getSelectionModel().getSelectedItem();
        if (selectedBooking == null) {
            showAlert("Please select a booking to cancel");
            return;
        }
        
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Cancellation");
        confirmAlert.setHeaderText("Cancel Booking");
        confirmAlert.setContentText("Are you sure you want to cancel this booking?");
        
        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            try {
                selectedBooking.setStatus("Cancelled");
                if (FileManager.updateBooking(selectedBooking)) {
                    showAlert("Booking cancelled successfully!");
                    loadBookings();
                } else {
                    showAlert("Failed to cancel booking");
                }
            } catch (Exception e) {
                showAlert("Error cancelling booking: " + e.getMessage());
            }
        }
    }
    
    private void updateAnalytics() {
        try {
            // Calculate total revenue
            double totalRevenue = 0.0;
            for (Booking booking : bookingsList) {
                if (!"Cancelled".equals(booking.getStatus())) {
                    totalRevenue += booking.getPrice();
                }
            }
            totalRevenueLabel.setText("Total Revenue: $" + String.format("%.2f", totalRevenue));
            
            // Update nationality chart (mock data)
            ObservableList<PieChart.Data> nationalityData = FXCollections.observableArrayList(
                new PieChart.Data("USA", 30),
                new PieChart.Data("UK", 25),
                new PieChart.Data("Germany", 20),
                new PieChart.Data("Australia", 15),
                new PieChart.Data("Others", 10)
            );
            nationalityChart.setData(nationalityData);
            
            // Update popular attractions chart
            Map<String, Integer> attractionCounts = new HashMap<>();
            for (Booking booking : bookingsList) {
                if (!"Cancelled".equals(booking.getStatus())) {
                    attractionCounts.put(booking.getAttractionName(), 
                        attractionCounts.getOrDefault(booking.getAttractionName(), 0) + 1);
                }
            }
            
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Bookings");
            for (Map.Entry<String, Integer> entry : attractionCounts.entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }
            popularAttractionsChart.getData().clear();
            popularAttractionsChart.getData().add(series);
            
        } catch (Exception e) {
            showAlert("Error updating analytics: " + e.getMessage());
        }
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
    
    private boolean validateGuideForm() {
        if (guideUsernameField.getText().trim().isEmpty() ||
            guideNameField.getText().trim().isEmpty() ||
            guidePasswordField.getText().trim().isEmpty() ||
            guideEmailField.getText().trim().isEmpty() ||
            guidePhoneField.getText().trim().isEmpty() ||
            guideLanguagesField.getText().trim().isEmpty() ||
            guideExperienceField.getText().trim().isEmpty()) {
            
            showAlert("Please fill all guide fields");
            return false;
        }
        return true;
    }
    
    private boolean validateAttractionForm() {
        if (attractionNameField.getText().trim().isEmpty() ||
            attractionAltitudeCombo.getValue() == null ||
            attractionDifficultyCombo.getValue() == null ||
            attractionPriceField.getText().trim().isEmpty()) {
            
            showAlert("Please fill all attraction fields");
            return false;
        }
        
        try {
            Double.parseDouble(attractionPriceField.getText().trim());
        } catch (NumberFormatException e) {
            showAlert("Please enter a valid price");
            return false;
        }
        
        return true;
    }
    
    private void clearGuideForm() {
        guideUsernameField.clear();
        guideNameField.clear();
        guidePasswordField.clear();
        guideEmailField.clear();
        guidePhoneField.clear();
        guideLanguagesField.clear();
        guideExperienceField.clear();
    }
    
    private void clearAttractionForm() {
        attractionNameField.clear();
        attractionAltitudeCombo.setValue(null);
        attractionDifficultyCombo.setValue(null);
        attractionPriceField.clear();
    }
    
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
