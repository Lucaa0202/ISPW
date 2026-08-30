package org.example.project3.dao;

import org.example.project3.exceptions.DAOException;
import org.example.project3.exceptions.NoResultException;
import org.example.project3.model.Exercise;
import org.example.project3.model.Request;
import org.example.project3.model.Schedule;

import java.util.List;

public interface ExerciseDAO {


    void addExerciseSchedule(Schedule schedule, Exercise exercise) throws DAOException;
    void addExercise(Exercise exercise) throws DAOException;
    void updateExercise(Exercise exercise) throws DAOException;
    void deleteExercise(Exercise exercise) throws DAOException;



    List<Exercise> retrieveExercises(Schedule schedule) throws DAOException, NoResultException;

    List<Exercise> searchExercises(String search, Schedule schedule) throws DAOException, NoResultException;


    List<Exercise> searchAllExercises(String search) throws DAOException, NoResultException;


    List<Exercise> retrieveAllExercises(Request request) throws DAOException;


    Exercise retrieveExerciseById(long id) throws DAOException, NoResultException;
}
