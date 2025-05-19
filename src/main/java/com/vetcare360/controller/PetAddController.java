package com.vetcare360.controller;

import com.vetcare360.model.DataStore;
import com.vetcare360.model.Owner;
import com.vetcare360.model.Pet;
import com.vetcare360.navigation.NavigationController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Controller for adding a new pet
 */
public class PetAddController implements Initializable {
    
    private Owner currentOwner;
    
    @FXML
    private Label ownerLabel;
    
    @FXML
    private TextField nameField;
    
    @FXML
    private DatePicker birthDatePicker;
    
    @FXML
    private ComboBox<String> typeComboBox;
    
    @FXML
    private TextField breedField;
    
    @FXML
    private ComboBox<String> genderComboBox;
    
    @FXML
    private TextField colorField;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set up combo boxes
        typeComboBox.getItems().addAll("Dog", "Cat", "Bird", "Rabbit", "Hamster", "Guinea Pig", "Other");
        genderComboBox.getItems().addAll("Male", "Female");
        
        // Set default values
        birthDatePicker.setValue(LocalDate.now().minusYears(1));
        
        // Get owner from navigation data
        Object ownerData = NavigationController.getInstance().getData("owner");
        if (ownerData instanceof Owner) {
            currentOwner = (Owner) ownerData;
            NavigationController.getInstance().clearData("owner");
            ownerLabel.setText("Owner: " + currentOwner.getFullName());
        } else {
            // Error handling
            System.err.println("No owner data found");
            NavigationController.getInstance().navigateTo("ownerSearch");
        }
    }
    
    @FXML
    private void handleSave() {
        // Validate input
        if (!validateInput()) {
            return;
        }
        
        // Create new pet
        Pet newPet = new Pet();
        newPet.setName(nameField.getText().trim());
        newPet.setBirthDate(birthDatePicker.getValue());
        newPet.setType(typeComboBox.getValue());
        newPet.setBreed(breedField.getText().trim());
        newPet.setGender(genderComboBox.getValue());
        newPet.setColor(colorField.getText().trim());
        newPet.setOwnerId(currentOwner.getId());
        
        // Save to data store
        DataStore.getInstance().addPet(newPet);
        
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
        
        if (nameField.getText().trim().isEmpty()) {
            errorMessage.append("- Pet name is required\n");
        }
        
        if (birthDatePicker.getValue() == null) {
            errorMessage.append("- Birth date is required\n");
        } else if (birthDatePicker.getValue().isAfter(LocalDate.now())) {
            errorMessage.append("- Birth date cannot be in the future\n");
        }
        
        if (typeComboBox.getValue() == null) {
            errorMessage.append("- Pet type is required\n");
        }
        
        if (genderComboBox.getValue() == null) {
            errorMessage.append("- Gender is required\n");
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