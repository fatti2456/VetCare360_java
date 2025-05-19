package com.vetcare360.controller;

import com.vetcare360.model.DataStore;
import com.vetcare360.model.Owner;
import com.vetcare360.navigation.NavigationController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

/**
 * Controller for adding a new owner
 */
public class OwnerAddController {
    
    @FXML
    private TextField firstNameField;
    
    @FXML
    private TextField lastNameField;
    
    @FXML
    private TextField addressField;
    
    @FXML
    private TextField cityField;
    
    @FXML
    private TextField zipCodeField;
    
    @FXML
    private TextField phoneField;
    
    @FXML
    private TextField emailField;
    
    @FXML
    private void handleSave() {
        // Validate input
        if (!validateInput()) {
            return;
        }
        
        // Create new owner
        Owner newOwner = new Owner();
        newOwner.setFirstName(firstNameField.getText().trim());
        newOwner.setLastName(lastNameField.getText().trim());
        newOwner.setAddress(addressField.getText().trim());
        newOwner.setCity(cityField.getText().trim());
        newOwner.setZipCode(zipCodeField.getText().trim());
        newOwner.setPhone(phoneField.getText().trim());
        newOwner.setEmail(emailField.getText().trim());
        
        // Save to data store
        DataStore.getInstance().addOwner(newOwner);
        
        // Navigate to owner details view
        NavigationController.getInstance().navigateTo("ownerDetails", "owner", newOwner);
    }
    
    @FXML
    private void handleCancel() {
        NavigationController.getInstance().navigateTo("ownerSearch");
    }
    
    /**
     * Validate the input fields
     * @return true if all required fields are filled
     */
    private boolean validateInput() {
        StringBuilder errorMessage = new StringBuilder();
        
        if (firstNameField.getText().trim().isEmpty()) {
            errorMessage.append("- First name is required\n");
        }
        
        if (lastNameField.getText().trim().isEmpty()) {
            errorMessage.append("- Last name is required\n");
        }
        
        if (phoneField.getText().trim().isEmpty()) {
            errorMessage.append("- Phone number is required\n");
        }
        
        if (errorMessage.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Input");
            alert.setHeaderText("Please correct the following errors:");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
            return false;
        }
        
        return true;
    }
}