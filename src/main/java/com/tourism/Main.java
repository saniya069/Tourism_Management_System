package com.tourism;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.tourism.utils.FileManager;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialize data files
            FileManager.initializeDataFiles();
            
            // Load the login FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();
            
            Scene scene = new Scene(root);
            primaryStage.setTitle("Nepal Tourism Management System");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error starting application: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
