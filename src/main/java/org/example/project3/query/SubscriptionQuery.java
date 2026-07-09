package org.example.project3.query;

import org.example.project3.exceptions.DbOperationException;
import org.example.project3.model.Subscription;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SubscriptionQuery {
    private SubscriptionQuery() {}

    public static void addSubscription(Connection conn, Subscription subscription) throws DbOperationException {
        String query = "INSERT INTO subscription (name, type, price, discount) VALUES (?, ?, ?,?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, subscription.getName());
            preparedStatement.setString(2, subscription.getType().toString());
            preparedStatement.setFloat(3, subscription.getPrice());
            preparedStatement.setInt(4, subscription.getDisconut());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DbOperationException("Errore nell'aggiunta dell'abbonamento", e);
        }
    }

    public static ResultSet retrieveSubscription(Connection conn, String name) throws SQLException {
        //Mettere un order by date (da più a meno recente)
        String query = "SELECT name, type, price, discount FROM subscription  WHERE name = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, name);
        return pstmt.executeQuery();
    }

    public static void modifySubscription(Connection conn, Subscription subscription) throws DbOperationException {
        String query = "UPDATE subscription SET name = ?, type = ?, price = ?, discount = ? WHERE id = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, subscription.getName());
            pstmt.setInt(2, subscription.getType().getId());
            pstmt.setFloat(3, subscription.getPrice());
            pstmt.setInt(4, subscription.getDisconut());
            pstmt.setInt(5, subscription.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DbOperationException("Errore nella modifica dell'abbonamento", e);
        }
    }

    public static void deleteSubscription(Connection conn, Integer id) throws DbOperationException {
        String query = "DELETE FROM subscription WHERE id = ? ";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DbOperationException("Errore nella rimozione dell'abbonamento", e);
        }
    }
}
