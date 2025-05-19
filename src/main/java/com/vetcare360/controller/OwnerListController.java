package com.vetcare360.controller;

import com.vetcare360.model.DataStore;
import com.vetcare360.model.Owner;
import com.vetcare360.model.Pet;
import com.vetcare360.navigation.NavigationController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Controller for the owner list view
 */
public class OwnerListController implements Initializable {
    
    @FXML
    private TableView<Owner> ownerTable;
    
    @FXML
    private TableColumn<Owner, String> nameColumn;
    
    @FXML
    private TableColumn<Owner, String> addressColumn;
    
    @FXML
    private TableColumn<Owner, String> cityColumn;
    
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
        
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
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
        
        // Load data
        loadOwners();
    }
    
    private void loadOwners() {
        ownerTable.getItems().setAll(DataStore.getInstance().getAllOwners());
    }
}