package org.example.project3.query;

import java.sql.*;
import org.example.project3.exceptions.DbOperationException;


public class ReservationQuery {
    private ReservationQuery() {}

    public static ResultSet retrieveReservation(Connection conn, String mail) throws SQLException {

        String query = "SELECT c.name, c.id, c.remaining, c.duration, c.level, r.date, r.hour " +
                "FROM course c, reservation r" +
                " WHERE c.id = r.course AND r.customer = ?";

        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, mail);
        return pstmt.executeQuery();
    }

    public static void addRequest(Connection conn, Integer course, String customer, String date, String hour) throws DbOperationException {
        String query = "INSERT INTO reservation(customer, course, date, hour) VALUES (?,?,?,?)";
        String updateCourse ="UPDATE course SET remaining=remaining-1 WHERE id=?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(query);
             PreparedStatement pstmt= conn.prepareStatement(updateCourse);) {

            preparedStatement.setString(1, customer);
            preparedStatement.setInt(2,course);
            preparedStatement.setString(3,date);
            preparedStatement.setString(4,hour);
            preparedStatement.executeUpdate();

            pstmt.setInt(1,course);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new DbOperationException("Errore aggiunta richiesta", e);
        }
    }

    public static void removeReservation(Connection conn, int course, String customer, String date, String hour) throws DbOperationException {
        String query = "DELETE FROM reservation WHERE customer = ? AND course = ? AND date = ? AND hour = ?";

        try(PreparedStatement pstmt= conn.prepareStatement(query)){
            pstmt.setString(1, customer);
            pstmt.setInt(2, course);
            pstmt.setString(3, date);
            pstmt.setString(4, hour);

            pstmt.executeUpdate();

        }catch(SQLException e){
            throw new DbOperationException("Errore aggiunta richiesta", e);
        }
    }
}
