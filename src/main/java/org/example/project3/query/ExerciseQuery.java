package org.example.project3.query;

import org.example.project3.exceptions.DbOperationException;
import org.example.project3.model.Exercise;
import org.example.project3.model.Schedule;

import java.sql.*;

public class ExerciseQuery {

    // =========================================================
    // COSTANTI SQL ESPOSTE AL DAO PER IL TRY-WITH-RESOURCES
    // =========================================================
    public static final String RETRIEVE_EXERCISE_BY_ID_QUERY = "SELECT * FROM exercise WHERE id = ?";
    public static final String RETRIEVE_EXERCISE_QUERY = "SELECT name, description, numberSeries, numberReps, restTime FROM exercise WHERE id = ?";
    public static final String RETRIEVE_ALL_EXERCISES_QUERY = "SELECT id, name, description, numberSeries, numberReps, restTime FROM exercise WHERE id NOT IN (SELECT exercise FROM participation WHERE schedule = ?)";
    public static final String SEARCH_EXERCISES_QUERY = "SELECT exercise.id, exercise.name, exercise.description, exercise.numberSeries, exercise.numberReps, exercise.restTime FROM exercise JOIN participation ON participation.exercise = exercise.id JOIN schedule ON schedule.id = participation.schedule WHERE LOWER(exercise.name) LIKE LOWER(?) AND schedule.id = LOWER(?)";
    public static final String SEARCH_ALL_EXERCISES_QUERY = "SELECT id, name, description, numberSeries, numberReps, restTime FROM exercise WHERE LOWER(name) LIKE LOWER(?)";

    private ExerciseQuery(){}

    public static void addExerciseSchedule(Connection conn, Schedule schedule, Exercise exercise) throws DbOperationException {
        String intoSchedule = "INSERT INTO participation (schedule, exercise) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(intoSchedule)) {
            pstmt.setLong(1, schedule.getId());
            pstmt.setLong(2, exercise.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DbOperationException("Errore nell'aggiunta dell'esercizio", e);
        }
    }

    public static void addExercise( Connection conn, Exercise exercise) throws DbOperationException {
        String insertExercise = "INSERT INTO exercise (id, name, description, numberseries, numberReps, restTime) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(insertExercise)) {
            preparedStatement.setLong(1, exercise.getId());
            preparedStatement.setString(2, exercise.getName());
            preparedStatement.setString(3, exercise.getDescription());
            preparedStatement.setInt(4, exercise.getNumberSeries());
            preparedStatement.setInt(5, exercise.getNumberReps());
            preparedStatement.setString(6, String.valueOf(exercise.getRestTime().getId()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DbOperationException("Errore nell'aggiunta dell'esercizio", e);
        }
    }

    // =========================================================
    // METODI REFACTORIZZATI PER RICEVERE IL PREPARED STATEMENT
    // =========================================================

    public static ResultSet retrieveExerciseById(PreparedStatement stmt, long id) throws SQLException {
        stmt.setLong(1, id);
        return stmt.executeQuery();
    }

    public static void modifyExercise(Connection conn, Exercise exercise) throws DbOperationException {
        String query = "UPDATE exercise SET numberSeries = ?, numberReps = ?, restTime = ? WHERE id = ? ";
        // FIX SONARCLOUD: Aggiunto il try-with-resources per pstmt
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, exercise.getNumberSeries());
            pstmt.setInt(2, exercise.getNumberReps());
            pstmt.setInt(3, exercise.getRestTime().getId());

            // FIX BUG LOGICO: Mancava l'id (il parametro 4)! Ora è sistemato.
            pstmt.setLong(4, exercise.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DbOperationException("Errore nella modifica dell'esercizio", e);
        }
    }

    public static ResultSet retrieveExercise(PreparedStatement stmt, Exercise exercise) throws SQLException {
        stmt.setLong(1, exercise.getId());
        return stmt.executeQuery();
    }

    public static ResultSet retrieveAllExercises(PreparedStatement stmt, Schedule schedule) throws SQLException {
        stmt.setLong(1, schedule.getId());
        return stmt.executeQuery();
    }

    public static ResultSet searchExercises(PreparedStatement stmt, String search, Schedule schedule) throws SQLException {
        String wildcard = "%" + search + "%";
        stmt.setString(1, wildcard);
        stmt.setLong(2, schedule.getId());
        return stmt.executeQuery();
    }

    public static ResultSet searchAllExercises(PreparedStatement stmt, String search) throws SQLException {
        String wildcard = "%" + search + "%";
        stmt.setString(1, wildcard);
        return stmt.executeQuery();
    }

    // =========================================================

    public static void deleteExercise(Connection conn, Long id) throws DbOperationException {
        String participationQuery = "DELETE FROM participation WHERE exercise = ?";
        String query = "DELETE FROM exercise WHERE id = ? ";
        try (PreparedStatement pstmt = conn.prepareStatement(query);
             PreparedStatement pstmt1 = conn.prepareStatement(participationQuery)) {

            pstmt1.setLong(1, id);
            pstmt1.executeUpdate();

            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DbOperationException("Errore nella rimozione dell'esercizio" + e.getMessage(), e);
        }
    }
}
