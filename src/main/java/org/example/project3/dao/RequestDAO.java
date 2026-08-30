package org.example.project3.dao;

import org.example.project3.exceptions.DAOException;
import org.example.project3.exceptions.NoResultException;
import org.example.project3.model.Request;
import org.example.project3.model.Trainer;
import org.example.project3.model.Reservation;

import java.util.List;

public interface RequestDAO {

    void sendRequest(Request request) throws DAOException;
    boolean hasAlreadySentRequest(Request request) throws DAOException;
    void deleteRequest(Request request) throws DAOException;
    void removeCourseRequest(Reservation reservation);
    void addCourseRequest(Reservation reservation);
    boolean alreadyHasReservation(Reservation reservation);


    List<Request> retrieveRequests(Trainer trainer) throws DAOException, NoResultException;
    List<Reservation> retrieveCourseRequest(Trainer trainer) throws DAOException;
}
