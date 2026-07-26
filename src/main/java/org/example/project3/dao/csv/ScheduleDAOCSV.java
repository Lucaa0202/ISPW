package org.example.project3.dao.csv;

import org.example.project3.dao.ScheduleDAO;
import org.example.project3.exceptions.DAOException;
import org.example.project3.exceptions.NoResultException;
import org.example.project3.model.Credentials;
import org.example.project3.model.Customer;
import org.example.project3.model.Exercise;
import org.example.project3.model.Request;
import org.example.project3.model.Schedule;
import org.example.project3.model.Trainer;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class ScheduleDAOCSV implements ScheduleDAO {

    private static final String FILE_PATH = "src/main/resources/data/schedules.csv";

    // Le colonne: salviamo l'ID, il nome, e le MAIL di cliente e trainer
    private static final String[] COLUMNS = {
            "id", "name", "customerMail", "trainerMail"
    };
    private static final String CSV_HEADER = String.join(",", COLUMNS);

    // La nostra cache in memoria che sostituisce SharedResources
    private final List<Schedule> schedulesList = new ArrayList<>();

    // ==========================================
    // METODI PRIVATI DI LETTURA E SCRITTURA CSV
    // ==========================================

    private void ensureLoaded() throws DAOException {
        if (schedulesList.isEmpty()) {
            loadSchedules();
        }
    }

    private void loadSchedules() throws DAOException {
        schedulesList.clear();
        Path path = Paths.get(FILE_PATH);

        if (!Files.exists(path)) {
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String header = reader.readLine(); // Salta header
            String line;
            while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
                Schedule schedule = parseSchedule(line);
                if (schedule != null) {
                    schedulesList.add(schedule);
                }
            }
        } catch (IOException e) {
            throw new DAOException("Errore durante la lettura del CSV", e);
        }
    }

    private void saveSchedules() throws DAOException {
        Path path = Paths.get(FILE_PATH);
        try {
            Files.createDirectories(path.getParent());
            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                writer.write(CSV_HEADER);
                writer.newLine();
                for (Schedule s : schedulesList) {
                    writer.write(formatSchedule(s));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            throw new DAOException("Errore durante il salvataggio del CSV", e);
        }
    }

    private Schedule parseSchedule(String line) {
        String[] fields = line.split(",");
        if (fields.length < COLUMNS.length) return null;

        try {
            long id = Long.parseLong(fields[0]);
            String name = fields[1];
            String customerMail = fields[2];
            String trainerMail = fields[3];

            // Creiamo la credenziale passando la mail direttamente nel costruttore.
            // Come "Role" possiamo passare null, o se preferisci Role.CUSTOMER / Role.TRAINER.
            // Ai fini della nostra ricerca, ci basta che la mail sia valorizzata!
            Credentials credC = new Credentials(customerMail, null);
            Customer dummyCustomer = new Customer(credC);

            Credentials credT = new Credentials(trainerMail, null);
            Trainer dummyTrainer = new Trainer(credT);

            // Restituiamo l'oggetto ricomposto
            return new Schedule(id, name, dummyCustomer, dummyTrainer);

        } catch (Exception e) {
            return null; // Se c'è un errore (es. ID non numerico), saltiamo la riga
        }
    }

    private String formatSchedule(Schedule schedule) {
        // Estraiamo in modo sicuro le mail navigando gli oggetti
        String customerMail = (schedule.getCustomer() != null && schedule.getCustomer().getCredentials() != null)
                ? schedule.getCustomer().getCredentials().getMail() : "";

        String trainerMail = (schedule.getTrainer() != null && schedule.getTrainer().getCredentials() != null)
                ? schedule.getTrainer().getCredentials().getMail() : "";

        return String.join(",",
                String.valueOf(schedule.getId()),
                schedule.getName(),
                customerMail,
                trainerMail
        );
    }

    // ==========================================
    // METODI DELL'INTERFACCIA SCHEDULE DAO
    // ==========================================

    @Override
    public void addSchedule(Schedule schedule) throws DAOException {
        if (schedule == null) throw new DAOException("Scheda non valida: null");
        ensureLoaded();

        // Verifica duplicati come nel DemoDAO
        for(Schedule s : schedulesList) {
            if(s.getId() == schedule.getId()) {
                throw new DAOException("Scheda con ID " + schedule.getId() + " già esistente");
            }
        }

        schedulesList.add(schedule);
        saveSchedules();
    }

    @Override
    public void deleteSchedule(Schedule schedule) throws DAOException {
        if (schedule == null) throw new DAOException("Errore nel DAO");
        ensureLoaded();

        schedulesList.removeIf(s -> s.getId() == schedule.getId());
        saveSchedules();
    }

    @Override
    public void retrieveSchedule(Customer customer, List<Schedule> schedules) throws NoResultException, DAOException {
        if (customer == null) throw new DAOException("Utente non valido: null");
        ensureLoaded();

        boolean found = false;
        String searchMail = customer.getCredentials().getMail();

        for (Schedule s : schedulesList) {
            if (s.getCustomer().getCredentials().getMail().equals(searchMail)) {
                schedules.add(s);
                found = true;
            }
        }

        if (!found) {
            throw new NoResultException("Nessuna scheda trovata per " + searchMail);
        }
    }

    @Override
    public void searchSchedules(List<Schedule> schedules, String search, Customer user) throws DAOException, NoResultException {
        if (search == null || user == null) throw new DAOException("Parametri non validi: search o user null");
        ensureLoaded();

        String lowerSearch = search.toLowerCase().trim();
        Long searchId = null;

        try {
            searchId = Long.parseLong(lowerSearch);
        } catch(NumberFormatException ignored) {}

        boolean found = false;
        String userMail = user.getCredentials().getMail().toLowerCase();

        for (Schedule schedule : schedulesList) {
            boolean matchId = (searchId != null && schedule.getId() == searchId);
            String targetCustomerMail = schedule.getCustomer().getCredentials().getMail().toLowerCase();
            String targetTrainerMail = schedule.getTrainer().getCredentials().getMail().toLowerCase();

            // Stessa esatta logica del DemoDAO
            if (targetCustomerMail.contains(userMail) &&
                    (targetTrainerMail.contains(lowerSearch) || matchId || schedule.getName().toLowerCase().contains(lowerSearch))) {
                schedules.add(schedule);
                found = true;
            }
        }

        if (!found) {
            throw new NoResultException("Nessuna scheda trovata per: " + search);
        }
    }
    @Override
    public void retrieveTrainer(Schedule schedule) throws NoResultException, DAOException {
        ensureLoaded();
        // Nel parseSchedule abbiamo già valorizzato il "dummyTrainer" con la sua Mail.
        // Se l'oggetto è presente, non facciamo nulla. Se ti servono anche Nome/Cognome del trainer,
        // dovrai chiamare TrainerDAOCSV.getTrainerByMail(...)
        if (schedule.getTrainer() == null) {
            throw new NoResultException("Nessun trainer trovato per la scheda con id: " + schedule.getId());
        }
    }

    @Override
    public void updateSchedule(Request request, Exercise exercise) throws DAOException {
        ensureLoaded();
        // ATTENZIONE: Nel tuo DemoDAO l'update modifica l'ESERCIZIO dentro la scheda, non la scheda stessa.
        // Poiché i dati degli esercizi non risiedono in schedules.csv, non devi salvare questo file qui.
        // L'operazione vera avverrà nell'ExerciseDAOCSV dove salverai le modifiche sul file degli esercizi.
    }
}