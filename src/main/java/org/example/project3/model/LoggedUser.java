package org.example.project3.model;

import java.time.LocalDate;

public abstract class LoggedUser {
    private Credentials credentials;
    private String name;
    private String surname;
    private String gender;
    private boolean online;
    private LocalDate birthday;

    protected  LoggedUser(Credentials credentials){
        this.credentials = credentials;
        this.name = null;
        this.surname = null;
        this.gender = null;
        this.online = false;
    }

    protected LoggedUser(Credentials credentials, String name, String surname, String gender, boolean isOnline, LocalDate birthday) {
        this.credentials = credentials;
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.online = isOnline;
        this.birthday = birthday;
    }

    //getter e setter per i nuovi campi

    public Credentials getCredentials() {
        return credentials;
    }

    //FORSE DA TOGLIERE
    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSurname() {
        return surname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public Boolean isOnline() {
        return online;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

}
