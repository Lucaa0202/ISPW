package org.example.project3.dao;

import org.example.project3.exceptions.LoginAndRegistrationException;
import org.example.project3.exceptions.MailAlreadyExistsException;
import org.example.project3.exceptions.NoResultException;
import org.example.project3.model.Credentials;
import org.example.project3.model.Trainer;
import org.example.project3.model.Course;

import java.sql.SQLException;
import java.util.List;

public abstract interface TrainerDAO {
    boolean emailExists(String email);
    boolean insertUser(Credentials credentials);
    void registerTrainer(Trainer trainer) throws MailAlreadyExistsException, LoginAndRegistrationException;
    void retrieveTrainer(Trainer trainer) throws NoResultException;
    default void removeTrainer(Trainer trainer){
        throw new UnsupportedOperationException("Rimozione del trainer non supportata da questa implementazione.");
    }
    default void modifyTrainer(Trainer trainer){
        throw new UnsupportedOperationException("Modifica del trainer non supportata da questa implementazione.");
    }
    default Trainer retrieveTrainerCourse(Course course){
        throw new UnsupportedOperationException("Recupero corsi non supportata da questa implementazione.");
    }
    default List<String> retrieveSpecialization(Course course) throws SQLException{
        throw new UnsupportedOperationException("Recupero specializzazioni non supportata da questa implementazione.");
    }


}