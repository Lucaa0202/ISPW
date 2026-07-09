package org.example.project3.dao.demo;

import org.example.project3.dao.ExerciseDAO;
import org.example.project3.dao.demo.shared.SharedResources;
import org.example.project3.exceptions.DAOException;
import org.example.project3.exceptions.NoResultException;

import org.example.project3.model.Exercise;
import org.example.project3.model.Request;
import org.example.project3.model.Schedule;
import org.example.project3.utilities.others.Printer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExerciseDAOP implements ExerciseDAO {
    @Override
    public void addExerciseSchedule(Schedule schedule, Exercise exercise) throws DAOException{
        if (schedule == null || exercise == null) {
            throw new DAOException("Parametri non validi: schedule o exercise null");
        }
        Schedule storedSchedule = SharedResources.getInstance().getSchedules().get(schedule.getId());
        if (storedSchedule == null) {
            throw new DAOException("Scheda non trovata con ID: " + schedule.getId());
        }
        schedule.addExercise(exercise);
        SharedResources.getInstance().getSchedules().put(schedule.getId(), schedule);
    }

    @Override
    public void addExercise(Exercise exercise) throws DAOException {
        if (exercise == null) {
            throw new DAOException("Esercizio non valido: null");
        }
        if (SharedResources.getInstance().getExercises().containsKey(exercise.getId())) {
            throw new DAOException("Esercizio con ID " + exercise.getId() + " già esistente");
        }
        SharedResources.getInstance().getExercises().putIfAbsent(exercise.getId(), exercise);
    }

    @Override
    public void deleteExercise(Exercise exercise) throws DAOException {
        if (exercise == null ){
            throw new DAOException("Errore nel DAO");
        }
        SharedResources.getInstance().getExercises().remove(exercise.getId());
    }

    @Override
    public void retrieveExercise(Exercise exercise) throws NoResultException,DAOException {
        if (exercise == null) {
            throw new DAOException("Esercizio non valido: null");
        }
        Exercise storedExercise = SharedResources.getInstance().getExercises().get(exercise.getId());
        if (storedExercise == null) {
            throw new NoResultException(exercise.getClass().getSimpleName() + " non trovato");
        }
        exercise.setName(storedExercise.getName());
        exercise.setDescription(storedExercise.getDescription());
        exercise.setNumberSeries(storedExercise.getNumberSeries());
        exercise.setNumberReps(storedExercise.getNumberReps());
        exercise.setRestTime(storedExercise.getRestTime());
    }

    @Override
    public void retrieveAllExercises(Request request, List<Exercise> exercises) throws NoResultException,DAOException {
        if (exercises == null) {
            throw new DAOException("La lista passata come parametro è null");
        }

        Map<?, Exercise> exercisesMap = SharedResources.getInstance().getExercises();
        if (exercisesMap == null) {
            throw new DAOException("Errore nel recupero della mappa degli esercizi");
        }

        List<Exercise> scheduleExercises = SharedResources.getInstance().getExerciseSchedules().get(request.getSchedule().getId());
        if (scheduleExercises == null) {
            scheduleExercises = new ArrayList<>(); // Inizializza come vuota se null
        }

        List<Exercise> retrievedExercises = new ArrayList<>();
        for (Exercise exercise : exercisesMap.values()) {
            boolean isInSchedule = scheduleExercises.stream()
                    .anyMatch(se -> se.getId() == exercise.getId());
            if (!isInSchedule) {
                retrievedExercises.add(exercise);
            }
        }
        if (retrievedExercises.isEmpty()) {
            throw new NoResultException("Nessun esercizio trovato.");
        }

        exercises.clear(); // Pulisci la lista esistente
        exercises.addAll(retrievedExercises);
    }

    @Override
    public void searchExercises(List<Exercise> exercises, String search, Schedule schedule) throws DAOException ,NoResultException{
        if (search == null || schedule == null) {
            throw new DAOException("Parametri non validi: search o schedule null");
        }
        String lowerSearch = search.toLowerCase();

        Schedule storedSchedule = SharedResources.getInstance().getSchedules().get(schedule.getId());
        if (storedSchedule == null) {
            throw new DAOException(schedule.getClass().getSimpleName() + " non trovato");
        }
        schedule.setExercises(storedSchedule.getExercises());

        Long id=null;
        for (Exercise exercise : schedule.getExercises()) {
            try{
                id= Long.parseLong(lowerSearch);
            }catch(NumberFormatException _){
                Printer.errorPrint("");
            }
            boolean match= (id!=null&&exercise.getId()==id);
            if (exercise.getName().toLowerCase().contains(lowerSearch)||match) {
                exercises.add(exercise);
            }
        }

        if (exercises.isEmpty()) {
            throw new NoResultException("Nessun esercizio trovato per: " + search);
        }
    }

    @Override
    public void searchAllExercises(List<Exercise> exercises, String search) throws DAOException ,NoResultException{
        if (search == null) {
            throw new DAOException("Parametri non validi: search o schedule null");
        }
        String lowerSearch = search.toLowerCase();

        List<Exercise> storedExercises = new ArrayList<>(SharedResources.getInstance().getExercises().values());
        if (storedExercises == null) {
            throw new DAOException("Errore nel recupero degli esercizi nel DAO");
        }

        Long id=null;
        for (Exercise exercise : storedExercises) {
            try{
                id= Long.parseLong(lowerSearch);
            }catch(NumberFormatException _){Printer.errorPrint("");}
            boolean match= (id!=null&&exercise.getId()==id);
            if (exercise.getName().toLowerCase().contains(lowerSearch)||match) {
                exercises.add(exercise);
            }
        }
        if (exercises.isEmpty()) {
            throw new NoResultException("Nessun esercizio trovato per: " + search);
        }
    }

    @Override
    public void updateExercise(Exercise exercise) throws DAOException {
        if (exercise == null) {
            throw new DAOException("Esercizio non valido: null");
        }
        if (!SharedResources.getInstance().getExercises().containsKey(exercise.getId())) {
            throw new DAOException("Esercizio non trovato con ID: " + exercise.getId());
        }
        SharedResources.getInstance().getExercises().put(exercise.getId(), exercise);
    }
}
