package org.example.project3.dao.demo;

import org.example.project3.dao.CustomerDAO;
import org.example.project3.dao.demo.shared.SharedResources;

import org.example.project3.exceptions.MailAlreadyExistsException;
import org.example.project3.exceptions.NoResultException;
import org.example.project3.model.Credentials;
import org.example.project3.model.Customer;

public class CustomerDAOP implements CustomerDAO {

    @Override
    public boolean emailExists(String mail) {
        if (mail == null) return false;
        return SharedResources.getInstance().getUserTable().containsKey(normalizeEmail(mail));
    }

    private String normalizeEmail(String mail) {
        if (mail == null) return "";
        return mail.trim().toLowerCase();
    }


    @Override
    public boolean insertUser(Credentials credentials)  {
        return SharedResources.getInstance().getUserTable().putIfAbsent(credentials.getMail(), credentials) == null;
    }

    @Override
    public void registerCustomer(Customer customer) throws MailAlreadyExistsException {
        if (emailExists(customer.getCredentials().getMail())) {
            throw new MailAlreadyExistsException("Mail già esistente");
        }
        if (insertUser(customer.getCredentials())) {
            SharedResources.getInstance().getCustomers().put(customer.getCredentials().getMail(), customer);
        } else {
            throw new MailAlreadyExistsException("Errore nella registrazione: credenziali già presenti");
        }
    }


    @Override
    public Customer retrieveCustomerByMail(String mail) throws NoResultException {
        Customer storedCustomer = SharedResources.getInstance().getCustomers().get(mail);

        if (storedCustomer == null) {
            throw new NoResultException("Cliente non trovato con la mail: " + mail);
        }


        return storedCustomer;
    }

    @Override
    public void removeCustomer(Customer customer) {
        SharedResources.getInstance().getCustomers().remove(customer.getCredentials().getMail());

        SharedResources.getInstance().getUserTable().remove(customer.getCredentials().getMail());
    }

    @Override
    public void modifyCustomer(Customer customer) {
        SharedResources.getInstance().getCustomers().put(customer.getCredentials().getMail(), customer);
    }
}