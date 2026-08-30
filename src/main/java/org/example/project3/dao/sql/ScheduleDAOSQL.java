package org.example.project3.dao.sql;

import org.example.project3.dao.CustomerDAO;
import org.example.project3.dao.ExerciseDAO;
import org.example.project3.dao.ScheduleDAO;
import org.example.project3.dao.TrainerDAO;
import org.example.project3.dao.factory.DAOFactory;
import org.example.project3.exceptions.DAOException;
import org.example.project3.exceptions.DbOperationException;
import org.example.project3.exceptions.NoResultException;
import org.example.project3.model.*;
import org.example.project3.query.ScheduleQuery;
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
    private static final String TRAINER="trainer";
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

    public List<Schedule> retrieveSchedule(Customer customer) throws NoResultException, DAOException {
        List<Schedule> schedules = new ArrayList<>();

        try (Connection conn = ConnectionSQL.getConnection();
             ResultSet rs = ScheduleQuery.retrieveSchedules(conn, customer.getCredentials().getMail())){

            while (rs.next()) {
                String trainerMail = rs.getString(TRAINER);


                TrainerDAO trainerDAO = DAOFactory.getInstance().getTrainerDAO();
                Trainer trainer = trainerDAO.retrieveTrainerByMail(trainerMail);
                Schedule schedule = new Schedule(
                        rs.getInt(ID),
                        rs.getString(NAME),
                        customer,
                        trainer);

                schedules.add(schedule);
            }

            if (schedules.isEmpty()) {
                throw new NoResultException("Nessuna scheda trovata");
            }

        } catch (SQLException e) {
            handleException(e);
        }

        return schedules;
    }
    @Override
    public Schedule retrieveScheduleById(long id) throws DAOException, NoResultException {
        Schedule schedule = null;

        try (Connection conn = ConnectionSQL.getConnection();
             ResultSet rs = ScheduleQuery.retrieveScheduleById(conn, id)) {

            if (rs.next()) {
                String name = rs.getString(NAME);

                String customerMail = rs.getString("customer");
                String trainerMail = rs.getString(TRAINER);


                CustomerDAO customerDAO = DAOFactory.getInstance().getCustomerDAO();
                Customer customer = customerDAO.retrieveCustomerByMail(customerMail);

                TrainerDAO trainerDAO = DAOFactory.getInstance().getTrainerDAO();
                Trainer trainer = trainerDAO.retrieveTrainerByMail(trainerMail);


                schedule = new Schedule(id, name, customer, trainer);


                ExerciseDAO exerciseDAO = DAOFactory.getInstance().getExerciseDAO();

                List<Exercise> exercises = exerciseDAO.retrieveExercises(schedule);

                schedule.setExercises(exercises);

            } else {
                throw new NoResultException("Nessuna scheda trovata con questo ID: " + id);
            }
        } catch (SQLException e) {
            throw new DAOException("Errore SQL nel recupero della scheda per ID", e);
        }

        return schedule;
    }
    @Override

    public List<Schedule> searchSchedules(String search, Customer user) throws NoResultException, DAOException{
        List<Schedule> schedules = new ArrayList<>();


        try (Connection conn = ConnectionSQL.getConnection();
             ResultSet rs = ScheduleQuery.searchSchedules(conn, search, user)) {


            while (rs.next()) {
                String trainerMail = rs.getString(TRAINER);


                TrainerDAO trainerDAO = DAOFactory.getInstance().getTrainerDAO();
                Trainer trainer = trainerDAO.retrieveTrainerByMail(trainerMail);

                Schedule schedule = new Schedule(
                        rs.getInt(ID),
                        rs.getString(NAME),
                        user,
                        trainer
                );
                schedules.add(schedule);
            }

            if (schedules.isEmpty()) {
                throw new NoResultException("Non è stata trovata nessuna scheda");
            }

        } catch (SQLException  e) {
            throw new DAOException("Errore nella ricerca della scheda", e);
        }

        return schedules;
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

    public Trainer retrieveTrainer(Schedule schedule) throws NoResultException, DAOException {
        Trainer trainer = null;

        try (Connection conn = ConnectionSQL.getConnection();
             ResultSet rs = ScheduleQuery.retrieveTrainer(conn, schedule)){

            if (rs.next()) {
                String trainerMail = rs.getString(SCHEDULETRAINER);


                TrainerDAO trainerDAO = DAOFactory.getInstance().getTrainerDAO();
                trainer = trainerDAO.retrieveTrainerByMail(trainerMail);

            } else {
                throw new NoResultException("Nessun trainer associato a questa scheda trovata");
            }

        } catch (SQLException e) {
            handleException(e);
        }

        return trainer;
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
