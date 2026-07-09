package org.example.project3.dao;

import org.example.project3.exceptions.LoginAndRegistrationException;
import org.example.project3.exceptions.MailAlreadyExistsException;
import org.example.project3.exceptions.WrongEmailOrPasswordException;
import org.example.project3.model.Credentials;

public interface CredentialsDAO {
    boolean emailExists(String email);
    boolean insertUser(Credentials credentials);
    void login(Credentials credentials) throws WrongEmailOrPasswordException, LoginAndRegistrationException;
    default void modifyCredentials(Credentials newCredentials, Credentials oldCredentials) throws MailAlreadyExistsException{
        throw new UnsupportedOperationException("Modifica credenziali non supportata da questa implementazione.");
    }
}