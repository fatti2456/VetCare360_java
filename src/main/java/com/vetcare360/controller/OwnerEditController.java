package com.vetcare360.controller;

import com.vetcare360.model.DataStore;
import com.vetcare360.model.Owner;
import com.vetcare360.navigation.NavigationController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for editing an owner
 */
public class OwnerEditController implements Initializable {
    
    private Owner currentOwner;
    
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
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Get owner from navigation data
        Object ownerData = NavigationController.getInstance().getData("owner");
        if (ownerData instanceof Owner) {
            currentOwner = (Owner) ownerData;
            NavigationController.getInstance().clearData("owner");
            populateFields();
        } else {
            // Error handling
            System.err.println("No owner data found");
            NavigationController.getInstance().navigateTo("ownerSearch");
        }
    }
    
    /**
     * Populate the form fields with owner data
     */
    private void populateFields() {
        firstNameField.setText(currentOwner.getFirstName());
        lastNameField.setText(currentOwner.getLastName());
        addressField.setText(currentOwner.getAddress());
        cityField.setText(currentOwner.getCity());
        zipCodeField.setText(currentOwner.getZipCode());
        phoneField.setText(currentOwner.getPhone());
        emailField.setText(currentOwner.getEmail());
    }
    
    @FXML
    private void handleSave() {
        // Validate input
        if (!validateInput()) {
            return;
        }
        
        // Update owner
        currentOwner.setFirstName(firstNameField.getText().trim());
        currentOwner.setLastName(lastNameField.getText().trim());
        currentOwner.setAddress(addressField.getText().trim());
        currentOwner.setCity(cityField.getText().trim());
        currentOwner.setZipCode(zipCodeField.getText().trim());
        currentOwner.setPhone(phoneField.getText().trim());
        currentOwner.setEmail(emailField.getText().trim());
        
        // Save to data store
        DataStore.getInstance().updateOwner(currentOwner);
        
        // Navigate back to owner details
        NavigationController.getInstance().navigateTo("ownerDetails", "owner", currentOwner);
    }
    
    @FXML
    private void handleCancel() {
        NavigationController.getInstance().navigateTo("ownerDetails", "owner", currentOwner);
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