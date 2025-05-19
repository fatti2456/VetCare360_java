package com.vetcare360.controller;

import com.vetcare360.model.DataStore;
import com.vetcare360.model.Owner;
import com.vetcare360.model.Pet;
import com.vetcare360.navigation.NavigationController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Controller for the owner search view
 */
public class OwnerSearchController implements Initializable {

    @FXML
    private TextField lastNameField;

    @FXML
    private Label resultLabel;

    @FXML
    private TableView<Owner> ownerTable;

    @FXML
    private TableColumn<Owner, String> nameColumn;

    @FXML
    private TableColumn<Owner, String> addressColumn;

    @FXML
    private TableColumn<Owner, String> phoneColumn;

    @FXML
    private TableColumn<Owner, String> petsColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Setup table columns
        nameColumn.setCellValueFactory(cellData -> {
            Owner owner = cellData.getValue();
            return javafx.beans.binding.Bindings.createStringBinding(
                    () -> owner.getFirstName() + " " + owner.getLastName());
        });

        addressColumn.setCellValueFactory(cellData -> {
            Owner owner = cellData.getValue();
            return javafx.beans.binding.Bindings.createStringBinding(
                    () -> owner.getAddress() + ", " + owner.getCity());
        });

        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        petsColumn.setCellValueFactory(cellData -> {
            Owner owner = cellData.getValue();
            List<Pet> pets = DataStore.getInstance().getPetsByOwnerId(owner.getId());
            String petNames = pets.stream()
                    .map(Pet::getName)
                    .collect(Collectors.joining(", "));
            return javafx.beans.binding.Bindings.createStringBinding(() -> petNames);
        });

        // Setup row double-click event
        ownerTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && ownerTable.getSelectionModel().getSelectedItem() != null) {
                Owner selectedOwner = ownerTable.getSelectionModel().getSelectedItem();
                NavigationController.getInstance().navigateTo("ownerDetails", "owner", selectedOwner);
            }
        });

        // Add listener for enter key in search field
        lastNameField.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                handleSearch();
            }
        });

        // Show all owners initially
        handleSearch();
    }

    @FXML
    private void handleSearch() {
        String searchTerm = lastNameField.getText().trim().toLowerCase();
        List<Owner> results;

        if (searchTerm.isEmpty()) {
            // Show all owners if search field is empty
            results = DataStore.getInstance().getAllOwners();
        } else {
            // Search by last name or first name
            results = DataStore.getInstance().getAllOwners().stream()
                    .filter(owner ->
                            owner.getLastName().toLowerCase().contains(searchTerm) ||
                                    owner.getFirstName().toLowerCase().contains(searchTerm))
                    .collect(Collectors.toList());
        }

        // Update the table
        ownerTable.getItems().setAll(results);

        // Show the results table and update count
        resultLabel.setVisible(true);
        ownerTable.setVisible(true);

        // Update result label
        int resultCount = results.size();
        resultLabel.setText(resultCount + " Owner" + (resultCount != 1 ? "s" : "") + " Found");
    }

    @FXML
    private void handleAddNewOwner() {
        NavigationController.getInstance().navigateTo("ownerAdd");
    }
}