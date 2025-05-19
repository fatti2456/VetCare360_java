package com.vetcare360.controller;

import com.vetcare360.model.DataStore;
import com.vetcare360.navigation.NavigationController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the main application view
 */
public class MainController implements Initializable {
    
    @FXML
    private Button homeButton;
    
    @FXML
    private Button vetsButton;
    
    @FXML
    private Button searchButton;
    
    @FXML
    private Button addOwnerButton;
    
    @FXML
    private Button allOwnersButton;
    
    @FXML
    private Label ownerCountLabel;
    
    @FXML
    private Label petCountLabel;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateStatistics();
    }
    
    /**
     * Update the statistics display in the sidebar
     */
    public void updateStatistics() {
        ownerCountLabel.setText(String.valueOf(DataStore.getInstance().getAllOwners().size()));
        petCountLabel.setText(String.valueOf(DataStore.getInstance().getAllPets().size()));
    }
    
    @FXML
    private void handleHomeButton() {
        NavigationController.getInstance().navigateTo("welcome");
    }
    
    @FXML
    private void handleVetsButton() {
        NavigationController.getInstance().navigateTo("vets");
    }
    
    @FXML
    private void handleSearchButton() {
        NavigationController.getInstance().navigateTo("ownerSearch");
    }
    
    @FXML
    private void handleAddOwnerButton() {
        NavigationController.getInstance().navigateTo("ownerAdd");
    }
    
    @FXML
    private void handleAllOwnersButton() {
        NavigationController.getInstance().navigateTo("ownerList");
    }
}