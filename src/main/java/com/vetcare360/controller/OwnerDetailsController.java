package com.vetcare360.controller;

import com.vetcare360.model.*;
import com.vetcare360.navigation.NavigationController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for the owner details view
 */
public class OwnerDetailsController implements Initializable {

    private Owner currentOwner;

    @FXML
    private Label ownerNameLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Label cityLabel;

    @FXML
    private Label zipCodeLabel;

    @FXML
    private Label phoneLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private TableView<Pet> petsTable;

    @FXML
    private TableColumn<Pet, String> nameColumn;

    @FXML
    private TableColumn<Pet, String> typeColumn;

    @FXML
    private TableColumn<Pet, String> breedColumn;

    @FXML
    private TableColumn<Pet, String> birthDateColumn;

    @FXML
    private TableColumn<Pet, Void> actionsColumn;

    @FXML
    private Label visitTitleLabel;

    @FXML
    private TableView<Visit> visitsTable;

    @FXML
    private TableColumn<Visit, String> visitDateColumn;

    @FXML
    private TableColumn<Visit, String> petNameColumn;

    @FXML
    private TableColumn<Visit, String> vetNameColumn;

    @FXML
    private TableColumn<Visit, String> descriptionColumn;

    @FXML
    private TableColumn<Visit, Void> visitActionsColumn;

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Get owner from navigation data
        Object ownerData = NavigationController.getInstance().getData("owner");
        if (ownerData instanceof Owner) {
            currentOwner = (Owner) ownerData;
            NavigationController.getInstance().clearData("owner");
            displayOwnerDetails();
            setupPetsTable();
            setupVisitsTable();
            loadPets();
            loadVisits();
        } else {
            // Error handling
            System.err.println("No owner data found");
            NavigationController.getInstance().navigateTo("ownerSearch");
        }
    }

    /**
     * Display the owner's details in the UI
     */
    private void displayOwnerDetails() {
        ownerNameLabel.setText(currentOwner.getFullName());
        addressLabel.setText(currentOwner.getAddress());
        cityLabel.setText(currentOwner.getCity());
        zipCodeLabel.setText(currentOwner.getZipCode());
        phoneLabel.setText(currentOwner.getPhone());
        emailLabel.setText(currentOwner.getEmail());
    }

    /**
     * Set up the pets table columns
     */
    private void setupPetsTable() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        breedColumn.setCellValueFactory(new PropertyValueFactory<>("breed"));

        birthDateColumn.setCellValueFactory(cellData -> {
            Pet pet = cellData.getValue();
            return javafx.beans.binding.Bindings.createStringBinding(
                    () -> pet.getBirthDate().format(dateFormatter) + " (" + pet.getAge() + " yrs)");
        });

        // Set up the actions column with buttons
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button addVisitButton = new Button("Add Visit");

            {
                editButton.getStyleClass().add("small-button");
                addVisitButton.getStyleClass().add("small-button");

                editButton.setOnAction(event -> {
                    Pet pet = getTableView().getItems().get(getIndex());
                    handleEditPet(pet);
                });

                addVisitButton.setOnAction(event -> {
                    Pet pet = getTableView().getItems().get(getIndex());
                    handleAddVisit(pet);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(5, editButton, addVisitButton);
                    setGraphic(buttons);
                }
            }
        });
    }

    /**
     * Set up the visits table columns
     */
    private void setupVisitsTable() {
        visitDateColumn.setCellValueFactory(cellData -> {
            Visit visit = cellData.getValue();
            return javafx.beans.binding.Bindings.createStringBinding(
                    () -> visit.getDate().format(dateFormatter));
        });

        petNameColumn.setCellValueFactory(cellData -> {
            Visit visit = cellData.getValue();
            Optional<Pet> pet = DataStore.getInstance().getPetById(visit.getPetId());
            return javafx.beans.binding.Bindings.createStringBinding(
                    () -> pet.map(Pet::getName).orElse("Unknown"));
        });

        vetNameColumn.setCellValueFactory(cellData -> {
            Visit visit = cellData.getValue();
            Optional<Veterinarian> vet = DataStore.getInstance().getVeterinarianById(visit.getVeterinarianId());
            return javafx.beans.binding.Bindings.createStringBinding(
                    () -> vet.map(Veterinarian::getFullName).orElse("Unknown"));
        });

        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Set up the actions column with delete button
        visitActionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.getStyleClass().addAll("small-button", "delete-button");

                deleteButton.setOnAction(event -> {
                    Visit visit = getTableView().getItems().get(getIndex());
                    handleDeleteVisit(visit);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });
    }

    /**
     * Load the pets for the current owner
     */
    private void loadPets() {
        List<Pet> pets = DataStore.getInstance().getPetsByOwnerId(currentOwner.getId());
        petsTable.getItems().setAll(pets);
    }

    /**
     * Load the visits for all pets of the current owner
     */
    private void loadVisits() {
        List<Pet> pets = DataStore.getInstance().getPetsByOwnerId(currentOwner.getId());
        visitsTable.getItems().clear();

        for (Pet pet : pets) {
            List<Visit> petVisits = DataStore.getInstance().getVisitsByPetId(pet.getId());
            visitsTable.getItems().addAll(petVisits);
        }

        visitTitleLabel.setVisible(!visitsTable.getItems().isEmpty());
        visitsTable.setVisible(!visitsTable.getItems().isEmpty());
    }

    @FXML
    private void handleEditOwner() {
        NavigationController.getInstance().navigateTo("ownerEdit", "owner", currentOwner);
    }

    @FXML
    private void handleDeleteOwner() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Owner");
        alert.setHeaderText("Delete " + currentOwner.getFullName() + "?");
        alert.setContentText("This will also delete all pets and visits associated with this owner. This action cannot be undone.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            DataStore.getInstance().deleteOwner(currentOwner.getId());
            NavigationController.getInstance().navigateTo("ownerSearch");
        }
    }

    @FXML
    private void handleAddNewPet() {
        NavigationController.getInstance().navigateTo("petAdd", "owner", currentOwner);
    }

    private void handleEditPet(Pet pet) {
        NavigationController.getInstance().navigateTo("petEdit", "pet", pet);
    }

    private void handleAddVisit(Pet pet) {
        NavigationController.getInstance().navigateTo("visitAdd", "pet", pet);
    }

    private void handleDeleteVisit(Visit visit) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Visit");
        alert.setHeaderText("Delete this visit?");
        alert.setContentText("This action cannot be undone.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            DataStore.getInstance().deleteVisit(visit.getId());
            loadVisits();
        }
    }
}