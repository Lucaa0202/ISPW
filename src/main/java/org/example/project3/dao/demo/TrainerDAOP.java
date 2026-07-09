package org.example.project3.dao.demo;

import org.example.project3.dao.TrainerDAO;
import org.example.project3.dao.demo.shared.SharedResources;
import org.example.project3.exceptions.LoginAndRegistrationException;
import org.example.project3.exceptions.MailAlreadyExistsException;
import org.example.project3.exceptions.NoResultException;
import org.example.project3.model.Course;
import org.example.project3.model.Credentials;

import org.example.project3.model.Trainer;

import java.sql.SQLException;
import java.util.List;

public class TrainerDAOP implements TrainerDAO {
    public boolean emailExists(String mail) {
        return SharedResources.getInstance().getUserTable().containsKey(normalizeEmail(mail));
    }

    private String normalizeEmail(String mail) {
        return mail.trim().toLowerCase();
    }

    //inserisco l'utente (credenziali) nel database
    public boolean insertUser(Credentials credentials) {
        return SharedResources.getInstance().getUserTable().putIfAbsent(credentials.getMail(), credentials) == null;
    }

    public void registerTrainer(Trainer trainer) throws MailAlreadyExistsException, LoginAndRegistrationException {
        if (emailExists(trainer.getCredentials().getMail())) {
            throw new MailAlreadyExistsException("Mail già esistente");
        }
        if (insertUser(trainer.getCredentials())) {
            SharedResources.getInstance().getTrainers().put(trainer.getCredentials().getMail(), trainer);
        } else {
            throw new LoginAndRegistrationException("Errore nella registrazione");
        }

    }

    @Override
    public void retrieveTrainer(Trainer trainer) throws NoResultException {
        Trainer storedTrainer = SharedResources.getInstance().getTrainers().get(trainer.getCredentials().getMail());
        if (storedTrainer == null) {
            throw new NoResultException(trainer.getClass().getSimpleName() + " non trovato");
        }
        trainer.setName(storedTrainer.getName());
        trainer.setSurname(storedTrainer.getSurname());
        trainer.setGender(storedTrainer.getGender());
        trainer.setOnline(storedTrainer.isOnline());
        trainer.setBirthday(storedTrainer.getBirthday());
    }

    @Override
    public void removeTrainer(Trainer trainer) {
        SharedResources.getInstance().getTrainers().remove(trainer.getCredentials().getMail());
    }

    @Override
    public void modifyTrainer(Trainer trainer) {
        SharedResources.getInstance().getTrainers().put(trainer.getCredentials().getMail(), trainer);
    }

    @Override
    public Trainer retrieveTrainerCourse(Course course) {

        Trainer trainer = SharedResources.getInstance().getTrainerCourse().get(course.getCourseName());
        if (trainer != null) {
            return trainer;
        } else {
            throw new NoResultException();
        }
    }

    @Override
    public List<String> retrieveSpecialization(Course course) throws SQLException {
        Trainer trainer = retrieveTrainerCourse(course);
        return trainer.getSpecializations();
    }
}
