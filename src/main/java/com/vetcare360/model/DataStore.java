package com.vetcare360.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Singleton class to store all application data using ArrayLists
 */
public class DataStore {
    private static final DataStore instance = new DataStore();

    private final List<Veterinarian> veterinarians = new ArrayList<>();
    private final List<Owner> owners = new ArrayList<>();
    private final List<Pet> pets = new ArrayList<>();
    private final List<Visit> visits = new ArrayList<>();

    private DataStore() {
        // Private constructor to enforce singleton pattern
    }

    public static DataStore getInstance() {
        return instance;
    }

    /**
     * Initialize the data store with sample data for testing
     */
    public void initializeWithSampleData() {
        // Add sample veterinarians
        veterinarians.add(new Veterinarian(1, "Dr. Emma", "Wilson", "Cardiology", "emma.wilson@vetcare360.com", "+1 (555) 123-4567"));
        veterinarians.add(new Veterinarian(2, "Dr. James", "Smith", "Surgery", "james.smith@vetcare360.com", "+1 (555) 234-5678"));
        veterinarians.add(new Veterinarian(3, "Dr. Sophia", "Chen", "Dermatology", "sophia.chen@vetcare360.com", "+1 (555) 345-6789"));
        veterinarians.add(new Veterinarian(4, "Dr. Michael", "Johnson", "General", "michael.johnson@vetcare360.com", "+1 (555) 456-7890"));

        // Add sample owners
        Owner owner1 = new Owner(1, "John", "Doe", "123 Main St", "Cityville", "12345", "+1 (555) 111-2222", "john.doe@email.com");
        Owner owner2 = new Owner(2, "Jane", "Smith", "456 Oak Ave", "Townsburg", "67890", "+1 (555) 333-4444", "jane.smith@email.com");
        owners.add(owner1);
        owners.add(owner2);

        // Add sample pets
        Pet pet1 = new Pet(1, "Max", LocalDate.of(2018, 5, 10), "Dog", "Golden Retriever", "Male", "Golden", owner1.getId());
        Pet pet2 = new Pet(2, "Luna", LocalDate.of(2020, 3, 15), "Cat", "Siamese", "Female", "Cream", owner1.getId());
        Pet pet3 = new Pet(3, "Bella", LocalDate.of(2019, 8, 22), "Dog", "Beagle", "Female", "Tricolor", owner2.getId());
        pets.add(pet1);
        pets.add(pet2);
        pets.add(pet3);

        // Add sample visits
        visits.add(new Visit(1, pet1.getId(), 1, LocalDate.of(2023, 2, 15), "Annual checkup", "Healthy, no issues found", "None"));
        visits.add(new Visit(2, pet1.getId(), 4, LocalDate.of(2023, 6, 10), "Vaccination", "Administered rabies vaccine", "None"));
        visits.add(new Visit(3, pet2.getId(), 3, LocalDate.of(2023, 4, 5), "Skin issue", "Minor dermatitis, prescribed medication", "Apply cream twice daily"));
        visits.add(new Visit(4, pet3.getId(), 2, LocalDate.of(2023, 7, 20), "Surgery", "Removed foreign object from stomach", "Rest for 2 weeks, special diet"));
    }

    // Veterinarian methods
    public List<Veterinarian> getAllVeterinarians() {
        return new ArrayList<>(veterinarians);
    }

    public Optional<Veterinarian> getVeterinarianById(int id) {
        return veterinarians.stream()
                .filter(vet -> vet.getId() == id)
                .findFirst();
    }

    public void addVeterinarian(Veterinarian veterinarian) {
        // Generate new ID
        int newId = veterinarians.isEmpty() ? 1 : veterinarians.stream().mapToInt(Veterinarian::getId).max().getAsInt() + 1;
        veterinarian.setId(newId);
        veterinarians.add(veterinarian);
    }

    // Owner methods
    public List<Owner> getAllOwners() {
        return new ArrayList<>(owners);
    }

    public List<Owner> searchOwnersByLastName(String lastName) {
        return owners.stream()
                .filter(owner -> owner.getLastName().toLowerCase().contains(lastName.toLowerCase()))
                .toList();
    }

    public Optional<Owner> getOwnerById(int id) {
        return owners.stream()
                .filter(owner -> owner.getId() == id)
                .findFirst();
    }

    public void addOwner(Owner owner) {
        // Generate new ID (in a real app, this would be handled by a database)
        int newId = owners.isEmpty() ? 1 : owners.stream().mapToInt(Owner::getId).max().getAsInt() + 1;
        owner.setId(newId);
        owners.add(owner);
    }

    public void updateOwner(Owner owner) {
        for (int i = 0; i < owners.size(); i++) {
            if (owners.get(i).getId() == owner.getId()) {
                owners.set(i, owner);
                break;
            }
        }
    }

    public void deleteOwner(int ownerId) {
        // Delete all pets belonging to this owner
        List<Pet> ownerPets = getPetsByOwnerId(ownerId);
        for (Pet pet : ownerPets) {
            // Delete all visits for each pet
            List<Visit> petVisits = getVisitsByPetId(pet.getId());
            visits.removeAll(petVisits);
            // Delete the pet
            pets.removeIf(p -> p.getId() == pet.getId());
        }
        // Delete the owner
        owners.removeIf(owner -> owner.getId() == ownerId);
    }

    // Pet methods
    public List<Pet> getAllPets() {
        return new ArrayList<>(pets);
    }

    public List<Pet> getPetsByOwnerId(int ownerId) {
        return pets.stream()
                .filter(pet -> pet.getOwnerId() == ownerId)
                .toList();
    }

    public Optional<Pet> getPetById(int id) {
        return pets.stream()
                .filter(pet -> pet.getId() == id)
                .findFirst();
    }

    public void addPet(Pet pet) {
        // Generate new ID
        int newId = pets.isEmpty() ? 1 : pets.stream().mapToInt(Pet::getId).max().getAsInt() + 1;
        pet.setId(newId);
        pets.add(pet);
    }

    public void updatePet(Pet pet) {
        for (int i = 0; i < pets.size(); i++) {
            if (pets.get(i).getId() == pet.getId()) {
                pets.set(i, pet);
                break;
            }
        }
    }

    // Visit methods
    public List<Visit> getAllVisits() {
        return new ArrayList<>(visits);
    }

    public List<Visit> getVisitsByPetId(int petId) {
        return visits.stream()
                .filter(visit -> visit.getPetId() == petId)
                .toList();
    }

    public Optional<Visit> getVisitById(int id) {
        return visits.stream()
                .filter(visit -> visit.getId() == id)
                .findFirst();
    }

    public void addVisit(Visit visit) {
        // Generate new ID
        int newId = visits.isEmpty() ? 1 : visits.stream().mapToInt(Visit::getId).max().getAsInt() + 1;
        visit.setId(newId);
        visits.add(visit);
    }

    public void deleteVisit(int visitId) {
        visits.removeIf(visit -> visit.getId() == visitId);
    }
}