package org.example.project3.dao.Factory;

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
    protected SubscriptionDAO createSubscriptionDAO() {
        return null;
    }

    @Override
    protected TrainerDAO createTrainerDAO() {
        return new TrainerDAOCSV();
    }

    @Override
    protected CourseDAO createCourseDAO() {
        return null;
    }

    @Override
    protected ReservationDAO createReservationDAO() {
        return null;
    }
}

