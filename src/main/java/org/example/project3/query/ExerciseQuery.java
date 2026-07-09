package org.example.project3.query;

import org.example.project3.exceptions.DbOperationException;
import org.example.project3.model.Exercise;
import org.example.project3.model.Schedule;
import org.example.project3.utilities.others.Printer;

import java.sql.*;

public class ExerciseQuery {
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

    public static void modifyExercise(Connection conn, Exercise exercise) throws DbOperationException {
        String query = "UPDATE exercise SET numberSeries = ?, numberReps = ?, restTime = ? WHERE id = ? ";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, exercise.getNumberSeries());
            pstmt.setInt(2, exercise.getNumberReps());
            pstmt.setInt(3, exercise.getRestTime().getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DbOperationException("Errore nella modifica dell'esercizio", e);
        }
    }

    public static ResultSet retrieveExercise(Connection conn, Exercise exercise) throws SQLException {
        String query = "SELECT name, description, numberSeries, numberReps, restTime FROM exercise WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setLong(1, exercise.getId());
        return pstmt.executeQuery();
    }

    public static ResultSet retrieveAllExercises(Connection conn,Schedule schedule) throws DbOperationException {
        try{
            String query = "SELECT id, name, description, numberSeries, numberReps, restTime FROM exercise WHERE id NOT IN  (  SELECT exercise  FROM participation WHERE schedule = ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setLong(1, schedule.getId());
            return pstmt.executeQuery();
        } catch (SQLException e) {
            throw new DbOperationException("Errore nel recupero degli esercizi", e);
        }
    }


    public static ResultSet searchExercises(Connection conn, String search, Schedule schedule) throws SQLException {
        try {
            String query = "SELECT exercise.id, exercise.name, exercise.description, exercise.numberSeries, exercise.numberReps, exercise.restTime FROM exercise JOIN participation ON participation.exercise = exercise.id JOIN schedule ON schedule.id = participation.schedule WHERE LOWER(exercise.name) LIKE LOWER(?) AND schedule.id = LOWER(?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            String wildcard = "%" + search + "%";
            pstmt.setString(1, wildcard);
            pstmt.setLong(2, schedule.getId());
            return pstmt.executeQuery();
        } catch (SQLException _) {
            Printer.errorPrint("Errore nella ricerca degli esercizi");
            return null;
        }
    }

    public static ResultSet searchAllExercises(Connection conn, String search) throws DbOperationException {
        try {
            String query = "SELECT id, name, description, numberSeries, numberReps, restTime FROM exercise WHERE LOWER(name) LIKE LOWER(?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            String wildcard = "%" + search + "%";
            pstmt.setString(1, wildcard);
            return pstmt.executeQuery();
        }catch (SQLException e) {
            throw new DbOperationException("Errore nella ricerca", e);
        }
    }

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
            throw new DbOperationException("Errore nella rimozione dell'esercizio"+e.getMessage(), e);
        }
    }

}
