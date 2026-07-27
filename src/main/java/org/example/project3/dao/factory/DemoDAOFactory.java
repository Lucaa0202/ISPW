package org.example.project3.dao.factory;

import org.example.project3.dao.*;
import org.example.project3.dao.demo.*;

public class DemoDAOFactory extends DAOFactory {

    @Override
    protected CredentialsDAO createCredentialsDAO() {
        return new CredentialsDAOP();
    }

    @Override
    protected CustomerDAO createCustomerDAO() {
        return new CustomerDAOP();
    }

    @Override
    protected ExerciseDAO createExerciseDAO() {
        return new ExerciseDAOP();
    }

    @Override
    protected RequestDAO createRequestDAO() {
        return new RequestDAOP();
    }

    @Override
    protected ScheduleDAO createScheduleDAO() {
        return new ScheduleDAOP();
    }

    @Override
    protected SubscriptionDAO createSubscriptionDAO() {
        return new SubscriptionDAOP();
    }

    @Override
    protected TrainerDAO createTrainerDAO() {
        return new TrainerDAOP();
    }

    @Override
    protected CourseDAO createCourseDAO() {
        return new CourseDAOP();
    }

    @Override
    protected ReservationDAO createReservationDAO() {
        return new ReservationDAOP();
    }
}