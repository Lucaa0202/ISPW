package org.example.project3.dao.sql;

import org.example.project3.dao.*;
import org.example.project3.dao.factory.DAOFactory;
import org.example.project3.exceptions.DAOException;
import org.example.project3.exceptions.DbOperationException;
import org.example.project3.exceptions.NoResultException;
import org.example.project3.model.*;
import org.example.project3.query.RequestQuery;
import org.example.project3.utilities.enums.Role;
import org.example.project3.utilities.others.Printer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList; // Aggiunto
import java.util.List;

public class RequestDAOSQL implements RequestDAO {
    private static final String ID = "id";
    private static final String REASON = "reason";
    private static final String DATETIME = "datetime";
    private static final String COURSE = "c.name";
    private static final String EXERCISENAME = "exercise.name";
    private static final String SCHEDULENAME = "schedule.name";
    private static final String SCHEDULECUSTOMER = "schedule.customer";
    private static final String SCHEDULEID = "schedule.id";
    private static final String REQUESTID = "request.id";
    private static final String EXERCISEID = "exercise.id";

    private static final String NAME = "cu.name";
    private static final String SURNAME = "surname";
    private static final String GENDER = "gender";
    private static final String EMAIL = "mail";
    private static final String INJURY = "injury";
    private static final String HOUR = "hour";
    private static final String DATE = "date";
    private static final String BIRTHDAY = "birthday";

    @Override
    public void sendRequest(Request request) throws DAOException {
        try(Connection conn = ConnectionSQL.getConnection()) {
            long id=RequestQuery.sendRequest(conn, request.getSchedule(), request.getExercise(), request.getReason());
            request.setId(id);
        } catch (SQLException | DbOperationException e){
            sendException(e);
        }
    }

    @Override
    public boolean hasAlreadySentRequest(Request request) throws DAOException {
        try (Connection conn = ConnectionSQL.getConnection()) {
            ResultSet rs = RequestQuery.hasAlreadySentARequest(conn, request);
            if(rs.next()){
                return rs.getInt(1) > 0;
            }
            return false;
        } catch (SQLException | DbOperationException e) {
            sendException(e);
            return false;
        }
    }

    @Override
    public void deleteRequest(Request request) throws DAOException {
        try(Connection conn = ConnectionSQL.getConnection()){
            RequestQuery.deleteRequest(conn, request);
        } catch(SQLException | DbOperationException e){
            sendException(e);
        }
    }


    @Override
    public List<Request> retrieveRequests(Trainer trainer) throws DAOException, NoResultException {
        List<Request> requests = new ArrayList<>(); // Crea la lista qui

        try (Connection conn = ConnectionSQL.getConnection();
             ResultSet rs = RequestQuery.retrieveRequests(conn, trainer.getCredentials().getMail())){
            while (rs.next()) {


                ScheduleDAO scheduleDAO = DAOFactory.getInstance().getScheduleDAO();

                Schedule schedule = scheduleDAO.retrieveScheduleById(rs.getLong(SCHEDULEID));


                ExerciseDAO exerciseDAO = DAOFactory.getInstance().getExerciseDAO();

                Exercise exercise = exerciseDAO.retrieveExerciseById(rs.getLong(EXERCISEID));


                Request request = new Request(
                        rs.getLong(REQUESTID),
                        schedule,
                        exercise,
                        rs.getString(REASON),
                        rs.getTimestamp(DATETIME).toLocalDateTime()
                );
                requests.add(request);
            }

            if (requests.isEmpty()) {
                throw new NoResultException("Nessuna richiesta trovata");
            }
        } catch (SQLException e) {
            sendException(e);
        }
        return requests;
    }

    @Override
    public void removeCourseRequest(Reservation reservation){
        try(Connection conn = ConnectionSQL.getConnection()) {
            RequestQuery.removeCourseRequest(conn, reservation.getCustomer().getCredentials().getMail(), reservation.getCourse().getCourseID(), reservation.getDay(), reservation.getHour());
        }catch (Exception e){
            handleException(e);
        }
    }


    @Override
    public List<Reservation> retrieveCourseRequest(Trainer trainer) throws DAOException {
        List<Reservation> reservationList = new ArrayList<>();


        try(Connection conn = ConnectionSQL.getConnection();
            ResultSet rs = RequestQuery.retireveCourseRequest(conn, trainer.getCredentials().getMail())){

            while(rs.next()){

                CustomerDAO customerDAO = DAOFactory.getInstance().getCustomerDAO();
                Customer customer = customerDAO.retrieveCustomerByMail(rs.getString(EMAIL));

                CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
                Course course = courseDAO.retrieveCourseById(rs.getInt(ID));

                Reservation reservation = new Reservation(customer, course, rs.getString(DATE), rs.getString(HOUR));
                reservationList.add(reservation);
            }
        } catch (SQLException e) {
            handleException(e);
            throw new DAOException("Errore nel recupero delle prenotazioni corso", e);
        }
        return reservationList;
    }

    @Override
    public void addCourseRequest(Reservation reservation) {
        try(Connection conn = ConnectionSQL.getConnection()) {
            RequestQuery.addCourseRequest(conn, reservation.getCustomer().getCredentials().getMail(), reservation.getCourse().getCourseID(), reservation.getDay(), reservation.getHour());
        }catch (SQLException | DbOperationException e){
            handleException(e);
        }
    }

    @Override
    public boolean alreadyHasReservation(Reservation reservation){
        try (Connection conn = ConnectionSQL.getConnection()) {
            ResultSet rs = RequestQuery.alreadyHasRequest(conn, reservation);
            if(rs.next()){
                return rs.getInt(1) > 0;
            }
            return false;
        } catch (SQLException | DbOperationException e) {
            handleException(e);
            return false;
        }
    }

    private void sendException(Exception e)throws DAOException{
        Printer.errorPrint(String.format("%s", e.getMessage()));
        throw new DAOException(e.getMessage(), e);
    }

    private void handleException(Exception e){
        Printer.errorPrint(String.format("%s", e.getMessage()));
    }
}
