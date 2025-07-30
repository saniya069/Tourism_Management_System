package com.tourism.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import com.tourism.models.User;
import com.tourism.models.Booking;
import com.tourism.models.Attraction;
import com.tourism.utils.FileManager;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class TouristDashboardController implements Initializable {
    
    // Header elements
    @FXML private Label welcomeLabel;
    @FXML private Button languageToggle;
    @FXML private Button logoutButton;
    
    // Form elements
    @FXML private ComboBox<String> attractionComboBox;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> difficultyComboBox;
    @FXML private Label priceLabel;
    @FXML private Button bookButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    
    // Table elements
    @FXML private TableView<Booking> bookingsTable;
    @FXML private TableColumn<Booking, String> bookingIdColumn;
    @FXML private TableColumn<Booking, String> attractionColumn;
    @FXML private TableColumn<Booking, String> dateColumn;
    @FXML private TableColumn<Booking, String> difficultyColumn;
    @FXML private TableColumn<Booking, Double> priceColumn;
    @FXML private TableColumn<Booking, String> statusColumn;
    
    private User currentUser;
    private boolean isNepali = false;
    private ObservableList<Booking> bookingsList = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("TouristDashboardController initialize() called");
        
        try {
            // Check if all FXML elements are properly injected
            checkFXMLInjection();
            
            // Initialize table columns with null checks
            initializeTableColumns();
            
            // Set up event handlers with null checks
            setupEventHandlers();
            
            System.out.println("Controller initialized successfully");
        } catch (Exception e) {
            System.err.println("Error in initialize(): " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void checkFXMLInjection() {
        System.out.println("Checking FXML injection...");
        System.out.println("welcomeLabel: " + (welcomeLabel != null ? "OK" : "NULL"));
        System.out.println("attractionComboBox: " + (attractionComboBox != null ? "OK" : "NULL"));
        System.out.println("datePicker: " + (datePicker != null ? "OK" : "NULL"));
        System.out.println("difficultyComboBox: " + (difficultyComboBox != null ? "OK" : "NULL"));
        System.out.println("priceLabel: " + (priceLabel != null ? "OK" : "NULL"));
        System.out.println("bookingsTable: " + (bookingsTable != null ? "OK" : "NULL"));
        System.out.println("bookingIdColumn: " + (bookingIdColumn != null ? "OK" : "NULL"));
        System.out.println("attractionColumn: " + (attractionColumn != null ? "OK" : "NULL"));
        System.out.println("dateColumn: " + (dateColumn != null ? "OK" : "NULL"));
        System.out.println("difficultyColumn: " + (difficultyColumn != null ? "OK" : "NULL"));
        System.out.println("priceColumn: " + (priceColumn != null ? "OK" : "NULL"));
        System.out.println("statusColumn: " + (statusColumn != null ? "OK" : "NULL"));
    }
    
    private void initializeTableColumns() {
        if (bookingIdColumn != null) {
            bookingIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        }
        if (attractionColumn != null) {
            attractionColumn.setCellValueFactory(new PropertyValueFactory<>("attractionName"));
        }
        if (dateColumn != null) {
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        }
        if (difficultyColumn != null) {
            difficultyColumn.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        }
        if (priceColumn != null) {
            priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        }
        if (statusColumn != null) {
            statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        }
        
        System.out.println("Table columns initialized");
    }
    
    private void setupEventHandlers() {
        if (attractionComboBox != null) {
            attractionComboBox.setOnAction(e -> updatePrice());
        }
        if (difficultyComboBox != null) {
            difficultyComboBox.setOnAction(e -> updatePrice());
        }
        
        System.out.println("Event handlers set up");
    }
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
        System.out.println("Setting current user: " + user.getUsername());
        
        // Update welcome label
        if (welcomeLabel != null && currentUser != null) {
            welcomeLabel.setText("Welcome, " + currentUser.getFullName() + "!");
        }
        
        // Load data
        loadAttractions();
        loadDifficulties();
        loadBookings();
    }
    
    private void loadAttractions() {
        if (attractionComboBox == null) {
            System.err.println("attractionComboBox is null, cannot load attractions");
            return;
        }
        
        try {
            List<Attraction> attractions = FileManager.loadAttractions();
            ObservableList<String> attractionNames = FXCollections.observableArrayList();
            for (Attraction attraction : attractions) {
                attractionNames.add(attraction.getName());
            }
            attractionComboBox.setItems(attractionNames);
            System.out.println("Loaded " + attractions.size() + " attractions");
        } catch (Exception e) {
            System.err.println("Error loading attractions: " + e.getMessage());
            showAlert("Error loading attractions: " + e.getMessage());
        }
    }
    
    private void loadDifficulties() {
        if (difficultyComboBox == null) {
            System.err.println("difficultyComboBox is null, cannot load difficulties");
            return;
        }
        
        try {
            difficultyComboBox.setItems(FXCollections.observableArrayList("Easy", "Medium", "Hard", "Extreme"));
            System.out.println("Difficulty levels loaded");
        } catch (Exception e) {
            System.err.println("Error loading difficulties: " + e.getMessage());
        }
    }
    
    private void loadBookings() {
        if (bookingsTable == null || currentUser == null) {
            System.err.println("bookingsTable is null or currentUser is null, cannot load bookings");
            return;
        }
        
        try {
            List<Booking> bookings = FileManager.loadBookings();
            bookingsList.clear();
            for (Booking booking : bookings) {
                if (booking.getTouristName().equals(currentUser.getUsername())) {
                    bookingsList.add(booking);
                }
            }
            bookingsTable.setItems(bookingsList);
            System.out.println("Loaded " + bookingsList.size() + " bookings for user: " + currentUser.getUsername());
        } catch (Exception e) {
            System.err.println("Error loading bookings: " + e.getMessage());
            showAlert("Error loading bookings: " + e.getMessage());
        }
    }
    
    private void updatePrice() {
        if (attractionComboBox == null || difficultyComboBox == null || priceLabel == null) {
            return;
        }
        
        String attractionName = attractionComboBox.getValue();
        String difficulty = difficultyComboBox.getValue();
        
        if (attractionName != null && difficulty != null) {
            try {
                List<Attraction> attractions = FileManager.loadAttractions();
                for (Attraction attraction : attractions) {
                    if (attraction.getName().equals(attractionName)) {
                        double basePrice = attraction.getBasePrice();
                        double multiplier = getDifficultyMultiplier(difficulty);
                        double finalPrice = basePrice * multiplier;
                        priceLabel.setText("$" + String.format("%.2f", finalPrice));
                        return;
                    }
                }
            } catch (Exception e) {
                System.err.println("Error calculating price: " + e.getMessage());
            }
        }
        priceLabel.setText("$0.00");
    }
    
    private double getDifficultyMultiplier(String difficulty) {
        switch (difficulty.toLowerCase()) {
            case "easy": return 1.0;
            case "medium": return 1.5;
            case "hard": return 2.0;
            case "extreme": return 3.0;
            default: return 1.0;
        }
    }
    
    @FXML
    private void handleBooking() {
        System.out.println("handleBooking() called");
        if (!validateBookingForm()) {
            return;
        }
        
        try {
            String bookingId = "BK" + System.currentTimeMillis();
            String attractionName = attractionComboBox.getValue();
            String date = datePicker.getValue().toString();
            String difficulty = difficultyComboBox.getValue();
            double price = Double.parseDouble(priceLabel.getText().replace("$", ""));
            
            Booking newBooking = new Booking(bookingId, currentUser.getUsername(), 
                                           attractionName, date, difficulty, price, "Confirmed");
            
            if (FileManager.saveBooking(newBooking)) {
                showAlert("Booking successful! Booking ID: " + bookingId);
                loadBookings();
                clearForm();
            } else {
                showAlert("Booking failed. Please try again.");
            }
        } catch (Exception e) {
            System.err.println("Error creating booking: " + e.getMessage());
            showAlert("Error creating booking: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleUpdateBooking() {
        System.out.println("handleUpdateBooking() called");
        if (bookingsTable == null) {
            showAlert("Table not available");
            return;
        }
        
        Booking selectedBooking = bookingsTable.getSelectionModel().getSelectedItem();
        if (selectedBooking == null) {
            showAlert("Please select a booking to update");
            return;
        }
        
        if (!validateBookingForm()) {
            return;
        }
        
        try {
            selectedBooking.setAttractionName(attractionComboBox.getValue());
            selectedBooking.setDate(datePicker.getValue().toString());
            selectedBooking.setDifficulty(difficultyComboBox.getValue());
            selectedBooking.setPrice(Double.parseDouble(priceLabel.getText().replace("$", "")));
            
            if (FileManager.updateBooking(selectedBooking)) {
                showAlert("Booking updated successfully!");
                loadBookings();
                clearForm();
            } else {
                showAlert("Update failed. Please try again.");
            }
        } catch (Exception e) {
            System.err.println("Error updating booking: " + e.getMessage());
            showAlert("Error updating booking: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleDeleteBooking() {
        System.out.println("handleDeleteBooking() called");
        if (bookingsTable == null) {
            showAlert("Table not available");
            return;
        }
        
        Booking selectedBooking = bookingsTable.getSelectionModel().getSelectedItem();
        if (selectedBooking == null) {
            showAlert("Please select a booking to delete");
            return;
        }
        
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Delete Booking");
        confirmAlert.setContentText("Are you sure you want to delete this booking?");
        
        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            try {
                if (FileManager.deleteBooking(selectedBooking.getBookingId())) {
                    showAlert("Booking deleted successfully!");
                    loadBookings();
                    clearForm();
                } else {
                    showAlert("Delete failed. Please try again.");
                }
            } catch (Exception e) {
                System.err.println("Error deleting booking: " + e.getMessage());
                showAlert("Error deleting booking: " + e.getMessage());
            }
        }
    }
    
    @FXML
    private void toggleLanguage() {
        System.out.println("toggleLanguage() called");
        isNepali = !isNepali;
        updateLanguage();
    }
    
    @FXML
    private void handleLogout() {
        System.out.println("handleLogout() called");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            System.err.println("Error logging out: " + e.getMessage());
            showAlert("Error logging out: " + e.getMessage());
        }
    }
    
    private void updateLanguage() {
        if (currentUser != null && welcomeLabel != null) {
            if (isNepali) {
                welcomeLabel.setText("स्वागतम्, " + currentUser.getFullName() + "!");
                if (languageToggle != null) languageToggle.setText("English");
                if (logoutButton != null) logoutButton.setText("लगआउट");
                if (bookButton != null) bookButton.setText("बुक गर्नुहोस्");
                if (updateButton != null) updateButton.setText("अपडेट");
                if (deleteButton != null) deleteButton.setText("मेटाउनुहोस्");
            } else {
                welcomeLabel.setText("Welcome, " + currentUser.getFullName() + "!");
                if (languageToggle != null) languageToggle.setText("नेपाली");
                if (logoutButton != null) logoutButton.setText("Logout");
                if (bookButton != null) bookButton.setText("Book Now");
                if (updateButton != null) updateButton.setText("Update");
                if (deleteButton != null) deleteButton.setText("Delete");
            }
        }
    }
    
    private boolean validateBookingForm() {
        if (attractionComboBox == null || datePicker == null || difficultyComboBox == null) {
            showAlert("Form elements not available");
            return false;
        }
        
        if (attractionComboBox.getValue() == null) {
            showAlert("Please select an attraction");
            return false;
        }
        if (datePicker.getValue() == null) {
            showAlert("Please select a date");
            return false;
        }
        if (datePicker.getValue().isBefore(LocalDate.now())) {
            showAlert("Please select a future date");
            return false;
        }
        if (difficultyComboBox.getValue() == null) {
            showAlert("Please select difficulty level");
            return false;
        }
        return true;
    }
    
    private void clearForm() {
        if (attractionComboBox != null) attractionComboBox.setValue(null);
        if (datePicker != null) datePicker.setValue(null);
        if (difficultyComboBox != null) difficultyComboBox.setValue(null);
        if (priceLabel != null) priceLabel.setText("$0.00");
    }
    
    private void showAlert(String message) {
        try {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        } catch (Exception e) {
            System.err.println("Error showing alert: " + e.getMessage());
            System.out.println("Alert message was: " + message);
        }
    }
}
