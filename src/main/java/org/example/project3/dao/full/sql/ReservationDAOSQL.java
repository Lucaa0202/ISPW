package org.example.project3.dao.full.sql;

import org.example.project3.dao.ReservationDAO;
import org.example.project3.exceptions.DAOException;
import org.example.project3.exceptions.DbOperationException;
import org.example.project3.model.*;
import org.example.project3.query.ReservationQuery;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ReservationDAOSQL implements ReservationDAO {

    private static final String NAME = "name";
    private static final String ID = "id";
    private static final String REMAINING = "remaining";
    private static final String DURATION = "duration";
    private static final String LEVEL = "level";
    private static final String DATE = "r.date";
    private static final String HOURR = "r.hour";

    @Override
    public void removeReservation(Reservation reservation) {
        try (Connection conn = ConnectionSQL.getConnection()) {
            ReservationQuery.removeReservation(conn, reservation.getCourse().getCourseID(), reservation.getCustomer().getCredentials().getMail(), reservation.getDay(), reservation.getHour());
        } catch (SQLException | DbOperationException e) {
            throw new DAOException("Errore rimozione reservation", e);
        }
    }

    @Override
    public void addReservation(Reservation reservation){

        try(Connection conn = ConnectionSQL.getConnection()) {
            ReservationQuery.addRequest(conn, reservation.getCourse().getCourseID(),reservation.getCustomer().getCredentials().getMail(), reservation.getDay(), reservation.getHour());
        }catch (SQLException | DbOperationException e){
            throw new DAOException("Errore rimozione reservation", e);
        }
    }

    @Override
    public void retrieveReservation(Customer customer, List<Reservation> reservations) {

        try (Connection conn = ConnectionSQL.getConnection()) {
            ResultSet rs = ReservationQuery.retrieveReservation(conn, customer.getCredentials().getMail());
            while (rs.next()) {
                Course course = new Course(rs.getInt(ID), rs.getString(NAME), rs.getInt(REMAINING), rs.getString(DURATION), rs.getString(LEVEL), rs.getString(DATE), rs.getString(HOURR));
                Reservation reservation = new Reservation(customer, course, rs.getString(DATE), rs.getString(HOURR));
                reservations.add(reservation);
            }

        } catch (SQLException e) {
            throw new DAOException("Errore recupero reservation", e);
        }
    }

}
