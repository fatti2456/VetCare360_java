package com.vetcare360.controller;

import com.vetcare360.model.*;
import com.vetcare360.navigation.NavigationController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for adding a new visit
 */
public class VisitAddController implements Initializable {
    
    private Pet currentPet;
    
    @FXML
    private Label petNameLabel;
    
    @FXML
    private DatePicker visitDatePicker;
    
    @FXML
    private ComboBox<Veterinarian> vetComboBox;
    
    @FXML
    private TextArea descriptionArea;
    
    @FXML
    private TextArea diagnosisArea;
    
    @FXML
    private TextArea treatmentArea;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set defaults
        visitDatePicker.setValue(LocalDate.now());
        
        // Get pet from navigation data
        Object petData = NavigationController.getInstance().getData("pet");
        if (petData instanceof Pet) {
            currentPet = (Pet) petData;
            NavigationController.getInstance().clearData("pet");
            petNameLabel.setText(currentPet.getName());
            
            // Load veterinarians
            loadVeterinarians();
        } else {
            // Error handling
            System.err.println("No pet data found");
            NavigationController.getInstance().navigateTo("ownerSearch");
        }
    }
    
    /**
     * Load the veterinarians for the combo box
     */
    private void loadVeterinarians() {
        vetComboBox.getItems().addAll(DataStore.getInstance().getAllVeterinarians());
        
        // Set up display for veterinarians in combo box
        vetComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Veterinarian vet) {
                if (vet == null) return null;
                return vet.getFullName() + " (" + vet.getSpeciality() + ")";
            }
            
            @Override
            public Veterinarian fromString(String string) {
                return null; // Not needed for this use case
            }
        });
        
        // Select first vet by default
        if (!vetComboBox.getItems().isEmpty()) {
            vetComboBox.getSelectionModel().selectFirst();
        }
    }
    
    @FXML
    private void handleSave() {
        // Validate input
        if (!validateInput()) {
            return;
        }
        
        // Create new visit
        Visit newVisit = new Visit();
        newVisit.setPetId(currentPet.getId());
        newVisit.setDate(visitDatePicker.getValue());
        newVisit.setVeterinarianId(vetComboBox.getValue().getId());
        newVisit.setDescription(descriptionArea.getText().trim());
        newVisit.setDiagnosis(diagnosisArea.getText().trim());
        newVisit.setTreatment(treatmentArea.getText().trim());
        
        // Save to data store
        DataStore.getInstance().addVisit(newVisit);
        
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
        
        if (visitDatePicker.getValue() == null) {
            errorMessage.append("- Visit date is required\n");
        } else if (visitDatePicker.getValue().isAfter(LocalDate.now())) {
            errorMessage.append("- Visit date cannot be in the future\n");
        }
        
        if (vetComboBox.getValue() == null) {
            errorMessage.append("- Veterinarian is required\n");
        }
        
        if (descriptionArea.getText().trim().isEmpty()) {
            errorMessage.append("- Description is required\n");
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