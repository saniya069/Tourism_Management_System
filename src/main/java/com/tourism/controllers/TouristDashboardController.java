package com.tourism.controllers;

import com.tourism.models.Attraction;
import com.tourism.models.Booking;
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
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

public class TouristDashboardController {
    
    @FXML private Label welcomeLabel;
    @FXML private ComboBox<Attraction> attractionComboBox;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> difficultyComboBox;
    @FXML private Label priceLabel;
    @FXML private Button bookButton;
    @FXML private Button languageToggle;
    @FXML private TableView<Booking> bookingsTable;
    @FXML private TableColumn<Booking, String> bookingIdColumn;
    @FXML private TableColumn<Booking, String> attractionColumn;
    @FXML private TableColumn<Booking, String> dateColumn;
    @FXML private TableColumn<Booking, String> difficultyColumn;
    @FXML private TableColumn<Booking, Double> priceColumn;
    @FXML private TableColumn<Booking, String> statusColumn;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button logoutButton;
    
    private String username;
    private LanguageManager languageManager = LanguageManager.getInstance();
    private ObservableList<Booking> bookingsList = FXCollections.observableArrayList();
    
    public void setUsername(String username) {
        this.username = username;
        welcomeLabel.setText("Welcome, " + username + "!");
        loadBookings();
    }
    
    @FXML
    private void initialize() {
        setupComboBoxes();
        setupTable();
        updateLanguage();
        
        attractionComboBox.setOnAction(e -> calculatePrice());
        difficultyComboBox.setOnAction(e -> calculatePrice());
        datePicker.setOnAction(e -> calculatePrice());
    }
    
    private void setupComboBoxes() {
        List<Attraction> attractions = FileManager.loadAttractions();
        attractionComboBox.setItems(FXCollections.observableArrayList(attractions));
        
        difficultyComboBox.getItems().addAll("Easy", "Medium", "Hard");
        
        attractionComboBox.setOnAction(e -> {
            Attraction selected = attractionComboBox.getValue();
            if (selected != null && "High".equals(selected.getAltitude())) {
                showHighAltitudeWarning();
            }
            calculatePrice();
        });
    }
    
    private void setupTable() {
        bookingIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        attractionColumn.setCellValueFactory(new PropertyValueFactory<>("attractionName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        difficultyColumn.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        bookingsTable.setItems(bookingsList);
    }
    
    @FXML
    private void handleBooking() {
        if (!validateBookingFields()) {
            return;
        }
        
        Attraction attraction = attractionComboBox.getValue();
        LocalDate date = datePicker.getValue();
        String difficulty = difficultyComboBox.getValue();
        double price = calculateFinalPrice();
        
        String bookingId = "B" + System.currentTimeMillis();
        Booking booking = new Booking(bookingId, username, attraction.getName(), 
                                    date.toString(), difficulty, price, "Confirmed");
        
        if (FileManager.saveBooking(booking)) {
            showSuccess("Booking created successfully!");
            loadBookings();
            clearFields();
            
            if (isFestivalSeason(date)) {
                showFestivalDiscountAlert();
            }
        } else {
            showError("Failed to create booking!");
        }
    }
    
    @FXML
    private void handleUpdateBooking() {
        Booking selected = bookingsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a booking to update!");
            return;
        }
        
        attractionComboBox.setValue(FileManager.getAttractionByName(selected.getAttractionName()));
        datePicker.setValue(LocalDate.parse(selected.getDate()));
        difficultyComboBox.setValue(selected.getDifficulty());
        
        if (validateBookingFields()) {
            selected.setAttractionName(attractionComboBox.getValue().getName());
            selected.setDate(datePicker.getValue().toString());
            selected.setDifficulty(difficultyComboBox.getValue());
            selected.setPrice(calculateFinalPrice());
            
            if (FileManager.updateBooking(selected)) {
                showSuccess("Booking updated successfully!");
                loadBookings();
                clearFields();
            } else {
                showError("Failed to update booking!");
            }
        }
    }
    
    @FXML
    private void handleDeleteBooking() {
        Booking selected = bookingsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a booking to delete!");
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Are you sure you want to delete this booking?");
        alert.setContentText("This action cannot be undone.");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (FileManager.deleteBooking(selected.getBookingId())) {
                showSuccess("Booking deleted successfully!");
                loadBookings();
            } else {
                showError("Failed to delete booking!");
            }
        }
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
        welcomeLabel.setText("Welcome, " + username + "!");
    }
    
    private void calculatePrice() {
        if (attractionComboBox.getValue() != null && difficultyComboBox.getValue() != null) {
            double price = calculateFinalPrice();
            priceLabel.setText("$" + String.format("%.2f", price));
        }
    }
    
    private double calculateFinalPrice() {
        if (attractionComboBox.getValue() == null || difficultyComboBox.getValue() == null) {
            return 0.0;
        }
        
        double basePrice = attractionComboBox.getValue().getBasePrice();
        String difficulty = difficultyComboBox.getValue();
        
        double multiplier = 1.0;
        switch (difficulty) {
            case "Medium": multiplier = 1.2; break;
            case "Hard": multiplier = 1.5; break;
        }
        
        double finalPrice = basePrice * multiplier;
        
        if (datePicker.getValue() != null && isFestivalSeason(datePicker.getValue())) {
            finalPrice *= 0.8; // 20% discount
        }
        
        return finalPrice;
    }
    
    private boolean isFestivalSeason(LocalDate date) {
        Month month = date.getMonth();
        return month == Month.AUGUST || month == Month.SEPTEMBER || month == Month.OCTOBER;
    }
    
    private void showFestivalDiscountAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Festival Discount Applied!");
        alert.setHeaderText("Dashain & Tihar Special");
        alert.setContentText("You have received a 20% festival discount on your booking!");
        alert.showAndWait();
    }
    
    private void showHighAltitudeWarning() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("High Altitude Warning");
        alert.setHeaderText("Safety Alert");
        alert.setContentText("This trek involves high altitude. Please ensure you are physically prepared and consult a doctor if necessary.");
        alert.showAndWait();
    }
    
    private boolean validateBookingFields() {
        if (attractionComboBox.getValue() == null ||
            datePicker.getValue() == null ||
            difficultyComboBox.getValue() == null) {
            showError("Please select attraction, date, and difficulty!");
            return false;
        }
        
        if (datePicker.getValue().isBefore(LocalDate.now())) {
            showError("Cannot book for past dates!");
            return false;
        }
        
        return true;
    }
    
    private void loadBookings() {
        List<Booking> userBookings = FileManager.loadUserBookings(username);
        bookingsList.clear();
        bookingsList.addAll(userBookings);
    }
    
    private void clearFields() {
        attractionComboBox.setValue(null);
        datePicker.setValue(null);
        difficultyComboBox.setValue(null);
        priceLabel.setText("$0.00");
    }
    
    private void updateLanguage() {
        bookButton.setText("Book Now");
        updateButton.setText("Update");
        deleteButton.setText("Delete");
        logoutButton.setText("Logout");
        languageToggle.setText(languageManager.getCurrentLanguage().equals("EN") ? "नेपाली" : "English");
    }
    
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
