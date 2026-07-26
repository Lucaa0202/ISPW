package org.example.project3.dao.Factory;


import org.example.project3.dao.*;
import org.example.project3.dao.sql.*;

public class SqlDAOFactory extends DAOFactory {

    @Override
    protected CredentialsDAO createCredentialsDAO() {
        return new CredentialsDAOSQL();
    }

    @Override
    protected CustomerDAO createCustomerDAO() {
        return new CustomerDAOSQL();
    }

    @Override
    protected ExerciseDAO createExerciseDAO() {
        return new ExerciseDAOSQL();
    }

    @Override
    protected RequestDAO createRequestDAO() {
        return new RequestDAOSQL();
    }

    @Override
    protected ScheduleDAO createScheduleDAO() {
        return new ScheduleDAOSQL();
    }

    @Override
    protected SubscriptionDAO createSubscriptionDAO() {
        return new SubscriptionDAOSQL();
    }

    @Override
    protected TrainerDAO createTrainerDAO() {
        return new TrainerDAOSQL();
    }

    @Override
    protected CourseDAO createCourseDAO() {
        return new CourseDAOSQL();
    }

    @Override
    protected ReservationDAO createReservationDAO() {
        return new ReservationDAOSQL();
    }
}