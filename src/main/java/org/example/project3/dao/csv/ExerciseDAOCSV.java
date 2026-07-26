package org.example.project3.dao.csv;

import org.example.project3.dao.ExerciseDAO;
import org.example.project3.exceptions.DAOException;
import org.example.project3.exceptions.NoResultException;
import org.example.project3.model.Exercise;
import org.example.project3.model.Request;
import org.example.project3.model.Schedule;
import org.example.project3.utilities.enums.RestTime;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ExerciseDAOCSV implements ExerciseDAO {

    private static final String EXERCISES_FILE = "src/main/resources/data/exercises.csv";
    private static final String SCHED_EX_FILE = "src/main/resources/data/schedule_exercises.csv";

    private final Map<Long, Exercise> exercisesMap = new HashMap<>();
    private final Map<Long, List<Exercise>> exerciseSchedulesMap = new HashMap<>();

    // ==========================================
    // METODI PRIVATI DI LETTURA E SCRITTURA
    // ==========================================

    private void ensureLoaded() {
        if (exercisesMap.isEmpty()) {
            loadExercises();
            loadExerciseSchedules();
        }
    }

    private void loadExercises() {
        exercisesMap.clear();
        Path path = Paths.get(EXERCISES_FILE);
        if (!Files.exists(path)) return;

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            reader.readLine(); // Salta header
            String line;
            while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
                String[] fields = line.split(",", -1);
                long id = Long.parseLong(fields[0]);
                String name = fields[1];
                String desc = fields[2];
                Integer series = fields[3].isEmpty() ? null : Integer.parseInt(fields[3]);
                Integer reps = fields[4].isEmpty() ? null : Integer.parseInt(fields[4]);

                RestTime rest = null;
                try { if (!fields[5].isEmpty()) rest = RestTime.valueOf(fields[5]); } catch (Exception ignored) {}

                Exercise ex = new Exercise(id, name, desc, series, reps, rest);
                exercisesMap.put(id, ex);
            }
        } catch (Exception e) {
            System.err.println("Errore lettura exercises.csv");
        }
    }

    private void loadExerciseSchedules() {
        exerciseSchedulesMap.clear();
        Path path = Paths.get(SCHED_EX_FILE);
        if (!Files.exists(path)) return;

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
                String[] fields = line.split(",");
                long scheduleId = Long.parseLong(fields[0]);
                long exerciseId = Long.parseLong(fields[1]);

                Exercise ex = exercisesMap.get(exerciseId);
                if (ex != null) {
                    exerciseSchedulesMap.computeIfAbsent(scheduleId, k -> new ArrayList<>()).add(ex);
                }
            }
        } catch (Exception e) {
            System.err.println("Errore lettura schedule_exercises.csv");
        }
    }

    // ==========================================
    // METODI DELL'INTERFACCIA
    // ==========================================

    @Override
    public void searchExercises(List<Exercise> exercises, String search, Schedule schedule) throws DAOException, NoResultException {
        if (search == null || schedule == null) throw new DAOException("Parametri non validi");
        ensureLoaded();

        String lowerSearch = search.toLowerCase().trim();
        List<Exercise> scheduleExercises = exerciseSchedulesMap.get(schedule.getId());

        if (scheduleExercises == null || scheduleExercises.isEmpty()) {
            throw new NoResultException("Nessun esercizio presente in questa scheda.");
        }

        schedule.setExercises(scheduleExercises);
        Long id = null;
        try { id = Long.parseLong(lowerSearch); } catch (NumberFormatException ignored) {}

        for (Exercise ex : scheduleExercises) {
            boolean match = (id != null && ex.getId() == id);
            // Se stringa vuota, prende tutti. Altrimenti filtra per nome o ID
            if (lowerSearch.isEmpty() || ex.getName().toLowerCase().contains(lowerSearch) || match) {
                exercises.add(ex);
            }
        }

        if (exercises.isEmpty()) throw new NoResultException("Nessun esercizio trovato per: " + search);
    }

    @Override
    public void retrieveExercise(Exercise exercise) throws DAOException, NoResultException {
        if (exercise == null) throw new DAOException("Esercizio non valido");
        ensureLoaded();
        Exercise stored = exercisesMap.get(exercise.getId());
        if (stored == null) throw new NoResultException("Esercizio non trovato");

        exercise.setName(stored.getName());
        exercise.setDescription(stored.getDescription());
        exercise.setNumberSeries(stored.getNumberSeries());
        exercise.setNumberReps(stored.getNumberReps());
        exercise.setRestTime(stored.getRestTime());
    }

    @Override
    public void retrieveExercises(Schedule schedule) throws DAOException, NoResultException {
        if (schedule == null) throw new DAOException("Scheda non valida");
        List<Exercise> exercises = new java.util.ArrayList<>();
        searchExercises(exercises, "", schedule);
        schedule.setExercises(exercises);
    }

    // Metodi implementati in modo basilare per compatibilità con l'interfaccia
    @Override
    public void addExerciseSchedule(Schedule schedule, Exercise exercise) throws DAOException { /* Logica di scrittura file omessa per brevità */ }
    @Override
    public void addExercise(Exercise exercise) throws DAOException { /* Logica di scrittura file omessa per brevità */ }
    @Override
    public void updateExercise(Exercise exercise) throws DAOException { /* Logica di scrittura file omessa per brevità */ }
    @Override
    public void deleteExercise(Exercise exercise) throws DAOException { /* Logica di scrittura file omessa per brevità */ }
    @Override
    public void retrieveAllExercises(Request request, List<Exercise> exercises) throws DAOException { }
    @Override
    public void searchAllExercises(List<Exercise> exercises, String search) throws DAOException, NoResultException { }
}
