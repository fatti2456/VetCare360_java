package com.vetcare360.controller;

import com.vetcare360.navigation.NavigationController;
import javafx.fxml.FXML;

/**
 * Controller for the welcome screen
 */
public class WelcomeController {
    
    @FXML
    private void handleViewVets() {
        NavigationController.getInstance().navigateTo("vets");
    }
    
    @FXML
    private void handleSearchOwners() {
        NavigationController.getInstance().navigateTo("ownerSearch");
    }
    
    @FXML
    private void handleViewAllOwners() {
        NavigationController.getInstance().navigateTo("ownerList");
    }
}