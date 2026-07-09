package org.example.project3.beans;

import org.example.project3.utilities.enums.Role;

public class CredentialsBean {
    //Le seguenti variabili sono dichiarate come final per garantire l'integrità dei dati dell'oggetto Credentials
    private String mail;
    private String password;
    private Role role;


    //costruttore
    public CredentialsBean(String mail, String password, Role role){
        this.mail = mail;
        this.password = password;
        this.role = role;
    }

    public CredentialsBean(String mail, Role role){
        this.mail = mail;
        this.password = null;
        this.role = role;
    }

    //getters
    public String getMail(){
        return mail;
    }

    public String getPassword(){ return password; }

    public Role getRole(){
        return role;
    }
    public void setRole(Role role){
        this.role = role;
    }

}