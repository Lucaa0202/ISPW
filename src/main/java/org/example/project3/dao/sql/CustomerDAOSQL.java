package org.example.project3.dao.sql;

import org.example.project3.dao.CustomerDAO;
import org.example.project3.exceptions.DAOException;
import org.example.project3.exceptions.DbOperationException;
import org.example.project3.exceptions.MailAlreadyExistsException;
import org.example.project3.exceptions.NoResultException;
import org.example.project3.model.Credentials;
import org.example.project3.model.Customer;
import org.example.project3.model.Subscription;
import org.example.project3.query.CredentialsQuery;
import org.example.project3.query.CustomerQuery;
import org.example.project3.utilities.enums.Role;
import org.example.project3.utilities.others.Printer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAOSQL implements CustomerDAO {
    private static final String NAME = "name";
    private static final String SURNAME = "surname";
    private static final String GENDER = "gender";
    private static final String ONLINE = "online";
    private static final String BIRTHDATE = "birthday";
    private static final String SUBSCRIPTION = "subscription";
    private static final String INJURY = "injury";
    private static final String STARTDATE = "startDate";
    private static final String ENDDATE = "endDate";

    @Override
    public boolean emailExists(String mail)  {
        try (Connection conn = ConnectionSQL.getConnection()){
            int rs = CredentialsQuery.checkMail(conn, mail);
            if (rs != 0)
                return true;
        }catch (SQLException | DbOperationException e){
            handleException(e);
        }
        return false;
    }

    public boolean insertUser(Credentials credentials)  {
        try (Connection conn = ConnectionSQL.getConnection()) {
            int rs = CredentialsQuery.registerUser(conn, credentials);
            return rs != 0;
        }catch (SQLException |DbOperationException e){
            handleException(e);
            return false;
        }
    }

    @Override
    public void registerCustomer(Customer customer) throws MailAlreadyExistsException {
        if(emailExists(customer.getCredentials().getMail())) {
            throw new MailAlreadyExistsException(("Mail già registrata"));
        }
        boolean flag = insertUser(customer.getCredentials());
        if(flag){
            try (Connection conn = ConnectionSQL.getConnection()){
                CustomerQuery.registerCustomer(conn, customer);
            }
            catch(SQLException | DbOperationException e){
                handleException(e);
            }
        }
    }

    @Override
    public Customer retrieveCustomerByMail(String mail) throws DAOException, NoResultException {
        Customer customer = null;

        // FIX SONARCLOUD: Connection, PreparedStatement e ResultSet inseriti a catena nel try-with-resources!
        // Così si chiuderanno in automatico senza creare memory leak nel database.
        try (Connection conn = ConnectionSQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(CustomerQuery.RETRIEVE_CUSTOMER_BY_MAIL_QUERY);
             ResultSet rs = CustomerQuery.retrieveCustomerByMail(stmt, mail)) {

            if (rs.next()) {

                Credentials credentials = new Credentials(mail, Role.CLIENT);

                customer = new Customer(
                        credentials,
                        rs.getString(NAME),
                        rs.getString(SURNAME),
                        rs.getString(GENDER),
                        rs.getBoolean(ONLINE),
                        rs.getDate(BIRTHDATE).toLocalDate()
                );

                customer.setSubscription(new Subscription(rs.getInt(SUBSCRIPTION)));
                customer.setInjury(rs.getString(INJURY));

                if (rs.getDate(STARTDATE) != null) {
                    customer.setStartDate(rs.getDate(STARTDATE));
                }
                if (rs.getDate(ENDDATE) != null) {
                    customer.setEndDate(rs.getDate(ENDDATE));
                }

            } else {
                throw new NoResultException("Nessun customer trovato con la mail: " + mail);
            }

        } catch (SQLException e) {
            throw new DAOException("Errore SQL nel recupero del customer", e);
        }

        return customer;
    }

    @Override
    public void removeCustomer(Customer customer) {
        try (Connection conn = ConnectionSQL.getConnection()) {
            CustomerQuery.removeCustomer(conn, customer.getCredentials().getMail());
        } catch (SQLException | DbOperationException e) {
            handleException(e);
        }
    }

    @Override
    public void modifyCustomer(Customer customer) {
        try(Connection conn = ConnectionSQL.getConnection()){
            CustomerQuery.modifyCustomer(conn, customer);
        } catch(SQLException | DbOperationException e){
            handleException(e);
        }
    }

    private void handleException(Exception e) {
        Printer.println(String.format("%s", e.getMessage()));
    }
}
