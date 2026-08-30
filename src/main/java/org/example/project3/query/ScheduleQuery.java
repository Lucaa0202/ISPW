package org.example.project3.query;

import org.example.project3.exceptions.DbOperationException;
import org.example.project3.model.Exercise;
import org.example.project3.model.LoggedUser;
import org.example.project3.model.Schedule;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ScheduleQuery {

    // =========================================================
    // COSTANTI SQL ESPOSTE AL DAO PER IL TRY-WITH-RESOURCES
    // =========================================================
    public static final String RETRIEVE_SCHEDULES_QUERY = "SELECT id, name, customer, trainer FROM schedule WHERE customer = ? ";
    public static final String RETRIEVE_EXERCISES_QUERY = "SELECT exercise.id, exercise.name, exercise.description, exercise.numberSeries, exercise.numberReps, exercise.restTime FROM exercise JOIN participation ON exercise.id = participation.exercise JOIN schedule ON participation.schedule = schedule.id WHERE schedule.id = ? ";
    public static final String RETRIEVE_SCHEDULE_BY_ID_QUERY = "SELECT * FROM schedule WHERE id = ?";

    // FIX BUG SICUREZZA: Aggiunte le parentesi attorno all'OR!
    public static final String SEARCH_SCHEDULES_QUERY = "SELECT id, name, trainer FROM schedule WHERE (LOWER(name) LIKE LOWER(?) OR LOWER(id) LIKE LOWER(?)) AND LOWER(customer) = LOWER(?)";

    public static final String RETRIEVE_TRAINER_QUERY = "SELECT schedule.trainer FROM request JOIN schedule ON schedule.id = request.schedule WHERE schedule.id = ? ";

    private ScheduleQuery(){}

    public static void addSchedule(Connection conn, Schedule schedule) throws DbOperationException {
        String query = "INSERT INTO schedule (id,name, customer, trainer) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setLong(1, schedule.getId());
            preparedStatement.setString(2, schedule.getName());
            preparedStatement.setString(3, schedule.getCustomer().getCredentials().getMail());
            preparedStatement.setString(4, schedule.getTrainer().getCredentials().getMail());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DbOperationException("Errore nell'aggiunta della scheda", e);
        }
    }

    // =========================================================
    // METODI REFACTORIZZATI (Ricevono PreparedStatement)
    // =========================================================

    public static ResultSet retrieveSchedules(PreparedStatement stmt, String mailCustomer) throws SQLException {
        stmt.setString(1, mailCustomer);
        return stmt.executeQuery();
    }

    public static ResultSet retrieveScheduleById(PreparedStatement stmt, long id) throws SQLException {
        stmt.setLong(1, id);
        return stmt.executeQuery();
    }

    public static ResultSet searchSchedules(PreparedStatement stmt, String search, LoggedUser customer) throws SQLException {
        String wildcard = "%" + search + "%";
        stmt.setString(1, wildcard);
        stmt.setString(2, wildcard);
        stmt.setString(3, customer.getCredentials().getMail());
        return stmt.executeQuery();
    }

    public static ResultSet retrieveTrainer(PreparedStatement stmt, Schedule schedule) throws SQLException {
        stmt.setLong(1, schedule.getId());
        return stmt.executeQuery();
    }

    // =========================================================

    public static void modifySchedule(Connection conn, Schedule schedule, Exercise newExercise, Exercise oldExercise) throws DbOperationException {
        String checkQuery = "SELECT COUNT(*) FROM participation WHERE schedule = ? AND exercise = ?";
        String updateQuery = "UPDATE participation SET exercise = ? WHERE schedule = ? AND exercise = ?";

        try {
            // Check if the new exercise already exists for this schedule
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setLong(1, schedule.getId());
                checkStmt.setLong(2, newExercise.getId());
                // FIX SONARCLOUD: Aggiunto try-with-resources per il ResultSet
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        throw new DbOperationException("L'esercizio è già associato a questa scheda.");
                    }
                }
            }

            // Perform the update
            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                updateStmt.setLong(1, newExercise.getId());
                updateStmt.setLong(2, schedule.getId());
                updateStmt.setLong(3, oldExercise.getId());
                int rowsAffected = updateStmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new DbOperationException("Nessuna riga aggiornata. Verifica i dati della scheda e dell'esercizio.");
                }
            }
        } catch (SQLException e) {
            throw new DbOperationException("Errore nella modifica del scheda: " + e.getMessage(), e);
        }
    }

    public static void deleteSchedule(Connection conn, String mailCustomer, String mailTrainer, String name) throws DbOperationException {
        String query = "DELETE FROM schedule WHERE customer = ? AND trainer = ? AND name = ? ";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, mailCustomer);
            pstmt.setString(2, mailTrainer);
            pstmt.setString(3, name);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DbOperationException("Errore nella rimozione della scheda", e);
        }
    }
    public static ResultSet retrieveExercises(PreparedStatement stmt, Schedule schedule) throws SQLException {
        stmt.setLong(1, schedule.getId());
        return stmt.executeQuery();
    }
}
