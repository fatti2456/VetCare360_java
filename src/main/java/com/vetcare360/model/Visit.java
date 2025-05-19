package com.vetcare360.model;

import java.time.LocalDate;

/**
 * Model class representing a visit/appointment
 */
public class Visit {
    private int id;
    private int petId;
    private int veterinarianId;
    private LocalDate date;
    private String description;
    private String diagnosis;
    private String treatment;

    public Visit() {
        // Default constructor
    }

    public Visit(int id, int petId, int veterinarianId, LocalDate date, String description, String diagnosis, String treatment) {
        this.id = id;
        this.petId = petId;
        this.veterinarianId = veterinarianId;
        this.date = date;
        this.description = description;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public int getVeterinarianId() {
        return veterinarianId;
    }

    public void setVeterinarianId(int veterinarianId) {
        this.veterinarianId = veterinarianId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    @Override
    public String toString() {
        return date + ": " + description;
    }
}