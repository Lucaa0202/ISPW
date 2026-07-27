package org.example.project3.dao.demo;

import org.example.project3.dao.CredentialsDAO;
import org.example.project3.dao.demo.shared.SharedResources;

import org.example.project3.exceptions.DAOException;

import org.example.project3.exceptions.MailAlreadyExistsException;
import org.example.project3.exceptions.WrongEmailOrPasswordException;
import org.example.project3.model.Credentials;
import org.example.project3.model.Customer;
import org.example.project3.model.Trainer;

import java.util.Objects;

public class CredentialsDAOP implements CredentialsDAO {

    @Override
    public boolean emailExists(String mail) {
        // Se la mail è null, di sicuro non esiste nel nostro "database"
        if (mail == null) {
            return false;
        }
        return SharedResources.getInstance().getUserTable().containsKey(normalizeEmail(mail));
    }

    private String normalizeEmail(String mail) {
        // Protezione extra (anche se ora emailExists lo filtra già)
        if (mail == null) {
            return "";
        }
        return mail.trim().toLowerCase();
    }

    //inserisco l'utente (credenziali) nel database
    public boolean insertUser(Credentials credentials)  {
        return SharedResources.getInstance().getUserTable().putIfAbsent(credentials.getMail(), credentials) == null;
    }

    @Override
    public void login(Credentials credentials) throws WrongEmailOrPasswordException {
        Credentials storedCredentials = SharedResources.getInstance().getUserTable().get(credentials.getMail());
        if (storedCredentials == null || !storedCredentials.getMail().equals(credentials.getMail())) {
            throw new WrongEmailOrPasswordException("Email o password errati");
        }
        credentials.setRole(storedCredentials.getRole());
    }

    @Override
    public void modifyCredentials(Credentials newCredentials, Credentials oldCredentials) throws MailAlreadyExistsException {
        if (!Objects.equals(newCredentials.getMail(), oldCredentials.getMail()) && emailExists(newCredentials.getMail())) {
            throw new MailAlreadyExistsException("Mail già registrata");
        }

        // Aggiornamento delle credenziali dell'utente
        if (SharedResources.getInstance().getTrainers().containsKey(oldCredentials.getMail())) {
            Trainer trainer = SharedResources.getInstance().getTrainers().remove(oldCredentials.getMail());
            trainer.setCredentials(newCredentials);
            SharedResources.getInstance().getTrainers().put(newCredentials.getMail(), trainer);
        } else if (SharedResources.getInstance().getCustomers().containsKey(oldCredentials.getMail())) {
            Customer customer = SharedResources.getInstance().getCustomers().remove(oldCredentials.getMail());
            customer.setCredentials(newCredentials);
            SharedResources.getInstance().getCustomers().put(newCredentials.getMail(), customer);
        } else {
            throw new DAOException();
        }
    }
}
