package com.vetcare360.model;

import java.time.LocalDate;
import java.time.Period;

/**
 * Model class representing a pet
 */
public class Pet {
    private int id;
    private String name;
    private LocalDate birthDate;
    private String type;  // Dog, Cat, etc.
    private String breed;
    private String gender;
    private String color;
    private int ownerId;

    public Pet() {
        // Default constructor
    }

    public Pet(int id, String name, LocalDate birthDate, String type, String breed, String gender, String color, int ownerId) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.type = type;
        this.breed = breed;
        this.gender = gender;
        this.color = color;
        this.ownerId = ownerId;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getAge() {
        if (birthDate == null) {
            return 0;
        }
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    @Override
    public String toString() {
        return name + " (" + type + ", " + breed + ")";
    }
}