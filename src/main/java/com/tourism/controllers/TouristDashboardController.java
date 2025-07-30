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
            
            // Initialize table columns programmatically
            initializeTableColumns();
            
            // Set up event handlers
            setupEventHandlers();
            
            System.out.println("Controller initialized successfully");
        } catch (Exception e) {
            System.err.println("Error in initialize(): " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void checkFXMLInjection() {
        System.out.println("=== FXML Injection Check ===");
        System.out.println("welcomeLabel: " + (welcomeLabel != null ? "✓ OK" : "✗ NULL"));
        System.out.println("languageToggle: " + (languageToggle != null ? "✓ OK" : "✗ NULL"));
        System.out.println("logoutButton: " + (logoutButton != null ? "✓ OK" : "✗ NULL"));
        System.out.println("attractionComboBox: " + (attractionComboBox != null ? "✓ OK" : "✗ NULL"));
        System.out.println("datePicker: " + (datePicker != null ? "✓ OK" : "✗ NULL"));
        System.out.println("difficultyComboBox: " + (difficultyComboBox != null ? "✓ OK" : "✗ NULL"));
        System.out.println("priceLabel: " + (priceLabel != null ? "✓ OK" : "✗ NULL"));
        System.out.println("bookButton: " + (bookButton != null ? "✓ OK" : "✗ NULL"));
        System.out.println("updateButton: " + (updateButton != null ? "✓ OK" : "✗ NULL"));
        System.out.println("deleteButton: " + (deleteButton != null ? "✓ OK" : "✗ NULL"));
        System.out.println("bookingsTable: " + (bookingsTable != null ? "✓ OK" : "✗ NULL"));
        System.out.println("bookingIdColumn: " + (bookingIdColumn != null ? "✓ OK" : "✗ NULL"));
        System.out.println("attractionColumn: " + (attractionColumn != null ? "✓ OK" : "✗ NULL"));
        System.out.println("dateColumn: " + (dateColumn != null ? "✓ OK" : "✗ NULL"));
        System.out.println("difficultyColumn: " + (difficultyColumn != null ? "✓ OK" : "✗ NULL"));
        System.out.println("priceColumn: " + (priceColumn != null ? "✓ OK" : "✗ NULL"));
        System.out.println("statusColumn: " + (statusColumn != null ? "✓ OK" : "✗ NULL"));
        System.out.println("=== End Injection Check ===");
    }
    
    private void initializeTableColumns() {
        System.out.println("Setting up table columns...");
        
        try {
            if (bookingIdColumn != null) {
                bookingIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
                System.out.println("✓ bookingIdColumn configured");
            } else {
                System.err.println("✗ bookingIdColumn is null");
            }
            
            if (attractionColumn != null) {
                attractionColumn.setCellValueFactory(new PropertyValueFactory<>("attractionName"));
                System.out.println("✓ attractionColumn configured");
            } else {
                System.err.println("✗ attractionColumn is null");
            }
            
            if (dateColumn != null) {
                dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
                System.out.println("✓ dateColumn configured");
            } else {
                System.err.println("✗ dateColumn is null");
            }
            
            if (difficultyColumn != null) {
                difficultyColumn.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
                System.out.println("✓ difficultyColumn configured");
            } else {
                System.err.println("✗ difficultyColumn is null");
            }
            
            if (priceColumn != null) {
                priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
                System.out.println("✓ priceColumn configured");
            } else {
                System.err.println("✗ priceColumn is null");
            }
            
            if (statusColumn != null) {
                statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
                System.out.println("✓ statusColumn configured");
            } else {
                System.err.println("✗ statusColumn is null");
            }
            
            System.out.println("Table columns setup completed");
        } catch (Exception e) {
            System.err.println("Error setting up table columns: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void setupEventHandlers() {
        System.out.println("Setting up event handlers...");
        
        try {
            if (attractionComboBox != null) {
                attractionComboBox.setOnAction(e -> updatePrice());
                System.out.println("✓ attractionComboBox event handler set");
            }
            
            if (difficultyComboBox != null) {
                difficultyComboBox.setOnAction(e -> updatePrice());
                System.out.println("✓ difficultyComboBox event handler set");
            }
            
            System.out.println("Event handlers setup completed");
        } catch (Exception e) {
            System.err.println("Error setting up event handlers: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void setCurrentUser(User user) {
        System.out.println("=== Setting Current User ===");
        this.currentUser = user;
        System.out.println("User: " + user.getUsername() + " (" + user.getFullName() + ")");
        
        // Update welcome label
        if (welcomeLabel != null && currentUser != null) {
            welcomeLabel.setText("Welcome, " + currentUser.getFullName() + "!");
            System.out.println("✓ Welcome label updated");
        } else {
            System.err.println("✗ Cannot update welcome label - welcomeLabel or currentUser is null");
        }
        
        // Load data
        loadAttractions();
        loadDifficulties();
        loadBookings();
        
        System.out.println("=== User Setup Complete ===");
    }
    
    private void loadAttractions() {
        System.out.println("Loading attractions...");
        
        if (attractionComboBox == null) {
            System.err.println("✗ attractionComboBox is null, cannot load attractions");
            return;
        }
        
        try {
            List<Attraction> attractions = FileManager.loadAttractions();
            ObservableList<String> attractionNames = FXCollections.observableArrayList();
            
            for (Attraction attraction : attractions) {
                attractionNames.add(attraction.getName());
                System.out.println("  - " + attraction.getName());
            }
            
            attractionComboBox.setItems(attractionNames);
            System.out.println("✓ Loaded " + attractions.size() + " attractions");
        } catch (Exception e) {
            System.err.println("✗ Error loading attractions: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error loading attractions: " + e.getMessage());
        }
    }
    
    private void loadDifficulties() {
        System.out.println("Loading difficulties...");
        
        if (difficultyComboBox == null) {
            System.err.println("✗ difficultyComboBox is null, cannot load difficulties");
            return;
        }
        
        try {
            ObservableList<String> difficulties = FXCollections.observableArrayList("Easy", "Medium", "Hard", "Extreme");
            difficultyComboBox.setItems(difficulties);
            System.out.println("✓ Difficulty levels loaded: " + difficulties);
        } catch (Exception e) {
            System.err.println("✗ Error loading difficulties: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void loadBookings() {
        System.out.println("Loading bookings...");
        
        if (bookingsTable == null) {
            System.err.println("✗ bookingsTable is null, cannot load bookings");
            return;
        }
        
        if (currentUser == null) {
            System.err.println("✗ currentUser is null, cannot load bookings");
            return;
        }
        
        try {
            List<Booking> allBookings = FileManager.loadBookings();
            bookingsList.clear();
            
            for (Booking booking : allBookings) {
                if (booking.getTouristName().equals(currentUser.getUsername())) {
                    bookingsList.add(booking);
                    System.out.println("  - Booking: " + booking.getBookingId() + " for " + booking.getAttractionName());
                }
            }
            
            bookingsTable.setItems(bookingsList);
            System.out.println("✓ Loaded " + bookingsList.size() + " bookings for user: " + currentUser.getUsername());
        } catch (Exception e) {
            System.err.println("✗ Error loading bookings: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error loading bookings: " + e.getMessage());
        }
    }
    
    private void updatePrice() {
        if (attractionComboBox == null || difficultyComboBox == null || priceLabel == null) {
            System.err.println("Cannot update price - form elements are null");
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
                        System.out.println("Price updated: " + attractionName + " (" + difficulty + ") = $" + String.format("%.2f", finalPrice));
                        return;
                    }
                }
            } catch (Exception e) {
                System.err.println("Error calculating price: " + e.getMessage());
                e.printStackTrace();
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
        System.out.println("=== Handle Booking ===");
        
        if (!validateBookingForm()) {
            System.err.println("Booking form validation failed");
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
            
            System.out.println("Creating booking: " + bookingId);
            System.out.println("  Tourist: " + currentUser.getUsername());
            System.out.println("  Attraction: " + attractionName);
            System.out.println("  Date: " + date);
            System.out.println("  Difficulty: " + difficulty);
            System.out.println("  Price: $" + price);
            
            if (FileManager.saveBooking(newBooking)) {
                System.out.println("✓ Booking saved successfully");
                showAlert("Booking successful! Booking ID: " + bookingId);
                loadBookings();
                clearForm();
            } else {
                System.err.println("✗ Failed to save booking");
                showAlert("Booking failed. Please try again.");
            }
        } catch (Exception e) {
            System.err.println("✗ Error creating booking: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error creating booking: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleUpdateBooking() {
        System.out.println("=== Handle Update Booking ===");
        
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
            System.out.println("Updating booking: " + selectedBooking.getBookingId());
            
            selectedBooking.setAttractionName(attractionComboBox.getValue());
            selectedBooking.setDate(datePicker.getValue().toString());
            selectedBooking.setDifficulty(difficultyComboBox.getValue());
            selectedBooking.setPrice(Double.parseDouble(priceLabel.getText().replace("$", "")));
            
            if (FileManager.updateBooking(selectedBooking)) {
                System.out.println("✓ Booking updated successfully");
                showAlert("Booking updated successfully!");
                loadBookings();
                clearForm();
            } else {
                System.err.println("✗ Failed to update booking");
                showAlert("Update failed. Please try again.");
            }
        } catch (Exception e) {
            System.err.println("✗ Error updating booking: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error updating booking: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleDeleteBooking() {
        System.out.println("=== Handle Delete Booking ===");
        
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
                System.out.println("Deleting booking: " + selectedBooking.getBookingId());
                
                if (FileManager.deleteBooking(selectedBooking.getBookingId())) {
                    System.out.println("✓ Booking deleted successfully");
                    showAlert("Booking deleted successfully!");
                    loadBookings();
                    clearForm();
                } else {
                    System.err.println("✗ Failed to delete booking");
                    showAlert("Delete failed. Please try again.");
                }
            } catch (Exception e) {
                System.err.println("✗ Error deleting booking: " + e.getMessage());
                e.printStackTrace();
                showAlert("Error deleting booking: " + e.getMessage());
            }
        }
    }
    
    @FXML
    private void toggleLanguage() {
        System.out.println("Toggling language from " + (isNepali ? "Nepali" : "English"));
        isNepali = !isNepali;
        updateLanguage();
    }
    
    @FXML
    private void handleLogout() {
        System.out.println("=== Handle Logout ===");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            System.out.println("✓ Logged out successfully");
        } catch (Exception e) {
            System.err.println("✗ Error logging out: " + e.getMessage());
            e.printStackTrace();
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
            System.out.println("Language updated to: " + (isNepali ? "Nepali" : "English"));
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
        System.out.println("Form cleared");
    }
    
    private void showAlert(String message) {
        try {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
            System.out.println("Alert shown: " + message);
        } catch (Exception e) {
            System.err.println("Error showing alert: " + e.getMessage());
            System.out.println("Alert message was: " + message);
        }
    }
}
