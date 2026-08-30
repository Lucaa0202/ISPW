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
import org.example.project3.utilities.enums.Role; // Aggiunto import per il Role

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class ScheduleDAOCSV implements ScheduleDAO {

    private static final String FILE_PATH = "src/main/resources/data/schedules.csv";

    private static final String[] COLUMNS = {
            "id", "name", "customerMail", "trainerMail"
    };
    private static final String CSV_HEADER = String.join(",", COLUMNS);

    private final List<Schedule> schedulesList = new ArrayList<>();



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
            String header = reader.readLine();
            if (header == null) {
                return;
            }
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
        } catch (Exception _) {
            throw new DAOException("Errore durante il salvataggio del CSV");
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


            Credentials credC = new Credentials(customerMail, Role.CLIENT);
            Customer dummyCustomer = new Customer(credC);

            Credentials credT = new Credentials(trainerMail, Role.TRAINER);
            Trainer dummyTrainer = new Trainer(credT);

            return new Schedule(id, name, dummyCustomer, dummyTrainer);

        } catch (Exception _) {
            return null;
        }
    }

    private String formatSchedule(Schedule schedule) {
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



    @Override
    public void addSchedule(Schedule schedule) throws DAOException {
        if (schedule == null) throw new DAOException("Scheda non valida: null");
        ensureLoaded();

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
    public List<Schedule> retrieveSchedule(Customer customer) throws NoResultException, DAOException {
        if (customer == null) throw new DAOException("Utente non valido: null");
        ensureLoaded();

        List<Schedule> result = new ArrayList<>();
        String searchMail = customer.getCredentials().getMail();

        for (Schedule s : schedulesList) {
            if (s.getCustomer().getCredentials().getMail().equals(searchMail)) {
                result.add(s);
            }
        }

        if (result.isEmpty()) {
            throw new NoResultException("Nessuna scheda trovata per " + searchMail);
        }
        return result;
    }


    @Override
    public Schedule retrieveScheduleById(long id) throws DAOException, NoResultException {
        ensureLoaded();

        for (Schedule s : schedulesList) {
            if (s.getId() == id) {
                return s;
            }
        }

        throw new NoResultException("Nessuna scheda trovata con questo ID: " + id);
    }

    // 3. Corretto: Ritorna List<Schedule>
    @Override
    public List<Schedule> searchSchedules(String search, Customer user) throws DAOException, NoResultException {
        if (search == null || user == null) throw new DAOException("Parametri non validi: search o user null");
        ensureLoaded();

        List<Schedule> result = new ArrayList<>();
        String lowerSearch = search.toLowerCase().trim();
        Long searchId = null;

        try {
            searchId = Long.parseLong(lowerSearch);
        } catch(NumberFormatException _) {
            // Ignoriamo l'eccezione intenzionalmente:
            // Se la stringa cercata non è un numero (es. l'utente ha cercato un nome),
            // la conversione fallisce, ma vogliamo che il programma prosegua
            // per effettuare la ricerca testuale.
        }

        String userMail = user.getCredentials().getMail().toLowerCase();

        for (Schedule schedule : schedulesList) {
            boolean matchId = (searchId != null && schedule.getId() == searchId);
            String targetCustomerMail = schedule.getCustomer().getCredentials().getMail().toLowerCase();
            String targetTrainerMail = schedule.getTrainer().getCredentials().getMail().toLowerCase();

            if (targetCustomerMail.contains(userMail) &&
                    (targetTrainerMail.contains(lowerSearch) || matchId || schedule.getName().toLowerCase().contains(lowerSearch))) {
                result.add(schedule);
            }
        }

        if (result.isEmpty()) {
            throw new NoResultException("Nessuna scheda trovata per: " + search);
        }
        return result;
    }


    @Override
    public Trainer retrieveTrainer(Schedule schedule) throws NoResultException, DAOException {
        ensureLoaded();

        if (schedule == null || schedule.getTrainer() == null) {
            throw new NoResultException("Nessun trainer trovato per la scheda con id: " + (schedule != null ? schedule.getId() : "null"));
        }

        return schedule.getTrainer();
    }

    @Override
    public void updateSchedule(Request request, Exercise exercise) throws DAOException {
        ensureLoaded();

    }
}