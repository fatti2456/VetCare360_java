package com.vetcare360.controller;
import javafx.scene.Node;

import com.vetcare360.model.DataStore;
import com.vetcare360.model.Veterinarian;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for the veterinarian list view
 */
public class VeterinarianListController implements Initializable {

    @FXML
    private TableView<Veterinarian> vetTable;

    @FXML
    private TableColumn<Veterinarian, String> nameColumn;

    @FXML
    private TableColumn<Veterinarian, String> specialityColumn;

    @FXML
    private TableColumn<Veterinarian, String> emailColumn;

    @FXML
    private TableColumn<Veterinarian, String> phoneColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Setup table columns
        nameColumn.setCellValueFactory(cellData -> {
            Veterinarian vet = cellData.getValue();
            return javafx.beans.binding.Bindings.createStringBinding(
                    () -> vet.getFirstName() + " " + vet.getLastName());
        });

        specialityColumn.setCellValueFactory(new PropertyValueFactory<>("speciality"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        // Load data
        loadVeterinarians();
    }

    private void loadVeterinarians() {
        vetTable.getItems().setAll(DataStore.getInstance().getAllVeterinarians());
    }

    @FXML
    private void handleAddNewVet() {
        // Create a dialog for adding a new veterinarian
        Dialog<Veterinarian> dialog = new Dialog<>();
        dialog.setTitle("Add New Veterinarian");
        dialog.setHeaderText("Enter veterinarian details");

        // Set the button types
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Create the form grid
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First Name");
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Last Name");
        TextField specialityField = new TextField();
        specialityField.setPromptText("Speciality");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone");

        grid.add(new Label("First Name:"), 0, 0);
        grid.add(firstNameField, 1, 0);
        grid.add(new Label("Last Name:"), 0, 1);
        grid.add(lastNameField, 1, 1);
        grid.add(new Label("Speciality:"), 0, 2);
        grid.add(specialityField, 1, 2);
        grid.add(new Label("Email:"), 0, 3);
        grid.add(emailField, 1, 3);
        grid.add(new Label("Phone:"), 0, 4);
        grid.add(phoneField, 1, 4);

        // Enable/Disable save button depending on whether all data was entered
        Node saveButton = dialog.getDialogPane().lookupButton(saveButtonType);
        saveButton.setDisable(true);

        // Add validators
        firstNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            saveButton.setDisable(newValue.trim().isEmpty() || lastNameField.getText().trim().isEmpty() ||
                    specialityField.getText().trim().isEmpty() || emailField.getText().trim().isEmpty() ||
                    phoneField.getText().trim().isEmpty());
        });

        lastNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            saveButton.setDisable(newValue.trim().isEmpty() || firstNameField.getText().trim().isEmpty() ||
                    specialityField.getText().trim().isEmpty() || emailField.getText().trim().isEmpty() ||
                    phoneField.getText().trim().isEmpty());
        });

        specialityField.textProperty().addListener((observable, oldValue, newValue) -> {
            saveButton.setDisable(newValue.trim().isEmpty() || firstNameField.getText().trim().isEmpty() ||
                    lastNameField.getText().trim().isEmpty() || emailField.getText().trim().isEmpty() ||
                    phoneField.getText().trim().isEmpty());
        });

        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            saveButton.setDisable(newValue.trim().isEmpty() || firstNameField.getText().trim().isEmpty() ||
                    lastNameField.getText().trim().isEmpty() || specialityField.getText().trim().isEmpty() ||
                    phoneField.getText().trim().isEmpty());
        });

        phoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            saveButton.setDisable(newValue.trim().isEmpty() || firstNameField.getText().trim().isEmpty() ||
                    lastNameField.getText().trim().isEmpty() || specialityField.getText().trim().isEmpty() ||
                    emailField.getText().trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the first name field by default
        firstNameField.requestFocus();

        // Convert the result to a veterinarian object when the save button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return new Veterinarian(
                        0, // ID will be generated by DataStore
                        firstNameField.getText().trim(),
                        lastNameField.getText().trim(),
                        specialityField.getText().trim(),
                        emailField.getText().trim(),
                        phoneField.getText().trim()
                );
            }
            return null;
        });

        Optional<Veterinarian> result = dialog.showAndWait();
        result.ifPresent(veterinarian -> {
            DataStore.getInstance().addVeterinarian(veterinarian);
            loadVeterinarians();
        });
    }
}