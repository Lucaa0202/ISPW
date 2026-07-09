package org.example.project3.dao.full.sql;

import org.example.project3.dao.CustomerDAO;
import org.example.project3.exceptions.DbOperationException;
import org.example.project3.exceptions.MailAlreadyExistsException;
import org.example.project3.model.Credentials;
import org.example.project3.model.Customer;
import org.example.project3.model.Subscription;
import org.example.project3.query.CredentialsQuery;
import org.example.project3.query.CustomerQuery;
import org.example.project3.utilities.others.Printer;

import java.sql.Connection;
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

    //inserisco l'utente (credenziali) nel database
    public boolean insertUser(Credentials credentials)  {
        try (Connection conn = ConnectionSQL.getConnection()) {
            int rs = CredentialsQuery.registerUser(conn, credentials);
            return rs != 0;
        }catch (SQLException |DbOperationException e){
            handleException(e);
            return false;
        }
    }

    public void registerCustomer(Customer customer) throws MailAlreadyExistsException {
        if(emailExists(customer.getCredentials().getMail())) {
            throw new MailAlreadyExistsException(("Mail già registrata"));
        }//inserisco la password e l'email in user
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
    public void retrieveCustomer(Customer customer) {
        try (Connection conn = ConnectionSQL.getConnection();
             ResultSet rs = CustomerQuery.retrieveCustomer(conn, customer.getCredentials().getMail())) {
            if (rs.next()) {
                customer.setName(rs.getString(NAME));
                customer.setSurname(rs.getString(SURNAME));
                customer.setGender(rs.getString(GENDER));
                customer.setOnline(rs.getBoolean(ONLINE));
                customer.setBirthday(rs.getDate(BIRTHDATE).toLocalDate());
                customer.setSubscription(new Subscription(rs.getInt(SUBSCRIPTION)));
                customer.setInjury(rs.getString(INJURY));
                customer.setStartDate(rs.getDate(STARTDATE));
                customer.setStartDate(rs.getDate(ENDDATE));
            }
        } catch (SQLException e) {
            handleException(e);
        }
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
