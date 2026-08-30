package org.example.project3.dao;

import org.example.project3.exceptions.DAOException;
import org.example.project3.exceptions.NoResultException;
import org.example.project3.model.*;

import java.util.List;

import java.util.List;

public interface ScheduleDAO {

    void addSchedule(Schedule schedule) throws DAOException;
    void deleteSchedule(Schedule schedule) throws DAOException;
    void updateSchedule(Request request, Exercise exercise) throws DAOException;

    Schedule retrieveScheduleById(long id) throws DAOException, NoResultException;

    List<Schedule> retrieveSchedule(Customer customer) throws DAOException, NoResultException;

    List<Schedule> searchSchedules(String search, Customer user) throws DAOException, NoResultException;


    Trainer retrieveTrainer(Schedule schedule) throws DAOException, NoResultException;
}
