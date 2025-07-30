package com.tourism.controllers;

import com.tourism.models.Attraction;
import com.tourism.models.Booking;
import com.tourism.utils.FileManager;
import com.tourism.utils.LanguageManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
        welcomeLabel.setText(languageManager.getText("welcome") + ", " + username + "!");
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
            showSuccess(languageManager.getText("booking.success"));
            loadBookings();
            clearFields();
            
            // Check for festival discount
            if (isFestivalSeason(date)) {
                showFestivalDiscountAlert();
            }
        } else {
            showError(languageManager.getText("booking.failed"));
        }
    }
    
    @FXML
    private void handleUpdateBooking() {
        Booking selected = bookingsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError(languageManager.getText("select.booking.update"));
            return;
        }
        
        // Populate fields with selected booking data
        attractionComboBox.setValue(FileManager.getAttractionByName(selected.getAttractionName()));
        datePicker.setValue(LocalDate.parse(selected.getDate()));
        difficultyComboBox.setValue(selected.getDifficulty());
        
        // Update booking
        if (validateBookingFields()) {
            selected.setAttractionName(attractionComboBox.getValue().getName());
            selected.setDate(datePicker.getValue().toString());
            selected.setDifficulty(difficultyComboBox.getValue());
            selected.setPrice(calculateFinalPrice());
            
            if (FileManager.updateBooking(selected)) {
                showSuccess(languageManager.getText("booking.updated"));
                loadBookings();
                clearFields();
            } else {
                showError(languageManager.getText("booking.update.failed"));
            }
        }
    }
    
    @FXML
    private void handleDeleteBooking() {
        Booking selected = bookingsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError(languageManager.getText("select.booking.delete"));
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(languageManager.getText("confirm.delete"));
        alert.setHeaderText(languageManager.getText("delete.booking.confirm"));
        alert.setContentText(languageManager.getText("action.cannot.undone"));
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (FileManager.deleteBooking(selected.getBookingId())) {
                showSuccess(languageManager.getText("booking.deleted"));
                loadBookings();
            } else {
                showError(languageManager.getText("booking.delete.failed"));
            }
        }
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
        welcomeLabel.setText(languageManager.getText("welcome") + ", " + username + "!");
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
        
        // Difficulty multiplier
        double multiplier = 1.0;
        switch (difficulty) {
            case "Medium": multiplier = 1.2; break;
            case "Hard": multiplier = 1.5; break;
        }
        
        double finalPrice = basePrice * multiplier;
        
        // Festival discount (20% off between August-October)
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
        alert.setTitle(languageManager.getText("festival.discount.title"));
        alert.setHeaderText(languageManager.getText("festival.discount.header"));
        alert.setContentText(languageManager.getText("festival.discount.message"));
        alert.showAndWait();
    }
    
    private void showHighAltitudeWarning() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(languageManager.getText("altitude.warning.title"));
        alert.setHeaderText(languageManager.getText("altitude.warning.header"));
        alert.setContentText(languageManager.getText("altitude.warning.message"));
        alert.showAndWait();
    }
    
    private boolean validateBookingFields() {
        if (attractionComboBox.getValue() == null ||
            datePicker.getValue() == null ||
            difficultyComboBox.getValue() == null) {
            showError(languageManager.getText("error.empty.booking.fields"));
            return false;
        }
        
        if (datePicker.getValue().isBefore(LocalDate.now())) {
            showError(languageManager.getText("error.past.date"));
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
        bookButton.setText(languageManager.getText("book.button"));
        updateButton.setText(languageManager.getText("update.button"));
        deleteButton.setText(languageManager.getText("delete.button"));
        logoutButton.setText(languageManager.getText("logout.button"));
        languageToggle.setText(languageManager.getCurrentLanguage().equals("EN") ? "नेपाली" : "English");
    }
    
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(languageManager.getText("error.title"));
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(languageManager.getText("success.title"));
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
