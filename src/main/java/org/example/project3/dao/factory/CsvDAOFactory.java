package org.example.project3.dao.factory;

import org.example.project3.dao.*;
import org.example.project3.dao.csv.*;

public class CsvDAOFactory extends DAOFactory {

    @Override
    protected CredentialsDAO createCredentialsDAO() {
        return new CredentialsDAOCSV();
    }

    @Override
    protected CustomerDAO createCustomerDAO() {
        return new CustomerDAOCSV();
    }

    @Override
    protected ExerciseDAO createExerciseDAO() {
        return new ExerciseDAOCSV();
    }

    @Override
    protected RequestDAO createRequestDAO() {

        return new RequestDAOCSV();
    }

    @Override
    protected ScheduleDAO createScheduleDAO() {

        return new ScheduleDAOCSV();
    }


    @Override
    protected TrainerDAO createTrainerDAO() {
        return new TrainerDAOCSV();
    }

    @Override
    protected SubscriptionDAO createSubscriptionDAO() {
        throw new UnsupportedOperationException("SubscriptionDAOCSV non implementato");
    }

    @Override
    protected CourseDAO createCourseDAO() {
        throw new UnsupportedOperationException("CourseDAOCSV non implementato");
    }

    @Override
    protected ReservationDAO createReservationDAO() {
        throw new UnsupportedOperationException("ReservationDAOCSV non implementato");
    }
}

