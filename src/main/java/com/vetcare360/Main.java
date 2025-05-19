package com.vetcare360;

import com.vetcare360.model.DataStore;
import com.vetcare360.navigation.NavigationController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialize data storage
            DataStore.getInstance().initializeWithSampleData();
            
            // Load the main interface
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
            Parent root = loader.load();
            
            // Initialize the navigation controller
            NavigationController.getInstance().initialize(primaryStage, loader);
            
            // Set up the scene
            Scene scene = new Scene(root, 1024, 768);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/styles.css")).toExternalForm());
            
            // Configure the stage
            primaryStage.setTitle("VetCare 360");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);
            
            // Try to load application icon
            try {
                primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/app_icon.png"))));
            } catch (Exception e) {
                System.out.println("Could not load application icon: " + e.getMessage());
            }
            
            primaryStage.show();
            
            // Navigate to welcome screen
            NavigationController.getInstance().navigateTo("welcome");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}