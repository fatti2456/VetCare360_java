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
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for editing a pet
 */
public class PetEditController implements Initializable {
    
    private Pet currentPet;
    
    @FXML
    private Label petNameLabel;
    
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
        
        // Get pet from navigation data
        Object petData = NavigationController.getInstance().getData("pet");
        if (petData instanceof Pet) {
            currentPet = (Pet) petData;
            NavigationController.getInstance().clearData("pet");
            populateFields();
        } else {
            // Error handling
            System.err.println("No pet data found");
            NavigationController.getInstance().navigateTo("ownerSearch");
        }
    }
    
    /**
     * Populate the form fields with pet data
     */
    private void populateFields() {
        petNameLabel.setText(currentPet.getName());
        nameField.setText(currentPet.getName());
        birthDatePicker.setValue(currentPet.getBirthDate());
        typeComboBox.setValue(currentPet.getType());
        breedField.setText(currentPet.getBreed());
        genderComboBox.setValue(currentPet.getGender());
        colorField.setText(currentPet.getColor());
    }
    
    @FXML
    private void handleSave() {
        // Validate input
        if (!validateInput()) {
            return;
        }
        
        // Update pet
        currentPet.setName(nameField.getText().trim());
        currentPet.setBirthDate(birthDatePicker.getValue());
        currentPet.setType(typeComboBox.getValue());
        currentPet.setBreed(breedField.getText().trim());
        currentPet.setGender(genderComboBox.getValue());
        currentPet.setColor(colorField.getText().trim());
        
        // Save to data store
        DataStore.getInstance().updatePet(currentPet);
        
        // Navigate back to owner details
        Optional<Owner> owner = DataStore.getInstance().getOwnerById(currentPet.getOwnerId());
        owner.ifPresent(o -> NavigationController.getInstance().navigateTo("ownerDetails", "owner", o));
    }
    
    @FXML
    private void handleCancel() {
        Optional<Owner> owner = DataStore.getInstance().getOwnerById(currentPet.getOwnerId());
        owner.ifPresent(o -> NavigationController.getInstance().navigateTo("ownerDetails", "owner", o));
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