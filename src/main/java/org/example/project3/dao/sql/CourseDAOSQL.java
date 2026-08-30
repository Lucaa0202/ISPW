package org.example.project3.dao.sql;

import org.example.project3.dao.CourseDAO;
import org.example.project3.exceptions.DAOException;
import org.example.project3.exceptions.NoResultException;
import org.example.project3.model.Course;
import org.example.project3.model.Trainer;
import org.example.project3.query.CourseQuery;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseDAOSQL implements CourseDAO {

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String REMAINING = "remaining";
    private static final String DURATION = "duration";
    private static final String LEVEL = "level";
    private static final String DAY = "day";
    private static final String HOUR = "hour";

    @Override
    public List<Course> searchCourses() throws DAOException, NoResultException {
        List<Course> courses = new ArrayList<>();

        try (Connection conn = ConnectionSQL.getConnection();
             ResultSet rs = CourseQuery.retrieveCourse(conn)) {

            while (rs.next()) {
                Course course = new Course(
                        rs.getInt(ID),
                        rs.getString(NAME),
                        rs.getInt(REMAINING),
                        rs.getString(DURATION),
                        rs.getString(LEVEL),
                        rs.getString(DAY),
                        rs.getString(HOUR)
                );
                courses.add(course);
            }

            if (courses.isEmpty()) {
                throw new NoResultException("Nessun corso trovato");
            }

        } catch (SQLException e) {
            throw new DAOException("Errore ricerca corsi", e);
        }

        return courses;
    }

    @Override
    public Course retrieveCourseById(int courseId) throws DAOException, NoResultException {
        Course course = null;


        try (Connection conn = ConnectionSQL.getConnection();
             ResultSet rs = CourseQuery.retrieveCourse(conn)) {

            if (rs.next()) {
                course = new Course(
                        rs.getInt(ID),
                        rs.getString(NAME),
                        rs.getInt(REMAINING),
                        rs.getString(DURATION),
                        rs.getString(LEVEL),
                        rs.getString(DAY),
                        rs.getString(HOUR)
                );
            } else {
                throw new NoResultException("Corso non trovato per ID: " + courseId);
            }

        } catch (SQLException e) {
            throw new DAOException("Errore nel recupero del corso per ID", e);
        }

        return course;
    }

    @Override
    public void addCourse(Course course) {
        try (Connection conn = ConnectionSQL.getConnection()) {
            CourseQuery.insertCourse(conn, course);
        } catch (SQLException e) {
            throw new DAOException("Errore inserimento corso", e);
        }
    }

    @Override
    public void removeCourse(Course course) {
        try (Connection conn = ConnectionSQL.getConnection()) {
            CourseQuery.deleteCourse(conn, course.getCourseID());
        } catch (SQLException e) {
            throw new DAOException("Errore durante la rimozione del corso", e);
        }
    }

    @Override
    public void createAssociation(Course course, Trainer trainer) {
        try (Connection conn = ConnectionSQL.getConnection()) {
            CourseQuery.createAssociation(conn, course.getCourseID(), trainer.getCredentials().getMail());
        } catch (SQLException e) {
            throw new DAOException("Errore durante la creazione associazione", e);
        }
    }
}
