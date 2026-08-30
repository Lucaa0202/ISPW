package org.example.project3.dao.sql;

import org.example.project3.dao.ExerciseDAO;
import org.example.project3.exceptions.DAOException;
import org.example.project3.exceptions.DbOperationException;
import org.example.project3.exceptions.NoResultException;
import org.example.project3.model.*;
import org.example.project3.query.ExerciseQuery;
import org.example.project3.query.ScheduleQuery;
import org.example.project3.utilities.enums.RestTime;
import org.example.project3.utilities.others.Printer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExerciseDAOSQL implements ExerciseDAO {
    private static final String ID="id";
    private static final String NAME="name";
    private static final String DESCRIPTION="description";
    private static final String NUMBERSERIES="numberSeries";
    private static final String NUMBERREPS="numberReps";
    private static final String RESTTIME="restTime";



    @Override
    public void addExerciseSchedule(Schedule schedule, Exercise exercise) throws DAOException{
        try (Connection conn = ConnectionSQL.getConnection()) {
            ExerciseQuery.addExerciseSchedule(conn, schedule, exercise);
        } catch (SQLException | DbOperationException e) {
            handleException(e);
        }
    }

    @Override
    public void addExercise(Exercise exercise) throws DAOException{
        try (Connection conn = ConnectionSQL.getConnection()) {
            ExerciseQuery.addExercise(conn, exercise);
        } catch (SQLException | DbOperationException e) {
            handleException(e);
        }
    }

    @Override
    public void deleteExercise(Exercise exercise) throws DAOException{
        try (Connection conn = ConnectionSQL.getConnection()) {
            ExerciseQuery.deleteExercise(conn, exercise.getId());
        } catch (SQLException | DbOperationException e) {
            handleException(e);
        }
    }

    @Override
    public void updateExercise(Exercise exercise) throws DAOException{
        try(Connection conn = ConnectionSQL.getConnection()){
            ExerciseQuery.modifyExercise(conn, exercise);
        } catch(SQLException | DbOperationException e){
            handleException(e);
        }
    }



    @Override
    public Exercise retrieveExerciseById(long id) throws DAOException, NoResultException {
        Exercise exercise = null;
        try (Connection conn = ConnectionSQL.getConnection();
             ResultSet rs = ExerciseQuery.retrieveExerciseById(conn, id)) {

            if (rs.next()) {
                exercise = new Exercise(
                        id,
                        rs.getString(NAME),
                        rs.getString(DESCRIPTION),
                        rs.getInt(NUMBERSERIES),
                        rs.getInt(NUMBERREPS),
                        RestTime.convertIntToRestTime(rs.getInt(RESTTIME))
                );
            } else {
                throw new NoResultException("Nessun esercizio trovato con ID: " + id);
            }
        } catch (SQLException e) {
            throw new DAOException("Errore SQL nel recupero dell'esercizio", e);
        }
        return exercise;
    }

    @Override
    public List<Exercise> retrieveAllExercises(Request request) throws NoResultException, DAOException {
        List<Exercise> exercises = new ArrayList<>();
        try (Connection conn = ConnectionSQL.getConnection();
             ResultSet rs = ExerciseQuery.retrieveAllExercises(conn, request.getSchedule())){
            while (rs.next()) {
                exercises.add(new Exercise(
                        rs.getLong(ID),
                        rs.getString(NAME),
                        rs.getString(DESCRIPTION),
                        rs.getInt(NUMBERSERIES),
                        rs.getInt(NUMBERREPS),
                        RestTime.convertIntToRestTime(rs.getInt(RESTTIME))
                ));
            }
        } catch (SQLException | DbOperationException e) {
            handleException(e);
        }
        return exercises;
    }

    @Override
    public List<Exercise> searchExercises(String search, Schedule schedule) throws NoResultException, DAOException{
        List<Exercise> exercises = new ArrayList<>();
        try (Connection conn = ConnectionSQL.getConnection()) {
            ResultSet rs = ExerciseQuery.searchExercises(conn, search, schedule);
            while (rs.next()) {
                exercises.add(new Exercise(
                        rs.getLong(ID),
                        rs.getString(NAME),
                        rs.getString(DESCRIPTION),
                        rs.getInt(NUMBERSERIES),
                        rs.getInt(NUMBERREPS),
                        RestTime.convertIntToRestTime(rs.getInt(RESTTIME))
                ));
            }
        } catch (SQLException e) {
            throw new DAOException("Errore nella ricerca degli esercizi", e);
        }
        return exercises;
    }

    @Override
    public List<Exercise> searchAllExercises(String search) throws NoResultException, DAOException{
        List<Exercise> exercises = new ArrayList<>();
        try (Connection conn = ConnectionSQL.getConnection()) {
            ResultSet rs = ExerciseQuery.searchAllExercises(conn, search);
            while (rs.next()) {
                exercises.add(new Exercise(
                        rs.getLong(ID),
                        rs.getString(NAME),
                        rs.getString(DESCRIPTION),
                        rs.getInt(NUMBERSERIES),
                        rs.getInt(NUMBERREPS),
                        RestTime.convertIntToRestTime(rs.getInt(RESTTIME))
                ));
            }
        } catch (SQLException | DbOperationException e) {
            throw new DAOException("Errore nella ricerca di tutti gli esercizi", e);
        }
        return exercises;
    }

    @Override
    public List<Exercise> retrieveExercises(Schedule schedule) throws NoResultException, DAOException {
        List<Exercise> exercises = new ArrayList<>();
        try (Connection conn = ConnectionSQL.getConnection();
             ResultSet rs = ScheduleQuery.retrieveExercises(conn, schedule)){
            while (rs.next()) {
                exercises.add(new Exercise(
                        rs.getLong(ID),
                        rs.getString(NAME),
                        rs.getString(DESCRIPTION),
                        rs.getInt(NUMBERSERIES),
                        rs.getInt(NUMBERREPS),
                        RestTime.convertIntToRestTime(rs.getInt(RESTTIME))
                ));
            }
        } catch (SQLException e) {
            handleException(e);
        }
        return exercises;
    }

    private void handleException(Exception e) throws DAOException {
        Printer.println(String.format("%s", e.getMessage()));
        throw new DAOException("Errore nell'operazione sul database", e);
    }
}
