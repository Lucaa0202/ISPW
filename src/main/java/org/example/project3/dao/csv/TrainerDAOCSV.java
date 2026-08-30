package org.example.project3.dao.csv;

import org.example.project3.dao.TrainerDAO;
import org.example.project3.exceptions.DAOException;
import org.example.project3.exceptions.LoginAndRegistrationException;
import org.example.project3.exceptions.MailAlreadyExistsException;
import org.example.project3.exceptions.NoResultException;
import org.example.project3.model.Course;
import org.example.project3.model.Credentials;
import org.example.project3.model.Trainer;
import org.example.project3.utilities.enums.Role; // Importante: aggiunto per il Role

import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrainerDAOCSV implements TrainerDAO {

    private static final String FILE_PATH = "src/main/resources/data/trainers.csv";
    private static final String CSV_HEADER = "mail,name,surname,gender,isOnline,birthday";
    private static final Logger LOGGER = Logger.getLogger(TrainerDAOCSV.class.getName());

    private final Map<String, Trainer> trainersMap = new HashMap<>();

    private void ensureLoaded() {
        if (trainersMap.isEmpty()) {
            loadTrainers();
        }
    }

    private void loadTrainers() {
        trainersMap.clear();
        Path path = Paths.get(FILE_PATH);
        if (!Files.exists(path)) return;

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String header = reader.readLine();
            if (header == null) {
                return;
            }
            String line;
            while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
                String[] fields = line.split(",", -1);
                if (fields.length >= 6) {
                    String mail = fields[0];
                    // FIX: Inserito Role.TRAINER come nel SQL
                    Credentials cred = new Credentials(mail, Role.TRAINER);

                    Trainer trainer = new Trainer(cred);
                    trainer.setName(fields[1]);
                    trainer.setSurname(fields[2]);
                    trainer.setGender(fields[3]);
                    trainer.setOnline(Boolean.parseBoolean(fields[4]));

                    if (!fields[5].isEmpty()) {
                        trainer.setBirthday(LocalDate.parse(fields[5]));
                    }

                    trainersMap.put(normalizeEmail(mail), trainer);
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Errore caricamento trainers.csv", e);
        }
    }

    private void saveTrainers() {
        Path path = Paths.get(FILE_PATH);
        try {
            Files.createDirectories(path.getParent());
            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                writer.write(CSV_HEADER);
                writer.newLine();
                for (Trainer t : trainersMap.values()) {
                    String bday = (t.getBirthday() != null) ? t.getBirthday().toString() : "";

                    String line = String.join(",",
                            t.getCredentials().getMail(), t.getName(), t.getSurname(),
                            t.getGender(), String.valueOf(t.isOnline()), bday
                    );
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Errore salvataggio trainers.csv", e);
        }
    }

    private String normalizeEmail(String mail) {
        return mail.trim().toLowerCase();
    }



    @Override
    public boolean emailExists(String mail) {
        ensureLoaded();
        return trainersMap.containsKey(normalizeEmail(mail));
    }

    @Override
    public boolean insertUser(Credentials credentials) {
        return true;
    }

    @Override
    public void registerTrainer(Trainer trainer) throws MailAlreadyExistsException, LoginAndRegistrationException {
        ensureLoaded();
        if (emailExists(trainer.getCredentials().getMail())) {
            throw new MailAlreadyExistsException("Mail già esistente");
        }
        trainersMap.put(normalizeEmail(trainer.getCredentials().getMail()), trainer);
        saveTrainers();
    }


    @Override
    public Trainer retrieveTrainerByMail(String mail) throws NoResultException {
        ensureLoaded();
        Trainer storedTrainer = trainersMap.get(normalizeEmail(mail));

        if (storedTrainer == null) {
            throw new NoResultException("Trainer non trovato con la mail: " + mail);
        }

        return storedTrainer;
    }

    @Override
    public void removeTrainer(Trainer trainer) {
        ensureLoaded();
        trainersMap.remove(normalizeEmail(trainer.getCredentials().getMail()));
        saveTrainers();
    }

    @Override
    public void modifyTrainer(Trainer trainer) {
        ensureLoaded();
        trainersMap.put(normalizeEmail(trainer.getCredentials().getMail()), trainer);
        saveTrainers();
    }



    @Override
    public List<String> retrieveSpecialization(Course course) throws DAOException {
        throw new UnsupportedOperationException("Metodo non implementato nella versione CSV");
    }

    @Override
    public Trainer retrieveTrainerCourse(Course course) throws DAOException, NoResultException {
        throw new UnsupportedOperationException("Metodo non implementato nella versione CSV");
    }
}
