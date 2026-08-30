package org.example.project3.dao.demo;

import org.example.project3.dao.ExerciseDAO;
import org.example.project3.dao.demo.shared.SharedResources;
import org.example.project3.exceptions.DAOException;
import org.example.project3.exceptions.NoResultException;

import org.example.project3.model.Exercise;
import org.example.project3.model.Request;
import org.example.project3.model.Schedule;

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

        SharedResources.getInstance().getExerciseSchedules()
                .computeIfAbsent(schedule.getId(), k -> new ArrayList<>()).add(exercise);
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
    public Exercise retrieveExerciseById(long id) throws NoResultException, DAOException {
        Exercise storedExercise = SharedResources.getInstance().getExercises().get(id);
        if (storedExercise == null) {
            throw new NoResultException("Esercizio non trovato con ID: " + id);
        }
        return storedExercise;
    }


    @Override
    public List<Exercise> retrieveAllExercises(Request request) throws NoResultException, DAOException {
        if (request == null || request.getSchedule() == null) {
            throw new DAOException("Richiesta non valida");
        }

        Map<?, Exercise> exercisesMap = SharedResources.getInstance().getExercises();
        if (exercisesMap == null) {
            throw new DAOException("Errore nel recupero della mappa degli esercizi");
        }

        List<Exercise> scheduleExercises = SharedResources.getInstance().getExerciseSchedules().get(request.getSchedule().getId());
        if (scheduleExercises == null) {
            scheduleExercises = new ArrayList<>();
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

        return retrievedExercises;
    }

    // ==========================================
    // METODO CORRETTO: Restituisce List<Exercise>
    // ==========================================
    @Override
    public List<Exercise> searchExercises(String search, Schedule schedule) throws DAOException, NoResultException {
        if (search == null || schedule == null) {
            throw new DAOException("Parametri non validi: search o schedule null");
        }

        String lowerSearch = search.toLowerCase().trim();
        List<Exercise> scheduleExercises = SharedResources.getInstance().getExerciseSchedules().get(schedule.getId());

        if (scheduleExercises == null || scheduleExercises.isEmpty()) {
            throw new NoResultException("Nessun esercizio presente in questa scheda.");
        }

        List<Exercise> result = new ArrayList<>();
        Long id = null;
        try {
            id = Long.parseLong(lowerSearch);
        } catch (NumberFormatException _) {
            // Ignoriamo l'eccezione intenzionalmente:
            // Se la stringa cercata non è un numero (es. l'utente ha cercato un nome),
            // la conversione fallisce, ma vogliamo che il programma prosegua
            // per effettuare la ricerca testuale.
        }

        for (Exercise exercise : scheduleExercises) {
            boolean match = (id != null && exercise.getId() == id);
            if (lowerSearch.isEmpty() || exercise.getName().toLowerCase().contains(lowerSearch) || match) {
                result.add(exercise);
            }
        }

        if (result.isEmpty()) {
            throw new NoResultException("Nessun esercizio trovato per: " + search);
        }

        return result;
    }


    @Override
    public List<Exercise> searchAllExercises(String search) throws DAOException, NoResultException {
        if (search == null) {
            throw new DAOException("Parametro search non valido");
        }
        String lowerSearch = search.toLowerCase().trim();

        List<Exercise> storedExercises = new ArrayList<>(SharedResources.getInstance().getExercises().values());
        if (storedExercises.isEmpty()) {
            throw new DAOException("Errore nel recupero degli esercizi nel DAO");
        }

        List<Exercise> result = new ArrayList<>();
        Long id = null;
        try {
            id = Long.parseLong(lowerSearch);
        } catch(NumberFormatException _) {
            // Ignoriamo l'eccezione intenzionalmente:
            // Se la stringa cercata non è un numero (es. l'utente ha cercato un nome),
            // la conversione fallisce, ma vogliamo che il programma prosegua
            // per effettuare la ricerca testuale.
        }

        for (Exercise exercise : storedExercises) {
            boolean match = (id != null && exercise.getId() == id);
            if (lowerSearch.isEmpty() || exercise.getName().toLowerCase().contains(lowerSearch) || match) {
                result.add(exercise);
            }
        }

        if (result.isEmpty()) {
            throw new NoResultException("Nessun esercizio trovato per: " + search);
        }

        return result;
    }


    @Override
    public List<Exercise> retrieveExercises(Schedule schedule) throws DAOException, NoResultException {
        if (schedule == null) throw new DAOException("Scheda non valida");
        // Sfruttiamo il metodo search passando una stringa vuota!
        return searchExercises("", schedule);
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
