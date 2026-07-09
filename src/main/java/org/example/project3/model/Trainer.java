package org.example.project3.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Trainer extends LoggedUser{
    private ArrayList<String> specializations;

    public Trainer(Credentials credentials, String name, String surname, String gender, boolean isOnline, LocalDate birthday) {
        super(credentials, name, surname, gender, isOnline, birthday);
        this.specializations = new ArrayList<>();
    }

    public Trainer(Credentials credentials) {
        super(credentials);
        this.specializations = new ArrayList<>();
    }

    public List<String> getSpecializations() {
        return specializations;
    }

    public void addSpecializations(String specialization) {
        specializations.add(specialization);
    }

    public void setSpecializations(List<String> specializations) {
        this.specializations = (ArrayList<String>) specializations;
    }
}
