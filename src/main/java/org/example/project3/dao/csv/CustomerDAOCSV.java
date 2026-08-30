package org.example.project3.dao.csv;

import org.example.project3.dao.CustomerDAO;
import org.example.project3.exceptions.LoginAndRegistrationException;
import org.example.project3.exceptions.MailAlreadyExistsException;
import org.example.project3.exceptions.NoResultException;
import org.example.project3.model.Credentials;
import org.example.project3.model.Customer;
import org.example.project3.utilities.enums.Role; // IMPORTANTE: aggiunto per il Role

import java.io.*;
import java.nio.file.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomerDAOCSV implements CustomerDAO {

    private static final String FILE_PATH = "src/main/resources/data/customers.csv";
    private static final Logger LOGGER = Logger.getLogger(CustomerDAOCSV.class.getName());
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
            if (header == null) {
                return;
            }

            String line;
            while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
                processCustomerLine(line);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Errore caricamento customers.csv", e);
        }
    }

    private void processCustomerLine(String line) {
        String[] fields = line.split(",", -1);

        if (fields.length < 9) {
            return;
        }

        String mail = fields[0];

        Credentials cred = new Credentials(mail, Role.CLIENT);

        Customer customer = new Customer(cred);
        customer.setName(fields[1]);
        customer.setSurname(fields[2]);
        customer.setGender(fields[3]);
        customer.setOnline(Boolean.parseBoolean(fields[4]));

        if (!fields[5].isEmpty()) {
            customer.setBirthday(LocalDate.parse(fields[5]));
        }

        customer.setInjury(fields[6]);

        if (!fields[7].isEmpty()) {
            customer.setStartDate(Date.from(Instant.ofEpochMilli(Long.parseLong(fields[7]))));
        }

        if (!fields[8].isEmpty()) {
            customer.setEndDate(Date.from(Instant.ofEpochMilli(Long.parseLong(fields[8]))));
        }

        customersMap.put(normalizeEmail(mail), customer);
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
            LOGGER.log(Level.SEVERE, "Errore salvataggio customers.csv", e);
        }
    }

    private String normalizeEmail(String mail) { return mail.trim().toLowerCase(); }

    @Override
    public boolean emailExists(String mail) {
        ensureLoaded();
        return customersMap.containsKey(normalizeEmail(mail));
    }

    @Override
    public boolean insertUser(Credentials credentials) { return true; }

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
    public Customer retrieveCustomerByMail(String mail) throws NoResultException {
        ensureLoaded();
        Customer storedCustomer = customersMap.get(normalizeEmail(mail));
        if (storedCustomer == null) {
            throw new NoResultException("Cliente non trovato con la mail: " + mail);
        }
        return storedCustomer; // Molto più elegante e diretto!
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