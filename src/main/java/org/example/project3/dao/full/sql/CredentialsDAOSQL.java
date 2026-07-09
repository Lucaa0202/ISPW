package org.example.project3.dao.full.sql;

import org.example.project3.dao.CredentialsDAO;
import org.example.project3.exceptions.DbOperationException;
import org.example.project3.exceptions.MailAlreadyExistsException;
import org.example.project3.exceptions.WrongEmailOrPasswordException;
import org.example.project3.model.Credentials;
import org.example.project3.query.CredentialsQuery;
import org.example.project3.utilities.enums.Role;
import org.example.project3.utilities.others.Printer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class CredentialsDAOSQL implements CredentialsDAO {

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

    @Override
    public void login(Credentials credentials) throws WrongEmailOrPasswordException {
        try (Connection conn = ConnectionSQL.getConnection();
             ResultSet rs = CredentialsQuery.logQuery(conn, credentials)) {
            if (rs.next()) {
                credentials.setRole(Role.valueOf(rs.getString("role")));
            }
        } catch (SQLException _) {
            throw new WrongEmailOrPasswordException("Mail o password errati");
        }
    }

    @Override
    public void modifyCredentials(Credentials newCredentials, Credentials oldCredentials) throws MailAlreadyExistsException {
        try(Connection conn = ConnectionSQL.getConnection()){
            if(!Objects.equals(newCredentials.getMail(), oldCredentials.getMail()) && emailExists(newCredentials.getMail()))
                throw new MailAlreadyExistsException(("Mail già registrata"));
            CredentialsQuery.modifyCredentials(conn, newCredentials.getMail(), newCredentials.getPassword(), oldCredentials.getMail());

        } catch(SQLException | DbOperationException e){
            handleException(e);
        } catch (MailAlreadyExistsException e) {
            throw new MailAlreadyExistsException(e.getMessage());
        }
    }


    private void handleException(Exception e) {
        Printer.println(String.format("%s", e.getMessage()));
    }
}
