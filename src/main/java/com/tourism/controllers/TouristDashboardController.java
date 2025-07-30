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
import com.tourism.models.Attraction;
import com.tourism.utils.FileManager;
import java.time.LocalDate;
import java.util.List;

public class TouristDashboardController {
    
    @FXML private Label welcomeLabel;
    @FXML private Button languageToggle;
    @FXML private Button logoutButton;
    @FXML private ComboBox<String> attractionComboBox;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> difficultyComboBox;
    @FXML private Label priceLabel;
    @FXML private Button bookButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
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
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
        initialize();
    }
    
    @FXML
    private void initialize() {
        if (currentUser != null) {
            welcomeLabel.setText("Welcome, " + currentUser.getFullName() + "!");
            
            // Initialize table columns
            bookingIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
            attractionColumn.setCellValueFactory(new PropertyValueFactory<>("attractionName"));
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
            difficultyColumn.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
            priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
            statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
            
            // Load data
            loadAttractions();
            loadDifficulties();
            loadBookings();
            
            // Set up event handlers
            attractionComboBox.setOnAction(e -> updatePrice());
            difficultyComboBox.setOnAction(e -> updatePrice());
        }
    }
    
    private void loadAttractions() {
        try {
            List<Attraction> attractions = FileManager.loadAttractions();
            ObservableList<String> attractionNames = FXCollections.observableArrayList();
            for (Attraction attraction : attractions) {
                attractionNames.add(attraction.getName());
            }
            attractionComboBox.setItems(attractionNames);
        } catch (Exception e) {
            showAlert("Error loading attractions: " + e.getMessage());
        }
    }
    
    private void loadDifficulties() {
        difficultyComboBox.setItems(FXCollections.observableArrayList("Easy", "Medium", "Hard", "Extreme"));
    }
    
    private void loadBookings() {
        try {
            List<Booking> bookings = FileManager.loadBookings();
            bookingsList.clear();
            for (Booking booking : bookings) {
                if (booking.getTouristName().equals(currentUser.getUsername())) {
                    bookingsList.add(booking);
                }
            }
            bookingsTable.setItems(bookingsList);
        } catch (Exception e) {
            showAlert("Error loading bookings: " + e.getMessage());
        }
    }
    
    private void updatePrice() {
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
                showAlert("Error calculating price: " + e.getMessage());
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
            showAlert("Error creating booking: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleUpdateBooking() {
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
            showAlert("Error updating booking: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleDeleteBooking() {
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
                showAlert("Error deleting booking: " + e.getMessage());
            }
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
            bookButton.setText("बुक गर्नुहोस्");
            updateButton.setText("अपडेट");
            deleteButton.setText("मेटाउनुहोस्");
        } else {
            welcomeLabel.setText("Welcome, " + currentUser.getFullName() + "!");
            languageToggle.setText("नेपाली");
            logoutButton.setText("Logout");
            bookButton.setText("Book Now");
            updateButton.setText("Update");
            deleteButton.setText("Delete");
        }
    }
    
    private boolean validateBookingForm() {
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
        attractionComboBox.setValue(null);
        datePicker.setValue(null);
        difficultyComboBox.setValue(null);
        priceLabel.setText("$0.00");
    }
    
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
