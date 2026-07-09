package org.example.project3.dao;


import org.example.project3.exceptions.DAOException;
import org.example.project3.exceptions.NoResultException;
import org.example.project3.model.Request;
import org.example.project3.model.Trainer;

import org.example.project3.model.Reservation;

import java.util.List;

public interface RequestDAO {
    //metodi per la gestione delle richieste
    void sendRequest(Request request)throws DAOException;
    boolean hasAlreadySentRequest(Request request) throws DAOException;
    void deleteRequest(Request request) throws DAOException;
    void retrieveRequests(Trainer trainer,List<Request> requests) throws DAOException, NoResultException;

    void retrieveCourseRequest(Trainer trainer, List<Reservation> reservationList);
    void removeCourseRequest(Reservation reservation);
    void addCourseRequest(Reservation reservation);
    boolean alreadyHasReservation(Reservation reservation);
}
