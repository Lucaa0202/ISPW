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
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExerciseDAOCSV implements ExerciseDAO {

    private static final String EXERCISES_FILE = "src/main/resources/data/exercises.csv";
    private static final String SCHED_EX_FILE = "src/main/resources/data/schedule_exercises.csv";
    private static final Logger LOGGER = Logger.getLogger(ExerciseDAOCSV.class.getName());

    private final Map<Long, Exercise> exercisesMap = new HashMap<>();
    private final Map<Long, List<Exercise>> exerciseSchedulesMap = new HashMap<>();


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
            String header = reader.readLine();
            if (header == null) {
                return;
            }
            String line;
            while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
                String[] fields = line.split(",", -1);
                long id = Long.parseLong(fields[0]);
                String name = fields[1];
                String desc = fields[2];
                Integer series = fields[3].isEmpty() ? null : Integer.parseInt(fields[3]);
                Integer reps = fields[4].isEmpty() ? null : Integer.parseInt(fields[4]);

                RestTime rest = parseRestTime(fields[5]);

                Exercise ex = new Exercise(id, name, desc, series, reps, rest);
                exercisesMap.put(id, ex);
            }

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Errore lettura exercises.csv", e);
        }
    }

    private void loadExerciseSchedules() {
        exerciseSchedulesMap.clear();
        Path path = Paths.get(SCHED_EX_FILE);
        if (!Files.exists(path)) return;

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String header = reader.readLine();
            if (header == null) {
                return;
            }
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
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Errore lettura schedule_exercises.csv", e);
        }
    }



    private RestTime parseRestTime(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return RestTime.valueOf(value);
        } catch (IllegalArgumentException _) {
            return null;
        }
    }

    private Long parseLongOrNull(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException _) {
            return null;
        }
    }



    @Override
    public List<Exercise> searchExercises(String search, Schedule schedule) throws DAOException, NoResultException {
        if (search == null || schedule == null) throw new DAOException("Parametri non validi");
        ensureLoaded();

        String lowerSearch = search.toLowerCase().trim();
        List<Exercise> scheduleExercises = exerciseSchedulesMap.get(schedule.getId());

        if (scheduleExercises == null || scheduleExercises.isEmpty()) {
            throw new NoResultException("Nessun esercizio presente in questa scheda.");
        }

        List<Exercise> result = new ArrayList<>();
        Long id = parseLongOrNull(lowerSearch);

        for (Exercise ex : scheduleExercises) {
            boolean match = (id != null && ex.getId() == id);
            if (lowerSearch.isEmpty() || ex.getName().toLowerCase().contains(lowerSearch) || match) {
                result.add(ex);
            }
        }

        if (result.isEmpty()) throw new NoResultException("Nessun esercizio trovato per: " + search);

        return result; // Restituisce la lista
    }

    @Override
    public Exercise retrieveExerciseById(long id) throws DAOException, NoResultException {
        ensureLoaded();
        Exercise stored = exercisesMap.get(id);
        if (stored == null) {
            throw new NoResultException("Esercizio non trovato con ID: " + id);
        }
        return stored; //
    }

    @Override
    public List<Exercise> retrieveExercises(Schedule schedule) throws DAOException, NoResultException {
        if (schedule == null) throw new DAOException("Scheda non valida");
        ensureLoaded();

        List<Exercise> scheduleExercises = exerciseSchedulesMap.get(schedule.getId());
        if (scheduleExercises == null || scheduleExercises.isEmpty()) {
            throw new NoResultException("Nessun esercizio presente in questa scheda.");
        }

        return new ArrayList<>(scheduleExercises); // Restituisce una copia sicura della lista
    }

    @Override
    public List<Exercise> retrieveAllExercises(Request request) throws DAOException, NoResultException {

        if (request == null || request.getSchedule() == null) throw new DAOException("Richiesta non valida");
        return retrieveExercises(request.getSchedule());
    }

    @Override
    public List<Exercise> searchAllExercises(String search) throws DAOException, NoResultException {
        ensureLoaded();
        List<Exercise> result = new ArrayList<>();
        String lowerSearch = search == null ? "" : search.toLowerCase().trim();
        Long id = parseLongOrNull(lowerSearch);

        for (Exercise ex : exercisesMap.values()) {
            boolean match = (id != null && ex.getId() == id);
            if (lowerSearch.isEmpty() || ex.getName().toLowerCase().contains(lowerSearch) || match) {
                result.add(ex);
            }
        }

        if (result.isEmpty()) throw new NoResultException("Nessun esercizio trovato");
        return result;
    }


    @Override
    public void addExerciseSchedule(Schedule schedule, Exercise exercise) throws DAOException {
        throw new UnsupportedOperationException("Metodo non implementato nella versione CSV");
    }
    @Override
    public void addExercise(Exercise exercise) throws DAOException {
        throw new UnsupportedOperationException("Metodo non implementato nella versione CSV");
    }
    @Override
    public void updateExercise(Exercise exercise) throws DAOException {
        throw new UnsupportedOperationException("Metodo non implementato nella versione CSV");
    }
    @Override
    public void deleteExercise(Exercise exercise) throws DAOException {
        throw new UnsupportedOperationException("Metodo non implementato nella versione CSV");
    }
}