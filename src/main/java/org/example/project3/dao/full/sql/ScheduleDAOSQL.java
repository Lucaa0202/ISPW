package org.example.project3.dao.full.sql;

import org.example.project3.dao.ScheduleDAO;
import org.example.project3.exceptions.DAOException;
import org.example.project3.exceptions.DbOperationException;
import org.example.project3.exceptions.NoResultException;
import org.example.project3.model.*;
import org.example.project3.query.ScheduleQuery;
import org.example.project3.utilities.enums.RestTime;
import org.example.project3.utilities.enums.Role;
import org.example.project3.utilities.others.Printer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ScheduleDAOSQL implements ScheduleDAO {

    private static final String ID="id";
    private static final String NAME="name";
    private static final String CUSTOMER="customer";
    private static final String TRAINER="trainer";
    private static final String DESCRIPTION="description";
    private static final String NUMBERSERIES="numberSeries";
    private static final String NUMBERREPS="numberReps";
    private static final String RESTTIME="restTime";
    private static final String SCHEDULETRAINER="schedule.trainer";

    @Override
    public void addSchedule(Schedule schedule) throws DAOException {
        try (Connection conn = ConnectionSQL.getConnection()) {
            ScheduleQuery.addSchedule(conn, schedule);
        } catch (SQLException | DbOperationException e) {
            handleException(e);
        }
    }


    @Override
    public void retrieveSchedule(Customer customer,List<Schedule> schedules) throws NoResultException, DAOException {
        try (Connection conn = ConnectionSQL.getConnection();
             ResultSet rs = ScheduleQuery.retrieveSchedules(conn, customer.getCredentials().getMail())){
            while (rs.next()) {
                Schedule schedule = new Schedule(
                        rs.getInt(ID),
                        rs.getString(NAME),
                        new Customer(new Credentials(customer.getCredentials().getMail(), Role.CLIENT)),
                        new Trainer(new Credentials(rs.getString(TRAINER), Role.TRAINER)));
                schedules.add(schedule);
            }
        } catch (SQLException e) {
            handleException(e);
        }catch (NoResultException _){
            throw new NoResultException("Nessuna scheda trovata");
        }
    }

    @Override
    public void retrieveExercises(Schedule schedule) throws NoResultException,DAOException {
        if (schedule.getExercises() == null) {
            schedule.setExercises(new ArrayList<>());
        } else {
            schedule.getExercises().clear(); // Pulisci la lista se è già esistente per nuovi risultati
        }
        try (Connection conn = ConnectionSQL.getConnection();
             ResultSet rs = ScheduleQuery.retrieveExercises(conn, schedule)){
            while (rs.next()) {
                Exercise exercise = new Exercise(
                        rs.getInt(ID),
                        rs.getString(NAME),
                        rs.getString(DESCRIPTION),
                        rs.getInt(NUMBERSERIES),
                        rs.getInt(NUMBERREPS),
                        RestTime.convertIntToRestTime(rs.getInt(RESTTIME))
                );
                schedule.addExercise(exercise);
            }
        } catch (SQLException e) {
            handleException(e);
        }catch(NoResultException e){
            throw new NoResultException("Nessuna scheda trovata", e);
        }
    }

    @Override
    public void searchSchedules(List<Schedule> schedules, String search, Customer user) throws NoResultException,DAOException{
        try (Connection conn = ConnectionSQL.getConnection()) {
            ResultSet rs = ScheduleQuery.searchSchedules(conn, search, user);
            if (rs.next()) {
                Schedule schedule = new Schedule(
                        rs.getInt(ID),
                        rs.getString(NAME),
                        new Customer(new Credentials(user.getCredentials().getMail(), Role.CLIENT)),
                        new Trainer(new Credentials(rs.getString(TRAINER), Role.TRAINER))
                );
                schedules.add(schedule);
            }
        } catch (SQLException  e) {
            throw new DAOException("Errore nella ricerca della scheda", e);
        } catch (NoResultException e){
            throw new NoResultException("Non è stato trovata nessuna scheda", e);
        }
    }


    @Override
    public void deleteSchedule(Schedule schedule) throws DAOException {
        try (Connection conn = ConnectionSQL.getConnection()) {
            ScheduleQuery.deleteSchedule(conn, schedule.getCustomer().getCredentials().getMail(), schedule.getTrainer().getCredentials().getMail(), schedule.getName());
        } catch (SQLException | DbOperationException e) {
            handleException(e);
        }
    }

    @Override
    public void retrieveTrainer(Schedule schedule) throws NoResultException, DAOException {
        try (Connection conn = ConnectionSQL.getConnection();
             ResultSet rs = ScheduleQuery.retrieveTrainer(conn, schedule)){
            if (rs.next()) {
                Trainer trainer = new Trainer(new Credentials(rs.getString(SCHEDULETRAINER), Role.TRAINER));
                schedule.setTrainer(trainer);
            }
        } catch (SQLException e) {
            handleException(e);
        }catch (NoResultException _){
            throw new NoResultException("Nessuna scheda trovata");
        }
    }

    @Override
    public void updateSchedule(Request request, Exercise exercise) throws DAOException {
        try (Connection conn = ConnectionSQL.getConnection()) {
            ScheduleQuery.modifySchedule(conn, request.getSchedule(), exercise, request.getExercise());
        } catch (SQLException | DbOperationException e) {
            throw new DAOException(e.getMessage(), e);
        }
    }

    private void handleException(Exception e) throws DAOException {
        Printer.errorPrint(String.format("%s", e.getMessage()));
        throw new DAOException("Errore nell'operazione sul database", e);
    }

}
