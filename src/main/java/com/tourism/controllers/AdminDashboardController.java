package com.tourism.controllers;

import com.tourism.models.Attraction;
import com.tourism.models.Booking;
import com.tourism.models.User;
import com.tourism.utils.FileManager;
import com.tourism.utils.LanguageManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdminDashboardController {
    
    @FXML private TabPane mainTabPane;
    @FXML private Label welcomeLabel;
    @FXML private Button languageToggle;
    @FXML private Button logoutButton;
    
    // Guide Management Tab
    @FXML private TableView<User> guidesTable;
    @FXML private TableColumn<User, String> guideUsernameColumn;
    @FXML private TableColumn<User, String> guideNameColumn;
    @FXML private TableColumn<User, String> guideLanguagesColumn;
    @FXML private TableColumn<User, String> guideExperienceColumn;
    @FXML private TextField guideUsernameField;
    @FXML private TextField guideNameField;
    @FXML private TextField guideLanguagesField;
    @FXML private TextField guideExperienceField;
    @FXML private PasswordField guidePasswordField;
    @FXML private TextField guideEmailField;
    @FXML private TextField guidePhoneField;
    @FXML private Button addGuideButton;
    @FXML private Button updateGuideButton;
    @FXML private Button deleteGuideButton;
    
    // Attractions Management Tab
    @FXML private TableView<Attraction> attractionsTable;
    @FXML private TableColumn<Attraction, String> attractionNameColumn;
    @FXML private TableColumn<Attraction, String> attractionAltitudeColumn;
    @FXML private TableColumn<Attraction, String> attractionDifficultyColumn;
    @FXML private TableColumn<Attraction, Double> attractionPriceColumn;
    @FXML private TextField attractionNameField;
    @FXML private ComboBox<String> attractionAltitudeCombo;
    @FXML private ComboBox<String> attractionDifficultyCombo;
    @FXML private TextField attractionPriceField;
    @FXML private Button addAttractionButton;
    @FXML private Button updateAttractionButton;
    @FXML private Button deleteAttractionButton;
    
    // Bookings Management Tab
    @FXML private TableView<Booking> bookingsTable;
    @FXML private TableColumn<Booking, String> bookingIdColumn;
    @FXML private TableColumn<Booking, String> bookingTouristColumn;
    @FXML private TableColumn<Booking, String> bookingAttractionColumn;
    @FXML private TableColumn<Booking, String> bookingDateColumn;
    @FXML private TableColumn<Booking, String> bookingStatusColumn;
    @FXML private Button cancelBookingButton;
    
    // Analytics Tab
    @FXML private PieChart nationalityChart;
    @FXML private BarChart<String, Number> popularAttractionsChart;
    @FXML private Label totalRevenueLabel;
    
    private String username;
    private LanguageManager languageManager = LanguageManager.getInstance();
    private ObservableList<User> guidesList = FXCollections.observableArrayList();
    private ObservableList<Attraction> attractionsList = FXCollections.observableArrayList();
    private ObservableList<Booking> bookingsList = FXCollections.observableArrayList();
    
    public void setUsername(String username) {
        this.username = username;
        welcomeLabel.setText(languageManager.getText("welcome") + ", " + username + "!");
        loadAllData();
    }
    
    @FXML
    private void initialize() {
        setupTables();
        setupComboBoxes();
        updateLanguage();
    }
    
    private void setupTables() {
        // Guides table
        guideUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        guideNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        guideLanguagesColumn.setCellValueFactory(new PropertyValueFactory<>("languages"));
        guideExperienceColumn.setCellValueFactory(new PropertyValueFactory<>("experience"));
        guidesTable.setItems(guidesList);
        
        // Attractions table
        attractionNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        attractionAltitudeColumn.setCellValueFactory(new PropertyValueFactory<>("altitude"));
        attractionDifficultyColumn.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        attractionPriceColumn.setCellValueFactory(new PropertyValueFactory<>("basePrice"));
        attractionsTable.setItems(attractionsList);
        
        // Bookings table
        bookingIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        bookingTouristColumn.setCellValueFactory(new PropertyValueFactory<>("touristName"));
        bookingAttractionColumn.setCellValueFactory(new PropertyValueFactory<>("attractionName"));
        bookingDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        bookingStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        bookingsTable.setItems(bookingsList);
    }
    
    private void setupComboBoxes() {
        attractionAltitudeCombo.getItems().addAll("High", "Low");
        attractionDifficultyCombo.getItems().addAll("Easy", "Medium", "Hard");
    }
    
    private void loadAllData() {
        loadGuides();
        loadAttractions();
        loadBookings();
        updateAnalytics();
    }
    
    private void loadGuides() {
        List<User> guides = FileManager.loadGuides();
        guidesList.clear();
        guidesList.addAll(guides);
    }
    
    private void loadAttractions() {
        List<Attraction> attractions = FileManager.loadAttractions();
        attractionsList.clear();
        attractionsList.addAll(attractions);
    }
    
    private void loadBookings() {
        List<Booking> bookings = FileManager.loadAllBookings();
        bookingsList.clear();
        bookingsList.addAll(bookings);
    }
    
    // Guide Management Methods
    @FXML
    private void handleAddGuide() {
        if (!validateGuideFields()) return;
        
        User guide = new User(
            guideUsernameField.getText().trim(),
            com.tourism.utils.PasswordUtils.hashPassword(guidePasswordField.getText()),
            guideNameField.getText().trim(),
            guideEmailField.getText().trim(),
            guidePhoneField.getText().trim(),
            "Guide"
        );
        guide.setLanguages(guideLanguagesField.getText().trim());
        guide.setExperience(guideExperienceField.getText().trim());
        
        if (FileManager.saveUser(guide, "guides.txt")) {
            showSuccess(languageManager.getText("guide.added.success"));
            loadGuides();
            clearGuideFields();
        } else {
            showError(languageManager.getText("guide.add.failed"));
        }
    }
    
    @FXML
    private void handleUpdateGuide() {
        User selected = guidesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError(languageManager.getText("select.guide.update"));
            return;
        }
        
        if (!validateGuideFields()) return;
        
        selected.setFullName(guideNameField.getText().trim());
        selected.setEmail(guideEmailField.getText().trim());
        selected.setPhone(guidePhoneField.getText().trim());
        selected.setLanguages(guideLanguagesField.getText().trim());
        selected.setExperience(guideExperienceField.getText().trim());
        
        if (FileManager.updateGuide(selected)) {
            showSuccess(languageManager.getText("guide.updated.success"));
            loadGuides();
            clearGuideFields();
        } else {
            showError(languageManager.getText("guide.update.failed"));
        }
    }
    
    @FXML
    private void handleDeleteGuide() {
        User selected = guidesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError(languageManager.getText("select.guide.delete"));
            return;
        }
        
        if (confirmDelete(languageManager.getText("delete.guide.confirm"))) {
            if (FileManager.deleteGuide(selected.getUsername())) {
                showSuccess(languageManager.getText("guide.deleted.success"));
                loadGuides();
            } else {
                showError(languageManager.getText("guide.delete.failed"));
            }
        }
    }
    
    // Attraction Management Methods
    @FXML
    private void handleAddAttraction() {
        if (!validateAttractionFields()) return;
        
        Attraction attraction = new Attraction(
            attractionNameField.getText().trim(),
            attractionAltitudeCombo.getValue(),
            attractionDifficultyCombo.getValue(),
            Double.parseDouble(attractionPriceField.getText().trim())
        );
        
        if (FileManager.saveAttraction(attraction)) {
            showSuccess(languageManager.getText("attraction.added.success"));
            loadAttractions();
            clearAttractionFields();
        } else {
            showError(languageManager.getText("attraction.add.failed"));
        }
    }
    
    @FXML
    private void handleUpdateAttraction() {
        Attraction selected = attractionsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError(languageManager.getText("select.attraction.update"));
            return;
        }
        
        if (!validateAttractionFields()) return;
        
        selected.setName(attractionNameField.getText().trim());
        selected.setAltitude(attractionAltitudeCombo.getValue());
        selected.setDifficulty(attractionDifficultyCombo.getValue());
        selected.setBasePrice(Double.parseDouble(attractionPriceField.getText().trim()));
        
        if (FileManager.updateAttraction(selected)) {
            showSuccess(languageManager.getText("attraction.updated.success"));
            loadAttractions();
            clearAttractionFields();
        } else {
            showError(languageManager.getText("attraction.update.failed"));
        }
    }
    
    @FXML
    private void handleDeleteAttraction() {
        Attraction selected = attractionsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError(languageManager.getText("select.attraction.delete"));
            return;
        }
        
        if (confirmDelete(languageManager.getText("delete.attraction.confirm"))) {
            if (FileManager.deleteAttraction(selected.getName())) {
                showSuccess(languageManager.getText("attraction.deleted.success"));
                loadAttractions();
            } else {
                showError(languageManager.getText("attraction.delete.failed"));
            }
        }
    }
    
    @FXML
    private void handleCancelBooking() {
        Booking selected = bookingsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError(languageManager.getText("select.booking.cancel"));
            return;
        }
        
        if (confirmDelete(languageManager.getText("cancel.booking.confirm"))) {
            selected.setStatus("Cancelled");
            if (FileManager.updateBooking(selected)) {
                showSuccess(languageManager.getText("booking.cancelled.success"));
                loadBookings();
            } else {
                showError(languageManager.getText("booking.cancel.failed"));
            }
        }
    }
    
    private void updateAnalytics() {
        // Update total revenue
        double totalRevenue = bookingsList.stream()
            .filter(b -> !"Cancelled".equals(b.getStatus()))
            .mapToDouble(Booking::getPrice)
            .sum();
        totalRevenueLabel.setText(languageManager.getText("total.revenue") + ": $" + String.format("%.2f", totalRevenue));
        
        // Update nationality pie chart (mock data)
        ObservableList<PieChart.Data> nationalityData = FXCollections.observableArrayList(
            new PieChart.Data("Nepal", 40),
            new PieChart.Data("India", 25),
            new PieChart.Data("USA", 15),
            new PieChart.Data("Europe", 12),
            new PieChart.Data("Others", 8)
        );
        nationalityChart.setData(nationalityData);
        
        // Update popular attractions bar chart
        Map<String, Long> attractionCounts = bookingsList.stream()
            .filter(b -> !"Cancelled".equals(b.getStatus()))
            .collect(Collectors.groupingBy(Booking::getAttractionName, Collectors.counting()));
        
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(languageManager.getText("bookings"));
        
        attractionCounts.forEach((attraction, count) -> 
            series.getData().add(new XYChart.Data<>(attraction, count)));
        
        popularAttractionsChart.getData().clear();
        popularAttractionsChart.getData().add(series);
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
        updateAnalytics();
    }
    
    private boolean validateGuideFields() {
        if (guideUsernameField.getText().trim().isEmpty() ||
            guideNameField.getText().trim().isEmpty() ||
            guidePasswordField.getText().isEmpty() ||
            guideEmailField.getText().trim().isEmpty() ||
            guidePhoneField.getText().trim().isEmpty() ||
            guideLanguagesField.getText().trim().isEmpty() ||
            guideExperienceField.getText().trim().isEmpty()) {
            
            showError(languageManager.getText("error.empty.fields"));
            return false;
        }
        return true;
    }
    
    private boolean validateAttractionFields() {
        if (attractionNameField.getText().trim().isEmpty() ||
            attractionAltitudeCombo.getValue() == null ||
            attractionDifficultyCombo.getValue() == null ||
            attractionPriceField.getText().trim().isEmpty()) {
            
            showError(languageManager.getText("error.empty.fields"));
            return false;
        }
        
        try {
            Double.parseDouble(attractionPriceField.getText().trim());
        } catch (NumberFormatException e) {
            showError(languageManager.getText("error.invalid.price"));
            return false;
        }
        
        return true;
    }
    
    private void clearGuideFields() {
        guideUsernameField.clear();
        guideNameField.clear();
        guidePasswordField.clear();
        guideEmailField.clear();
        guidePhoneField.clear();
        guideLanguagesField.clear();
        guideExperienceField.clear();
    }
    
    private void clearAttractionFields() {
        attractionNameField.clear();
        attractionAltitudeCombo.setValue(null);
        attractionDifficultyCombo.setValue(null);
        attractionPriceField.clear();
    }
    
    private boolean confirmDelete(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(languageManager.getText("confirm.delete"));
        alert.setHeaderText(message);
        alert.setContentText(languageManager.getText("action.cannot.undone"));
        
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }
    
    private void updateLanguage() {
        addGuideButton.setText(languageManager.getText("add.button"));
        updateGuideButton.setText(languageManager.getText("update.button"));
        deleteGuideButton.setText(languageManager.getText("delete.button"));
        addAttractionButton.setText(languageManager.getText("add.button"));
        updateAttractionButton.setText(languageManager.getText("update.button"));
        deleteAttractionButton.setText(languageManager.getText("delete.button"));
        cancelBookingButton.setText(languageManager.getText("cancel.button"));
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
