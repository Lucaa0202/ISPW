package org.example.project3.dao.csv;

import org.example.project3.dao.CustomerDAO;
import org.example.project3.exceptions.LoginAndRegistrationException;
import org.example.project3.exceptions.MailAlreadyExistsException;
import org.example.project3.exceptions.NoResultException;
import org.example.project3.model.Credentials;
import org.example.project3.model.Customer;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CustomerDAOCSV implements CustomerDAO {

    private static final String FILE_PATH = "src/main/resources/data/customers.csv";

    // Salviamo tutti i campi primari del Customer
    private static final String CSV_HEADER = "mail,name,surname,gender,isOnline,birthday,injury,startDate,endDate";

    private final Map<String, Customer> customersMap = new HashMap<>();

    private void ensureLoaded() {
        if (customersMap.isEmpty()) {
            loadCustomers();
        }
    }

    private void loadCustomers() {
        customersMap.clear();
        Path path = Paths.get(FILE_PATH);
        if (!Files.exists(path)) return;

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String header = reader.readLine();
            String line;
            while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
                String[] fields = line.split(",", -1);
                if (fields.length >= 9) {
                    String mail = fields[0];
                    Credentials cred = new Credentials(mail, null); // Password e Role sono in credentials.csv

                    Customer customer = new Customer(cred);
                    customer.setName(fields[1]);
                    customer.setSurname(fields[2]);
                    customer.setGender(fields[3]);
                    customer.setOnline(Boolean.parseBoolean(fields[4]));

                    if (!fields[5].isEmpty()) customer.setBirthday(LocalDate.parse(fields[5]));
                    customer.setInjury(fields[6]);

                    // Convertiamo i millisecondi salvati in formato testo in oggetti Date
                    if (!fields[7].isEmpty()) customer.setStartDate(new Date(Long.parseLong(fields[7])));
                    if (!fields[8].isEmpty()) customer.setEndDate(new Date(Long.parseLong(fields[8])));

                    customersMap.put(normalizeEmail(mail), customer);
                }
            }
        } catch (Exception e) {
            System.err.println("Errore caricamento customers.csv: " + e.getMessage());
        }
    }

    private void saveCustomers() {
        Path path = Paths.get(FILE_PATH);
        try {
            Files.createDirectories(path.getParent());
            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                writer.write(CSV_HEADER);
                writer.newLine();
                for (Customer c : customersMap.values()) {
                    String bday = (c.getBirthday() != null) ? c.getBirthday().toString() : "";
                    String sDate = (c.getStartDate() != null) ? String.valueOf(c.getStartDate().getTime()) : "";
                    String eDate = (c.getEndDate() != null) ? String.valueOf(c.getEndDate().getTime()) : "";
                    String inj = (c.getInjury() != null) ? c.getInjury().replace(",", ";") : "";

                    String line = String.join(",",
                            c.getCredentials().getMail(), c.getName(), c.getSurname(), c.getGender(),
                            String.valueOf(c.isOnline()), bday, inj, sDate, eDate
                    );
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Errore salvataggio customers.csv: " + e.getMessage());
        }
    }

    private String normalizeEmail(String mail) {
        return mail.trim().toLowerCase();
    }

    // ==========================================
    // METODI INTERFACCIA
    // ==========================================

    @Override
    public boolean emailExists(String mail) {
        ensureLoaded();
        return customersMap.containsKey(normalizeEmail(mail));
    }

    @Override
    public boolean insertUser(Credentials credentials) {
        // Logicamente l'inserimento puro della credenziale si fa in CredentialsDAO, ma lo replichiamo se richiesto dall'interfaccia
        return true;
    }

    @Override
    public void registerCustomer(Customer customer) throws MailAlreadyExistsException, LoginAndRegistrationException {
        ensureLoaded();
        if (emailExists(customer.getCredentials().getMail())) {
            throw new MailAlreadyExistsException("Mail già esistente");
        }
        customersMap.put(normalizeEmail(customer.getCredentials().getMail()), customer);
        saveCustomers();
    }

    @Override
    public void retrieveCustomer(Customer customer) throws NoResultException {
        ensureLoaded();
        Customer storedCustomer = customersMap.get(normalizeEmail(customer.getCredentials().getMail()));

        if (storedCustomer == null) {
            throw new NoResultException("Cliente non trovato");
        }

        // Esattamente la stessa logica del tuo DemoDAO: copiamo i dati
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
        ensureLoaded();
        customersMap.remove(normalizeEmail(customer.getCredentials().getMail()));
        saveCustomers();
    }

    @Override
    public void modifyCustomer(Customer customer) {
        ensureLoaded();
        customersMap.put(normalizeEmail(customer.getCredentials().getMail()), customer);
        saveCustomers();
    }
}