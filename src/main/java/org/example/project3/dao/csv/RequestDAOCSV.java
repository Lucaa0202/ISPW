package org.example.project3.dao.csv;

import org.example.project3.dao.RequestDAO;
import org.example.project3.exceptions.DAOException;
import org.example.project3.exceptions.NoResultException;
import org.example.project3.model.Credentials;
import org.example.project3.model.Customer;
import org.example.project3.model.Exercise;
import org.example.project3.model.Request;
import org.example.project3.model.Reservation;
import org.example.project3.model.Schedule;
import org.example.project3.model.Trainer;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class RequestDAOCSV implements RequestDAO {

    private static final String FILE_PATH = "src/main/resources/data/requests.csv";

    // Oltre agli ID, salviamo le mail di customer e trainer per facilitare i controlli (come facevi in SharedResources)
    private static final String[] COLUMNS = {
            "id", "scheduleId", "exerciseId", "reason", "dateTime", "customerMail", "trainerMail"
    };
    private static final String CSV_HEADER = String.join(",", COLUMNS);

    private final List<Request> requestsList = new ArrayList<>();

    // ==========================================
    // METODI PRIVATI DI LETTURA E SCRITTURA CSV
    // ==========================================

    private void ensureLoaded() throws DAOException {
        if (requestsList.isEmpty()) {
            loadRequests();
        }
    }

    private void loadRequests() throws DAOException {
        requestsList.clear();
        Path path = Paths.get(FILE_PATH);

        if (!Files.exists(path)) return;

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String header = reader.readLine();
            if (header == null) return;
            String line;
            while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
                Request request = parseRequest(line);
                if (request != null) {
                    requestsList.add(request);
                }
            }
        } catch (IOException e) {
            throw new DAOException("Errore durante la lettura del CSV", e);
        }
    }

    private void saveRequests() throws DAOException {
        Path path = Paths.get(FILE_PATH);
        try {
            Files.createDirectories(path.getParent());
            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                writer.write(CSV_HEADER);
                writer.newLine();
                for (Request r : requestsList) {
                    writer.write(formatRequest(r));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            throw new DAOException("Errore durante il salvataggio del CSV", e);
        }
    }

    private Request parseRequest(String line) {
        String[] fields = line.split(",", -1); // -1 per tenere i campi vuoti
        if (fields.length < COLUMNS.length) return null;

        try {
            long id = Long.parseLong(fields[0]);
            long scheduleId = Long.parseLong(fields[1]);

            // L'esercizio potrebbe essere nullo se la richiesta riguarda tutta la scheda
            Exercise dummyExercise = null;
            if (!fields[2].isEmpty()) {
                long exerciseId = Long.parseLong(fields[2]);
                // Usiamo il costruttore a 2 parametri passandogli l'id e una stringa vuota per il nome
                dummyExercise = new Exercise(exerciseId, "");
            }

            String reason = fields[3];
            LocalDateTime dateTime = fields[4].isEmpty() ? null : LocalDateTime.parse(fields[4]);

            String customerMail = fields[5];
            String trainerMail = fields[6];

            // Ricostruiamo la gerarchia fantoccio
            Customer dummyCustomer = new Customer(new Credentials(customerMail, null));
            Trainer dummyTrainer = new Trainer(new Credentials(trainerMail, null));

            Schedule dummySchedule = new Schedule(scheduleId, "", dummyCustomer, dummyTrainer);

            return new Request(id, dummySchedule, dummyExercise, reason, dateTime);

        } catch (Exception _) {
            return null;
        }
    }

    private String formatRequest(Request r) {
        String scheduleId = (r.getSchedule() != null) ? String.valueOf(r.getSchedule().getId()) : "";

        // ⚠️ ASSICURATI DI AVERE UN METODO GET PER L'ID DELL'ESERCIZIO (es. getId())
        // Usiamo il metodo getId() che è regolarmente presente nel tuo Model!
        String exerciseId = (r.getExercise() != null) ? String.valueOf(r.getExercise().getId()) : "";

        String reason = (r.getReason() != null) ? r.getReason().replace(",", ";") : ""; // Evitiamo che le virgole rompano il CSV
        String dt = (r.getDateTime() != null) ? r.getDateTime().toString() : "";

        String customerMail = (r.getSchedule() != null && r.getSchedule().getCustomer() != null)
                ? r.getSchedule().getCustomer().getCredentials().getMail() : "";

        String trainerMail = (r.getSchedule() != null && r.getSchedule().getTrainer() != null)
                ? r.getSchedule().getTrainer().getCredentials().getMail() : "";

        return String.join(",", String.valueOf(r.getID()), scheduleId, exerciseId, reason, dt, customerMail, trainerMail);
    }


    // ==========================================
    // METODI DELL'INTERFACCIA REQUEST DAO
    // ==========================================

    @Override
    public void sendRequest(Request request) throws DAOException {
        if (request == null) throw new DAOException("Richiesta non valida: null");
        ensureLoaded();

        // Genera un ID basato sui nanosecondi, esattamente come nel tuo DemoDAO
        long newId = LocalDateTime.now(ZoneId.systemDefault()).getNano();
        request.setId(newId);

        // Controllo duplicati per ID scheda
        for (Request req : requestsList) {
            if (req.getSchedule().getId() == request.getSchedule().getId()) {
                throw new DAOException("Richiesta con id scheda " + request.getSchedule().getId() + " esiste già");
            }
        }

        requestsList.add(request);
        saveRequests();
    }

    @Override
    public boolean hasAlreadySentRequest(Request request) throws DAOException {
        if (request == null) throw new DAOException("Richiesta non valida: null");
        ensureLoaded();

        long scheduleId = request.getSchedule().getId();
        String targetCustomerMail = request.getSchedule().getCustomer().getCredentials().getMail();
        String targetTrainerMail = request.getSchedule().getTrainer().getCredentials().getMail();

        for (Request req : requestsList) {
            if (req.getSchedule().getId() == scheduleId) {
                String existingCustomerMail = req.getSchedule().getCustomer().getCredentials().getMail();
                String existingTrainerMail = req.getSchedule().getTrainer().getCredentials().getMail();

                if (existingCustomerMail.equals(targetCustomerMail) && existingTrainerMail.equals(targetTrainerMail)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void deleteRequest(Request request) throws DAOException {
        if (request == null) throw new DAOException("Errore nel DAO");
        ensureLoaded();

        // Rimuove la richiesta controllando l'ID
        requestsList.removeIf(req -> req.getID() == request.getID());
        saveRequests();
    }

    @Override
    public void retrieveRequests(Trainer trainer, List<Request> requests) throws DAOException, NoResultException {
        if (trainer == null) throw new DAOException("Trainer non valido: null");
        ensureLoaded();

        boolean found = false;
        String trainerMail = trainer.getCredentials().getMail();

        for (Request req : requestsList) {
            if (req.getSchedule().getTrainer().getCredentials().getMail().equals(trainerMail)) {
                requests.add(req);
                found = true;
            }
        }

        if (!found) {
            throw new NoResultException("Nessuna richiesta trovata per: " + trainerMail);
        }
    }

    // ==========================================
    // METODI RESERVATION (Da implementare a parte)
    // ==========================================

    // ==========================================
    // METODI RESERVATION (Non supportati in CSV)
    // ==========================================

    @Override
    public void retrieveCourseRequest(Trainer trainer, List<Reservation> reservationList) {
        throw new UnsupportedOperationException("Gestione prenotazioni corsi non implementata in CSV");
    }

    @Override
    public void removeCourseRequest(Reservation reservation) {
        throw new UnsupportedOperationException("Gestione prenotazioni corsi non implementata in CSV");
    }

    @Override
    public void addCourseRequest(Reservation reservation) {
        throw new UnsupportedOperationException("Gestione prenotazioni corsi non implementata in CSV");
    }

    @Override
    public boolean alreadyHasReservation(Reservation reservation) {
        throw new UnsupportedOperationException("Gestione prenotazioni corsi non implementata in CSV");
    }
}
