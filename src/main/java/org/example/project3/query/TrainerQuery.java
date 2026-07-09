package org.example.project3.query;

import org.example.project3.exceptions.DbOperationException;
import org.example.project3.exceptions.MailAlreadyExistsException;
import org.example.project3.model.Trainer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TrainerQuery {
    private TrainerQuery(){}

    public static void registerTrainer(Connection conn, Trainer trainer) throws SQLException, MailAlreadyExistsException, DbOperationException {
        String query = "INSERT INTO trainer (email, name, surname, gender, online, birthDate) VALUES (?,?,?,?,?,?)";
        try(PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, trainer.getCredentials().getMail());
            pstmt.setString(2, trainer.getName());
            pstmt.setString(3, trainer.getSurname());
            pstmt.setString(4, trainer.getGender());
            pstmt.setBoolean(5, trainer.isOnline());
            pstmt.setDate(6, java.sql.Date.valueOf(trainer.getBirthday()));
            int rs = pstmt.executeUpdate();
            if (rs == 0) {
                throw new MailAlreadyExistsException("Mail già esistente");
            }
        }catch (SQLException e) {
            throw new DbOperationException("Errore nella registrazione", e);
        }
    }



    public static void modifyTrainer(Connection conn, Trainer trainer) throws DbOperationException {
        String query = "UPDATE trainer SET name = ?, surname = ?, gender = ?, online = ?, birthDate = ? WHERE email = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, trainer.getName());
            pstmt.setString(2, trainer.getSurname());
            pstmt.setString(3, trainer.getGender());
            pstmt.setBoolean(4, trainer.isOnline());
            pstmt.setString(5, trainer.getCredentials().getMail());
            pstmt.setDate(6, java.sql.Date.valueOf(trainer.getBirthday()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DbOperationException("Errore nella modifica del profilo", e);
        }
    }

    public static ResultSet retrieveTrainer(Connection conn, String mail) throws SQLException {
        String query = "SELECT name, surname, gender, online, birthDate FROM trainer WHERE email = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, mail);
        return pstmt.executeQuery();
    }

    public static void removeTrainer(Connection conn, String mail) throws DbOperationException {
        String deletePatient = "DELETE FROM trainer WHERE email = ?";
        String deleteUser = "DELETE FROM users WHERE email = ?";

        try (PreparedStatement pstmt1 = conn.prepareStatement(deletePatient);
             PreparedStatement pstmt2 = conn.prepareStatement(deleteUser)) {

            pstmt1.setString(1, mail);
            pstmt1.executeUpdate();

            pstmt2.setString(1, mail);
            pstmt2.executeUpdate();

        } catch (SQLException e) {
            throw new DbOperationException("Errore nella rimozione del trainer", e);
        }
    }

    public static ResultSet retrieveCourseTrainer( Connection conn, String course) throws SQLException {
        String query = "SELECT t.email, t.name, t.surname, t.gender, t.birthDate FROM trainer t JOIN course c ON c.trainer = t.email WHERE c.name = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, course);
        return pstmt.executeQuery();
    }

    public static ResultSet retrieveSpecializzation(Connection conn, String course) throws SQLException {
        String query = "SELECT description FROM specialization s JOIN expertieses e ON e.specialization=s.id JOIN trainer t ON e.trainer=t.email JOIN course c ON t.email = c.trainer WHERE c.name = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, course);
        return pstmt.executeQuery();
    }
}
