package com.tourism;

import com.tourism.utils.FileManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize sample data
        FileManager.initializeSampleData();
        
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        primaryStage.setTitle("Nepal Tourism Management System");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setResizable(false);
        primaryStage.show();
        
        // Print login credentials for testing
        System.out.println("=== LOGIN CREDENTIALS ===");
        System.out.println("Admin: saniya / saniya123");
        System.out.println("Sample Tourist: tourist1 / password123");
        System.out.println("Sample Guide: guide1 / password123");
        System.out.println("========================");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
