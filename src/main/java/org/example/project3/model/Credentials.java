package org.example.project3.model;

import org.example.project3.utilities.enums.Role;

public class Credentials {
    //Le seguenti variabili sono dichiarate come final per garantire l'integrità dei dati dell'oggetto Credentials
    private final String mail;
    private final String password;
    private Role role;


    public Credentials(String mail, Role role){
        this.mail = mail;
        this.password = null;
        this.role = role;
    }

    //costruttore
    public Credentials(String mail, String password, Role role){
        this.mail = mail;
        this.password = password;
        this.role = role;
    }

    //getters
    public String getMail(){
        return mail;
    }

    public String getPassword(){ return password; }

    public void setRole(Role role){
        this.role = role;
    }

    public Role getRole(){
        return role;
    }

}
