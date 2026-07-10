package org.example.project3.dao.demo;

import org.example.project3.dao.ScheduleDAO;
import org.example.project3.dao.demo.shared.SharedResources;
import org.example.project3.exceptions.DAOException;
import org.example.project3.exceptions.NoResultException;
import org.example.project3.model.*;

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
        List<Schedule> schedulesForCustomer = SharedResources.getInstance().getCustomerSchedules()
                .computeIfAbsent(schedule.getCustomer().getCredentials().getMail(), k -> new ArrayList<>());
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
        if (storedSchedules == null || storedSchedules.isEmpty()) {
            throw new NoResultException("Nessuna scheda trovata per " + customer.getCredentials().getMail());
        }
        schedules.addAll(storedSchedules);
    }



    @Override
    public void retrieveExercises(Schedule schedule) throws NoResultException, DAOException {
        if (schedule == null) {
            throw new DAOException("Scheda non valida: null");
        }

        long targetId = schedule.getId();

        Schedule storedSchedule = SharedResources.getInstance().getSchedules().get(targetId);
        if (storedSchedule == null) {
            throw new DAOException(schedule.getClass().getSimpleName() + " non trovata");
        }

        List<Exercise> storedExercises = SharedResources.getInstance().getExerciseSchedules().get(targetId);

        if (storedExercises == null || storedExercises.isEmpty()) {
            throw new NoResultException("Nessun esercizio trovato per la scheda " + targetId);
        }

        // LO SCUDO ANTIMEMORIA: Creiamo una lista NUOVA e cloniamo gli elementi.
        // In questo modo, se il Controller chiama .clear(), svuota questa copia
        // e non il database centrale di SharedResources!
        List<Exercise> clonedList = new ArrayList<>();
        clonedList.addAll(storedExercises);

        schedule.setExercises(clonedList);
    }

    @Override
    public void searchSchedules(List<Schedule> schedules, String search, Customer user) {
        if(search==null || user == null){
            throw new DAOException("Parametri non validi: search o user null");
        }
        String lowerSearch = search.toLowerCase().trim();
        Long id = null;

        // OTTIMIZZAZIONE SONARCLOUD: Parse eseguito una volta sola, fuori dal ciclo!
        try {
            id = Long.parseLong(lowerSearch);
        } catch(NumberFormatException _) {
            // Ignoriamo silenziosamente se non è un numero
        }

        for (Schedule schedule : SharedResources.getInstance().getSchedules().values()) {
            boolean match = (id != null && schedule.getId() == id);
            if (schedule.getCustomer().getCredentials().getMail().toLowerCase().contains(user.getCredentials().getMail().toLowerCase()) &&
                    (schedule.getTrainer().getCredentials().getMail().toLowerCase().contains(lowerSearch) ||
                            match ||
                            schedule.getName().toLowerCase().contains(lowerSearch))
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

        // OTTIMIZZAZIONE SONARCLOUD: Rimosso il ciclo for inutile. Il trainer c'è già nello storedSchedule!
        schedule.setTrainer(storedSchedule.getTrainer());

        if (schedule.getTrainer() == null) {
            throw new NoResultException("Nessun trainer trovato per la scheda con id: " + schedule.getId());
        }
    }

    @Override
    public void updateSchedule(Request request, Exercise exercise) throws DAOException {
        if (request == null || exercise == null) {
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
                storedExercises.set(i, exercise);
                break;
            }
        }
        // Riaggiorna la mappa
        SharedResources.getInstance().getExerciseSchedules().put(storedSchedule.getId(), storedExercises);
    }
}