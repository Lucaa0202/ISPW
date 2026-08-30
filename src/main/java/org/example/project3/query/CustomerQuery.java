package org.example.project3.query;

import org.example.project3.exceptions.DbOperationException;
import org.example.project3.exceptions.MailAlreadyExistsException;
import org.example.project3.model.Customer;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;

public class CustomerQuery {

    // Espongo le stringhe SQL per permettere al DAO di creare il PreparedStatement
    public static final String RETRIEVE_CUSTOMER_BY_MAIL_QUERY = "SELECT * FROM customer WHERE mail = ?";
    public static final String RETRIEVE_CUSTOMER_QUERY = "SELECT mail, name, surname, gender, online, birthday, subscription, injury, startDate, endDate FROM customer WHERE mail = ?";

    private CustomerQuery() {}

    public static void registerCustomer(Connection conn, Customer customer) throws SQLException, MailAlreadyExistsException, DbOperationException {
        String query = "INSERT INTO customer (mail, name, surname, gender, online, birthday, injury, startDate) VALUES (?,?,?,?,?,?,?,?)";
        try(PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, customer.getCredentials().getMail());
            pstmt.setString(2, customer.getName());
            pstmt.setString(3, customer.getSurname());
            pstmt.setString(4, customer.getGender());
            pstmt.setBoolean(5, customer.isOnline());
            pstmt.setDate(6, java.sql.Date.valueOf(customer.getBirthday()));
            pstmt.setString(7, customer.getInjury());
            pstmt.setDate(8, Date.valueOf(LocalDate.now(ZoneId.systemDefault())));
            int rs = pstmt.executeUpdate();
            if (rs == 0) {
                throw new MailAlreadyExistsException("Mail già esistente");
            }
        } catch (SQLException e) {
            throw new DbOperationException("Errore nella registrazione", e);
        }
    }

    public static void modifyCustomer(Connection conn, Customer customer) throws DbOperationException {
        String query = "UPDATE customer SET name = ?, surname = ?, gender = ?, online = ?, birthday = ? WHERE mail = ?";
        // FIX SONARCLOUD: Aggiunto il try-with-resources
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, customer.getName());
            pstmt.setString(2, customer.getSurname());
            pstmt.setString(3, customer.getGender());
            pstmt.setBoolean(4, customer.isOnline());

            // FIX BUG LOGICO: L'ordine dei parametri era invertito rispetto alla stringa SQL!
            pstmt.setDate(5, java.sql.Date.valueOf(customer.getBirthday())); // Era al 6!
            pstmt.setString(6, customer.getCredentials().getMail());         // Era al 5!

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DbOperationException("Errore nella modifica del profilo", e);
        }
    }

    // NUOVA STRUTTURA: Ora riceve il PreparedStatement già aperto dal DAO
    public static ResultSet retrieveCustomerByMail(PreparedStatement stmt, String mail) throws SQLException {
        stmt.setString(1, mail);
        return stmt.executeQuery(); // Il ResultSet verrà chiuso dal DAO
    }

    // NUOVA STRUTTURA: Stessa cosa qui, nel caso venga richiamato in futuro
    public static ResultSet retrieveCustomer(PreparedStatement stmt, String mail) throws SQLException {
        stmt.setString(1, mail);
        return stmt.executeQuery();
    }

    public static void removeCustomer(Connection conn, String mail) throws DbOperationException {
        String deletePatient = "DELETE FROM customer WHERE mail = ?";
        String deleteUser = "DELETE FROM users WHERE email = ?";

        try (PreparedStatement pstmt1 = conn.prepareStatement(deletePatient);
             PreparedStatement pstmt2 = conn.prepareStatement(deleteUser)) {

            pstmt1.setString(1, mail);
            pstmt1.executeUpdate();

            pstmt2.setString(1, mail);
            pstmt2.executeUpdate();

        } catch (SQLException e) {
            throw new DbOperationException("Errore nella rimozione del cliente", e);
        }
    }
}
