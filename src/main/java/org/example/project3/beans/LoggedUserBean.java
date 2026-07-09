package org.example.project3.beans;


import java.time.LocalDate;

public abstract class LoggedUserBean {
    private CredentialsBean credentialsBean;
    private String name;
    private String surname;
    private String gender;
    private boolean online;
    private LocalDate birthday;

    protected LoggedUserBean(CredentialsBean credentialsBean, String name, String surname, String gender, boolean isOnline, LocalDate birthday) {
        this.credentialsBean = credentialsBean;
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.online = isOnline;
        this.birthday = birthday;
    }

    protected LoggedUserBean(CredentialsBean credentialsBean){
        this.credentialsBean = credentialsBean;
        this.name = null;
        this.surname = null;
        this.gender = null;
        this.online = false;
        this.birthday = null;
    }
    //getter e setter

    public CredentialsBean getCredentialsBean() {
        return credentialsBean;
    }

    public void setCredentialsBean(CredentialsBean credentials) {
        this.credentialsBean = credentials;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
}