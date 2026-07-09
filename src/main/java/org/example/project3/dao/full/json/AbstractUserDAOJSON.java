package org.example.project3.dao.full.json;

import org.example.project3.model.Credentials;
import org.example.project3.model.Customer;
import org.example.project3.model.LoggedUser;
import org.example.project3.model.Trainer;
import org.example.project3.utilities.enums.Role;
import org.example.project3.utilities.others.Printer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractUserDAOJSON {


    protected static final String PERSISTENCE_FILE = "src/main/resources/org/example/project3/json.json";
    protected List<LoggedUser> userList = new ArrayList<>();

    protected AbstractUserDAOJSON() {
    }

    protected void loadFile() {
        userList.clear(); // Pulisce la lista prima di ricaricare per evitare duplicati
        try (BufferedReader reader = new BufferedReader(new FileReader(PERSISTENCE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                Credentials credentials = new Credentials(data[0], data[1], Role.valueOf(data[2]));
                if (credentials.getRole() == null) {
                    throw new IllegalArgumentException("Invalid role");
                } else {
                    LoggedUser loggedUser = getLoggedUser(credentials, data); // Metodo astratto
                    if (loggedUser != null) {
                        userList.add(loggedUser);
                    }
                }
            }
        } catch (IOException _) {
            Printer.errorPrint("Impossibile caricare gli utenti dal file utenti.");
        }
    }


    protected LoggedUser getLoggedUser(Credentials credentials, String[] data){
        LoggedUser loggedUser = null;
        if(credentials.getRole().equals(Role.CLIENT)) {
            loggedUser = new Customer(credentials, data[3], data[4], data[5], Boolean.parseBoolean(data[6]), LocalDate.parse(data[7]));

        }
        else if(credentials.getRole().equals(Role.TRAINER)) {
            loggedUser = new Trainer(credentials, data[3], data[4], data[5], Boolean.parseBoolean(data[6]), LocalDate.parse(data[7]));
        }
        return loggedUser;
    }

    public boolean emailExists(String mail) {
        // Si assicura che userList sia caricata prima di fare la ricerca
        if (userList.isEmpty()) {
            loadFile(); // Carica la lista se è vuota
        }
        return userList.stream().anyMatch(p -> p.getCredentials().getMail().equals(mail));
    }

    public boolean insertUser(Credentials credentials) {
        return !emailExists(credentials.getMail());
    }


    protected void addToFile(LoggedUser loggedUser) {
        try (FileWriter writer = new FileWriter(PERSISTENCE_FILE, true)) {
            String userString = convertUserToString(loggedUser, loggedUser.getCredentials());
            writer.write(userString + "\n");
            loadFile();
        } catch (IOException _) {
            Printer.errorPrint("Impossibile salvare l'utente sul file JSON.");
        }
    }

    protected String convertUserToString(LoggedUser loggedUser,Credentials credentials){
        // Costruisce la stringa da aggqingere al file
        return credentials.getMail() + "," +
                credentials.getPassword() + "," +
                credentials.getRole() + "," +
                loggedUser.getName() + "," +
                loggedUser.getSurname() + "," +
                loggedUser.getGender() + "," +
                loggedUser.isOnline() + "," +
                loggedUser.getBirthday();
    }

    protected void setUserParameters(LoggedUser user, LoggedUser app) {
        user.setName(app.getName());
        user.setSurname(app.getSurname());
        user.setGender(app.getGender());
        user.setOnline(app.isOnline());
        user.setBirthday(app.getBirthday());
    }
}