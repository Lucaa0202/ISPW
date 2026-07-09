package org.example.project3.dao;

import org.example.project3.exceptions.LoginAndRegistrationException;
import org.example.project3.exceptions.MailAlreadyExistsException;
import org.example.project3.exceptions.NoResultException;
import org.example.project3.model.Credentials;
import org.example.project3.model.Customer;

public interface CustomerDAO {
    boolean emailExists(String email);
    boolean insertUser(Credentials credentials);
    void registerCustomer(Customer customer) throws MailAlreadyExistsException, LoginAndRegistrationException;
    void retrieveCustomer(Customer customer) throws NoResultException;
    void removeCustomer(Customer customer);
    default void modifyCustomer(Customer customer){
        throw new UnsupportedOperationException("Modifica del cliente non supportata da questa implementazione.");
    }
}
