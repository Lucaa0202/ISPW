package org.example.project3.dao.demo;

import org.example.project3.dao.CredentialsDAO;
import org.example.project3.dao.demo.shared.SharedResources;
import org.example.project3.exceptions.MailAlreadyExistsException;
import org.example.project3.exceptions.WrongEmailOrPasswordException;
import org.example.project3.model.Credentials;
import org.example.project3.model.Customer;
import org.example.project3.model.Trainer;

import java.util.Objects;

public class CredentialsDAOP implements CredentialsDAO {

    @Override
    public boolean emailExists(String mail) {
        if (mail == null) {
            return false;
        }
        return SharedResources.getInstance().getUserTable().containsKey(normalizeEmail(mail));
    }

    private String normalizeEmail(String mail) {
        if (mail == null) {
            return "";
        }
        return mail.trim().toLowerCase();
    }

    @Override
    public boolean insertUser(Credentials credentials)  {
        return SharedResources.getInstance().getUserTable().putIfAbsent(credentials.getMail(), credentials) == null;
    }


    @Override
    public Credentials login(Credentials credentials) throws WrongEmailOrPasswordException {
        Credentials storedCredentials = SharedResources.getInstance().getUserTable().get(credentials.getMail());

        // BUG FIX: Ora controlliamo la PASSWORD, non più la mail!
        if (storedCredentials == null || !storedCredentials.getPassword().equals(credentials.getPassword())) {
            throw new WrongEmailOrPasswordException("Email o password errati");
        }


        return storedCredentials;
    }

    @Override
    public void modifyCredentials(Credentials newCredentials, Credentials oldCredentials) throws MailAlreadyExistsException {
        if (!Objects.equals(newCredentials.getMail(), oldCredentials.getMail()) && emailExists(newCredentials.getMail())) {
            throw new MailAlreadyExistsException("Mail già registrata");
        }


        SharedResources.getInstance().getUserTable().remove(oldCredentials.getMail());
        SharedResources.getInstance().getUserTable().put(newCredentials.getMail(), newCredentials);


        if (SharedResources.getInstance().getTrainers().containsKey(oldCredentials.getMail())) {
            Trainer trainer = SharedResources.getInstance().getTrainers().remove(oldCredentials.getMail());
            trainer.setCredentials(newCredentials);
            SharedResources.getInstance().getTrainers().put(newCredentials.getMail(), trainer);
        } else if (SharedResources.getInstance().getCustomers().containsKey(oldCredentials.getMail())) {
            Customer customer = SharedResources.getInstance().getCustomers().remove(oldCredentials.getMail());
            customer.setCredentials(newCredentials);
            SharedResources.getInstance().getCustomers().put(newCredentials.getMail(), customer);
        }
    }
}
