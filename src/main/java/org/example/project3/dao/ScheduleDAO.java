package org.example.project3.dao;

import org.example.project3.exceptions.DAOException;
import org.example.project3.exceptions.NoResultException;
import org.example.project3.model.Customer;
import org.example.project3.model.Exercise;
import org.example.project3.model.Request;
import org.example.project3.model.Schedule;

import java.util.List;

public interface ScheduleDAO {
    void addSchedule(Schedule schedule) throws DAOException;
    void retrieveExercises(Schedule schedule)  throws DAOException;
    void retrieveSchedule(Customer customer, List<Schedule> schedules)  throws DAOException, NoResultException;
    void searchSchedules(List<Schedule> schedules, String search, Customer user) throws DAOException, NoResultException;
    void deleteSchedule(Schedule schedule) throws DAOException;
    void retrieveTrainer(Schedule schedule) throws DAOException, NoResultException;
    void updateSchedule(Request request, Exercise exercise) throws DAOException;
}
