package org.example.project3.dao.csv;

import org.example.project3.dao.CredentialsDAO;
import org.example.project3.exceptions.MailAlreadyExistsException;
import org.example.project3.exceptions.WrongEmailOrPasswordException;
import org.example.project3.model.Credentials;
import org.example.project3.utilities.enums.Role;

import java.io.*;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CredentialsDAOCSV implements CredentialsDAO {

    private static final Logger LOGGER = Logger.getLogger(CredentialsDAOCSV.class.getName());
    private static final String FILE_PATH = "src/main/resources/data/credentials.csv";
    private static final String CSV_HEADER = "mail,password,role";

    // Usiamo una Mappa con la mail come chiave
    private final Map<String, Credentials> credentialsMap = new HashMap<>();

    private void ensureLoaded() {
        if (credentialsMap.isEmpty()) {
            loadCredentials();
        }
    }

    private void loadCredentials() {
        credentialsMap.clear();
        Path path = Paths.get(FILE_PATH);
        if (!Files.exists(path)) return;

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String header = reader.readLine();
            if (header == null) {
                return; // Se il file è vuoto, fermiamo la lettura
            }
            String line;
            while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
                String[] fields = line.split(",", -1);
                if (fields.length >= 3) {
                    String mail = fields[0];
                    String password = fields[1];
                    Role role = Role.valueOf(fields[2]); // Converte la stringa nell'Enum

                    Credentials cred = new Credentials(mail, password, role);
                    credentialsMap.put(normalizeEmail(mail), cred);
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Errore caricamento credentials.csv", e);
        }
    }

    private void saveCredentials() {
        Path path = Paths.get(FILE_PATH);
        try {
            Files.createDirectories(path.getParent());
            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                writer.write(CSV_HEADER);
                writer.newLine();
                for (Credentials c : credentialsMap.values()) {
                    writer.write(String.join(",", c.getMail(), c.getPassword(), c.getRole().name()));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Errore salvataggio credentials.csv", e);
        }
    }

    private String normalizeEmail(String mail) {
        return mail.trim().toLowerCase();
    }



    @Override
    public boolean emailExists(String mail) {
        ensureLoaded();
        return credentialsMap.containsKey(normalizeEmail(mail));
    }

    @Override
    public boolean insertUser(Credentials credentials) {
        ensureLoaded();
        if (credentialsMap.containsKey(normalizeEmail(credentials.getMail()))) {
            return false;
        }
        credentialsMap.put(normalizeEmail(credentials.getMail()), credentials);
        saveCredentials();
        return true;
    }


    @Override
    public Credentials login(Credentials credentials) throws WrongEmailOrPasswordException {
        ensureLoaded();
        Credentials stored = credentialsMap.get(normalizeEmail(credentials.getMail()));

        // Controlla se la mail esiste e se la password corrisponde
        if (stored == null || !stored.getPassword().equals(credentials.getPassword())) {
            throw new WrongEmailOrPasswordException("Email o password errati");
        }


        return stored;
    }

    @Override
    public void modifyCredentials(Credentials newCredentials, Credentials oldCredentials) throws MailAlreadyExistsException {
        ensureLoaded();
        if (!Objects.equals(normalizeEmail(newCredentials.getMail()), normalizeEmail(oldCredentials.getMail()))
                && emailExists(newCredentials.getMail())) {
            throw new MailAlreadyExistsException("Mail già registrata");
        }

        // Rimuove la vecchia credenziale e inserisce la nuova
        credentialsMap.remove(normalizeEmail(oldCredentials.getMail()));
        credentialsMap.put(normalizeEmail(newCredentials.getMail()), newCredentials);
        saveCredentials();
    }
}