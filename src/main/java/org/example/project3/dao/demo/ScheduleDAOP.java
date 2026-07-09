package org.example.project3.dao.demo;

import org.example.project3.dao.ScheduleDAO;
import org.example.project3.dao.demo.shared.SharedResources;
import org.example.project3.exceptions.DAOException;
import org.example.project3.exceptions.NoResultException;
import org.example.project3.model.*;
import org.example.project3.utilities.others.Printer;

import java.util.ArrayList;
import java.util.List;

public class ScheduleDAOP implements ScheduleDAO {
    @Override
    public void addSchedule(Schedule schedule) throws DAOException {
        if (schedule == null) {
            throw new DAOException("Scheda non valida: null");
        }
        if(SharedResources.getInstance().getSchedules().containsKey(schedule.getId())){
            throw new DAOException("Scheda con ID " + schedule.getId() + " già esistente");
        }
        SharedResources.getInstance().getSchedules().putIfAbsent(schedule.getId(), schedule);
        List<Schedule> schedulesForCustomer = SharedResources.getInstance().getCustomerSchedules().computeIfAbsent(schedule.getCustomer().getCredentials().getMail(), k->new ArrayList<>());
        schedulesForCustomer.add(schedule);
    }

    @Override
    public void deleteSchedule(Schedule schedule) throws DAOException {
        if (schedule == null){
            throw new DAOException("Errore nel DAO");
        }
        SharedResources.getInstance().getSchedules().remove(schedule.getId());
    }

    @Override
    public void retrieveSchedule(Customer customer, List<Schedule> schedules) throws NoResultException,DAOException {
        if (customer == null) {
            throw new DAOException("Utente non valido: null");
        }
        List<Schedule> storedSchedules = SharedResources.getInstance().getCustomerSchedules().get(customer.getCredentials().getMail());

        if (storedSchedules == null) {
            throw new NoResultException("Nessuna scheda trovata per " + customer.getCredentials().getMail());
        }

        // Risolto l'errore SonarCloud rimuovendo l'else if ridondante
        schedules.addAll(storedSchedules);
    }

    @Override
    public void retrieveExercises(Schedule schedule) throws NoResultException {
        if (schedule == null) {
            throw new DAOException("Scheda non valida: null");
        }
        if(schedule.getExercises() == null){
            schedule.setExercises(new ArrayList<>());
        }else{
            schedule.getExercises().clear();
        }
        Schedule storedSchedule = SharedResources.getInstance().getSchedules().get(schedule.getId());
        if (storedSchedule == null) {
            throw new DAOException(schedule.getClass().getSimpleName() + " non trovato");
        }
        List<Exercise> storedExercises = SharedResources.getInstance().getExerciseSchedules().get(schedule.getId());
        if (storedExercises == null) {
            throw new NoResultException("Nessun esercizio trovato per: " + storedSchedule.getId());
        }
        for(Exercise exercise : storedExercises){
            schedule.addExercise(exercise);
        }
    }

    @Override
    public void searchSchedules(List<Schedule> schedules, String search, Customer user) {
        if(search==null || user == null){
            throw new DAOException("Parametri non validi: search o user null");
        }
        String lowerSearch = search.toLowerCase();
        Long id =null;
        for (Schedule schedule : SharedResources.getInstance().getSchedules().values()) {
            try{
                id= Long.parseLong(lowerSearch);
            }catch(NumberFormatException _){
                Printer.errorPrint("");}
            boolean match= (id!=null&&schedule.getId()==id);
            if (schedule.getCustomer().getCredentials().getMail().toLowerCase().contains(user.getCredentials().getMail().toLowerCase())&&
                    schedule.getTrainer().getCredentials().getMail().toLowerCase().contains(lowerSearch)||
                    match||
                    schedule.getName().toLowerCase().contains(lowerSearch)
            ) {
                schedules.add(schedule);
            }
        }

        if (schedules.isEmpty()) {
            throw new NoResultException("Nessuna scheda trovata per: " + search);
        }
    }

    @Override
    public void retrieveTrainer(Schedule schedule) throws NoResultException, DAOException {
        if (schedule == null) {
            throw new DAOException("Scheda non valida: null");
        }
        Schedule storedSchedule = SharedResources.getInstance().getSchedules().get(schedule.getId());
        if (storedSchedule == null) {
            throw new DAOException(schedule.getClass().getSimpleName() + " non trovato");
        }
        for (Schedule searchSchedule : SharedResources.getInstance().getSchedules().values()) {
            if (searchSchedule.getId()== schedule.getId()) {
                schedule.setTrainer(searchSchedule.getTrainer());
            }
        }
        if (schedule.getTrainer()==null) {
            throw new NoResultException("Nessun trainer trovato per la scheda con id: " + schedule.getId());
        }
    }

    @Override
    public void updateSchedule(Request request, Exercise exercise) throws DAOException {
        if (request == null||exercise == null) {
            throw new DAOException("Parametri non validi: exercise o schedule null");
        }
        Schedule storedSchedule = SharedResources.getInstance().getSchedules().get(request.getSchedule().getId());
        if (storedSchedule == null) {
            throw new DAOException(request.getSchedule().getClass().getSimpleName() + " non trovato");
        }
        List<Exercise> storedExercises = SharedResources.getInstance().getExerciseSchedules().get(storedSchedule.getId());
        if (storedExercises == null) {
            throw new NoResultException(("Nessun esercizio trovato per: " + storedSchedule.getId()));
        }
        for (int i = 0; i < storedExercises.size(); i++) {
            Exercise foundExercise = storedExercises.get(i);
            if (foundExercise.getId() == request.getExercise().getId()) {
                storedExercises.set(i, exercise); // Sostituisce foundExercise con exercise
                break; // Esce dal ciclo una volta trovata la corrispondenza
            }
        }
        SharedResources.getInstance().getExerciseSchedules().remove(storedSchedule.getId());
        SharedResources.getInstance().getExerciseSchedules().put(storedSchedule.getId(), storedExercises);
    }

}