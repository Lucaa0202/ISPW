package org.example.project3.dao;

import org.example.project3.exceptions.DAOException;
import org.example.project3.exceptions.LoginAndRegistrationException;
import org.example.project3.exceptions.MailAlreadyExistsException;
import org.example.project3.exceptions.NoResultException;
import org.example.project3.model.Credentials;
import org.example.project3.model.Customer;

public interface CustomerDAO {
    boolean emailExists(String email);
    boolean insertUser(Credentials credentials);
    void registerCustomer(Customer customer) throws MailAlreadyExistsException, LoginAndRegistrationException;
    void removeCustomer(Customer customer);
    Customer retrieveCustomerByMail(String mail) throws DAOException, NoResultException;
    default void modifyCustomer(Customer customer){
        throw new UnsupportedOperationException("Modifica del cliente non supportata da questa implementazione.");
    }
}
