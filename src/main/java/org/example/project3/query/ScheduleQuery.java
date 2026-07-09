package org.example.project3.query;

import org.example.project3.exceptions.DbOperationException;
import org.example.project3.model.Exercise;
import org.example.project3.model.LoggedUser;
import org.example.project3.model.Schedule;
import org.example.project3.utilities.others.Printer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ScheduleQuery {
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

    public static ResultSet retrieveSchedules(Connection conn, String mailCustomer) throws SQLException {
        //Mettere un order by date (da più a meno recente)
        String query = "SELECT id, name, customer, trainer FROM schedule WHERE customer = ? ";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, mailCustomer);
        return pstmt.executeQuery();
    }

    public static ResultSet retrieveExercises(Connection conn, Schedule schedule) throws SQLException {
        //Mettere un order by date (da più a meno recente)
        String query = "SELECT exercise.id, exercise.name, exercise.description, exercise.numberSeries, exercise.numberReps, exercise.restTime FROM exercise JOIN participation ON exercise.id = participation.exercise JOIN schedule ON participation.schedule = schedule.id WHERE schedule.id = ? ";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setLong(1, schedule.getId());
        return pstmt.executeQuery();
    }

    public static ResultSet searchSchedules(Connection conn, String search, LoggedUser customer) throws SQLException {
        try {
            String query = "SELECT id, name, trainer FROM schedule WHERE LOWER(name) LIKE LOWER(?) OR LOWER(id) LIKE LOWER(?) AND LOWER(customer) = LOWER(?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            String wildcard = "%" + search + "%";
            pstmt.setString(1, wildcard);
            pstmt.setString(2, wildcard);
            pstmt.setString(3, customer.getCredentials().getMail());
            return pstmt.executeQuery();
        } catch (SQLException _) {
            Printer.errorPrint("Errore nella ricerca della scheda");
            return null;
        }
    }


    public static void modifySchedule(Connection conn, Schedule schedule, Exercise newExercise, Exercise oldExercise) throws DbOperationException {
        String checkQuery = "SELECT COUNT(*) FROM participation WHERE schedule = ? AND exercise = ?";
        String updateQuery = "UPDATE participation SET exercise = ? WHERE schedule = ? AND exercise = ?";

        try {
            // Check if the new exercise already exists for this schedule
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setLong(1, schedule.getId());
                checkStmt.setLong(2, newExercise.getId());
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new DbOperationException("L'esercizio è già associato a questa scheda.");
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

    public static ResultSet retrieveTrainer(Connection conn, Schedule schedule) throws SQLException {
        //Mettere un order by date (da più a meno recente)
        String query = "SELECT schedule.trainer FROM request JOIN schedule ON schedule.id = request.schedule WHERE schedule.id = ? ";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setLong(1, schedule.getId());
        return pstmt.executeQuery();
    }
}
