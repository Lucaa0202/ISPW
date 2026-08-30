package org.example.project3.dao.demo;

import org.example.project3.dao.TrainerDAO;
import org.example.project3.dao.demo.shared.SharedResources;
import org.example.project3.exceptions.DAOException;
import org.example.project3.exceptions.MailAlreadyExistsException;
import org.example.project3.exceptions.NoResultException;
import org.example.project3.model.Course;
import org.example.project3.model.Credentials;
import org.example.project3.model.Trainer;

import java.util.List;

public class TrainerDAOP implements TrainerDAO {

    @Override
    public boolean emailExists(String mail) {
        if (mail == null) return false;
        return SharedResources.getInstance().getUserTable().containsKey(normalizeEmail(mail));
    }

    private String normalizeEmail(String mail) {
        if (mail == null) return "";
        return mail.trim().toLowerCase();
    }


    @Override
    public boolean insertUser(Credentials credentials) {
        return SharedResources.getInstance().getUserTable().putIfAbsent(credentials.getMail(), credentials) == null;
    }

    @Override
    public void registerTrainer(Trainer trainer) throws MailAlreadyExistsException {
        if (emailExists(trainer.getCredentials().getMail())) {
            throw new MailAlreadyExistsException("Mail già esistente");
        }
        if (insertUser(trainer.getCredentials())) {
            SharedResources.getInstance().getTrainers().put(trainer.getCredentials().getMail(), trainer);
        } else {
            throw new MailAlreadyExistsException("Errore nella registrazione: credenziali già presenti");
        }
    }


    @Override
    public Trainer retrieveTrainerByMail(String mail) throws NoResultException {
        Trainer storedTrainer = SharedResources.getInstance().getTrainers().get(mail);

        if (storedTrainer == null) {
            throw new NoResultException("Trainer non trovato con la mail: " + mail);
        }

        return storedTrainer;
    }

    @Override
    public void removeTrainer(Trainer trainer) {
        SharedResources.getInstance().getTrainers().remove(trainer.getCredentials().getMail());
        // Buona pratica: ripuliamo anche la tabella globale delle credenziali
        SharedResources.getInstance().getUserTable().remove(trainer.getCredentials().getMail());
    }

    @Override
    public void modifyTrainer(Trainer trainer) {
        SharedResources.getInstance().getTrainers().put(trainer.getCredentials().getMail(), trainer);
    }

    @Override
    public Trainer retrieveTrainerCourse(Course course) throws NoResultException, DAOException {
        Trainer trainer = SharedResources.getInstance().getTrainerCourse().get(course.getCourseName());

        if (trainer != null) {
            return trainer;
        } else {
            throw new NoResultException("Nessun trainer trovato per il corso: " + course.getCourseName());
        }
    }


    @Override
    public List<String> retrieveSpecialization(Course course) throws DAOException {
        try {
            Trainer trainer = retrieveTrainerCourse(course);
            return trainer.getSpecializations();
        } catch (NoResultException e) {
            throw new DAOException("Impossibile recuperare le specializzazioni, trainer non trovato", e);
        }
    }
}
