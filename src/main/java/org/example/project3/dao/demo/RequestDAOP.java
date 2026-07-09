package org.example.project3.dao.demo;

import org.example.project3.dao.RequestDAO;
import org.example.project3.dao.demo.shared.SharedResources;

import org.example.project3.exceptions.DAOException;
import org.example.project3.exceptions.NoResultException;
import org.example.project3.model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RequestDAOP implements RequestDAO {
    @Override
    public void sendRequest(Request request) throws DAOException {
        if(request==null){
            throw new DAOException("Richiesta non valida: null");
        }else{
            long id = LocalDateTime.now().getNano();
            request.setId(id);
        }
        if(SharedResources.getInstance().getRequestsSent().containsKey(request.getSchedule().getId())){
            throw new DAOException("Richiesta con id " + request.getSchedule().getId() + " esiste già");
        }
        SharedResources.getInstance().getRequestsSent()
                .computeIfAbsent(request.getSchedule().getId(), k -> new ArrayList<>()).add(request);
        SharedResources.getInstance().getRequestTrainer()
                .computeIfAbsent(request.getSchedule().getTrainer().getCredentials().getMail(), k -> new ArrayList<>()).add(request);
    }

    @Override
    public boolean hasAlreadySentRequest(Request request) throws DAOException {
        if(request==null){
            throw new  DAOException("Richiesta non valida: null");
        }
        Long scheduleId = request.getSchedule().getId();

        // Controlla se esiste una lista di richieste per questo Id
        if (SharedResources.getInstance().getRequestsSent().containsKey(scheduleId)) {
            // Itera sulla lista di richieste associate a questo Id
            for (Request existingRequest : SharedResources.getInstance().getRequestsSent().get(scheduleId)) {
                // Confronta le email del cliente e del trainer per verificare se la richiesta è la stessa
                if (existingRequest.getSchedule().getCustomer().getCredentials().getMail().equals(request.getSchedule().getCustomer().getCredentials().getMail()) &&
                        existingRequest.getSchedule().getTrainer().getCredentials().getMail().equals(request.getSchedule().getTrainer().getCredentials().getMail())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void deleteRequest(Request request) throws DAOException {
        if(request==null){
            throw new DAOException("Errore nel DAO");
        }
        SharedResources.getInstance().getRequestsSent().remove(request.getSchedule().getId());

        List<Request> trainerRequests = SharedResources.getInstance().getRequestTrainer().get(request.getSchedule().getTrainer().getCredentials().getMail());

        if(trainerRequests != null) {
            trainerRequests.removeIf(req -> req.getID()==request.getID());
        }
    }

    @Override
    public void retrieveRequests(Trainer trainer, List<Request> requests)throws DAOException, NoResultException{
        if (trainer == null) {
            throw new DAOException("Trainer non valido: null");
        }
        Trainer storedTrainer = SharedResources.getInstance().getTrainers().get(trainer.getCredentials().getMail());
        if (storedTrainer == null) {
            throw new DAOException(trainer.getClass().getSimpleName() + " non trovato");
        }
        List<Request> storedRequests = SharedResources.getInstance().getRequestTrainer().get(storedTrainer.getCredentials().getMail());
        if (storedRequests == null) {
            throw new NoResultException(("Nessuna richiesta trovata per: " + storedTrainer.getName()));
        }
        requests.addAll(storedRequests);

    }


    // chiave per la lista Corso,
    @Override
    public void retrieveCourseRequest(Trainer trainer, List<Reservation> reservationList){
        reservationList.addAll(SharedResources.getInstance().getReservationRequests().get(trainer.getCredentials().getMail()));
    }


    @Override
    public void removeCourseRequest(Reservation reservation){
        Trainer trainer = SharedResources.getInstance().getTrainerCourse().get(reservation.getCourse().getCourseName());
        List<Reservation> reservationsRequests = SharedResources.getInstance().getReservationRequests().get(trainer.getCredentials().getMail());

        reservationsRequests.removeIf(r->
                r.getCourse().equals(reservation.getCourse()) &&
                        r.getCustomer().equals(reservation.getCustomer()) &&
                        r.getDay().equals(reservation.getDay()) &&
                        r.getHour().equals(reservation.getHour())
        );

        //il corso identifica la lista precisa
        SharedResources.getInstance().getReservationRequests().remove(reservation.getCourse().getCourseName());

        SharedResources.getInstance().getReservationRequests().put(reservation.getCourse().getCourseName(), reservationsRequests);
    }


    @Override
    public void addCourseRequest(Reservation reservation){
        Trainer trainer = SharedResources.getInstance().getTrainerCourse().get(reservation.getCourse().getCourseName());

        // Aggiunge la prenotazione alla mappa
        SharedResources.getInstance().getReservationRequests() .computeIfAbsent(trainer.getCredentials().getMail(), k -> new ArrayList<>()).add(reservation);
    }

    @Override
    public boolean alreadyHasReservation(Reservation reservation) {
        Trainer trainer = SharedResources.getInstance().getTrainerCourse().get(reservation.getCourse().getCourseName());

        if (SharedResources.getInstance().getReservationRequests().containsKey(trainer.getCredentials().getMail())) {
            for (Reservation existingReservation : SharedResources.getInstance().getReservationRequests().get(trainer.getCredentials().getMail())) {
                if (existingReservation.getCourse().getCourseName().equals(reservation.getCourse().getCourseName())
                        && existingReservation.getCustomer().getCredentials().getMail().equals(reservation.getCustomer().getCredentials().getMail())
                        && existingReservation.getHour().equals(reservation.getHour())
                        && existingReservation.getDay().equals(reservation.getDay())){
                    return true;
                }
            }
        }
        return false;
    }

}