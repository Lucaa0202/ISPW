package org.example.project3.dao.demo;

import org.example.project3.dao.CustomerDAO;
import org.example.project3.dao.demo.shared.SharedResources;

import org.example.project3.exceptions.LoginAndRegistrationException;
import org.example.project3.exceptions.MailAlreadyExistsException;
import org.example.project3.exceptions.NoResultException;
import org.example.project3.model.Credentials;
import org.example.project3.model.Customer;


public class CustomerDAOP implements CustomerDAO {
    public boolean emailExists(String mail) {
        return SharedResources.getInstance().getUserTable().containsKey(normalizeEmail(mail));
    }

    private String normalizeEmail(String mail) {
        return mail.trim().toLowerCase();
    }

    //inserisco l'utente (credenziali) nel database
    public boolean insertUser(Credentials credentials)  {
        return SharedResources.getInstance().getUserTable().putIfAbsent(credentials.getMail(), credentials) == null;
    }

    public void registerCustomer(Customer customer) throws MailAlreadyExistsException, LoginAndRegistrationException {
        if (emailExists(customer.getCredentials().getMail())) {
            throw new MailAlreadyExistsException("Mail già esistente");
        }
        if (insertUser(customer.getCredentials())) {
            SharedResources.getInstance().getCustomers().put(customer.getCredentials().getMail(), customer);
        } else {
            throw new LoginAndRegistrationException("Errore nella registrazione");
        }

    }

    @Override
    public void retrieveCustomer(Customer customer) throws NoResultException {
        Customer storedCustomer = SharedResources.getInstance().getCustomers().get(customer.getCredentials().getMail());
        if (storedCustomer == null) {
            throw new NoResultException(customer.getClass().getSimpleName() + " non trovato");
        }
        customer.setName(storedCustomer.getName());
        customer.setSurname(storedCustomer.getSurname());
        customer.setGender(storedCustomer.getGender());
        customer.setOnline(storedCustomer.isOnline());
        customer.setBirthday(storedCustomer.getBirthday());
        customer.setSubscription(storedCustomer.getSubscription());
        customer.setInjury(storedCustomer.getInjury());
        customer.setStartDate(storedCustomer.getStartDate());
        customer.setEndDate(storedCustomer.getEndDate());
    }

    @Override
    public void removeCustomer(Customer customer) {
        SharedResources.getInstance().getCustomers().remove(customer.getCredentials().getMail());
    }

    @Override
    public void modifyCustomer(Customer customer) {
        SharedResources.getInstance().getCustomers().put(customer.getCredentials().getMail(), customer);
    }
}