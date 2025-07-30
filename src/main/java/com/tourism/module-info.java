module com.tourism {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    
    opens com.tourism to javafx.fxml;
    opens com.tourism.controllers to javafx.fxml;
    opens com.tourism.models to javafx.base;
    
    exports com.tourism;
    exports com.tourism.controllers;
    exports com.tourism.models;
    exports com.tourism.utils;
}
