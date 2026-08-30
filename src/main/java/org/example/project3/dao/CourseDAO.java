package org.example.project3.dao;

import org.example.project3.exceptions.DAOException;
import org.example.project3.exceptions.DbOperationException;
import org.example.project3.exceptions.NoResultException;
import org.example.project3.model.Course;
import org.example.project3.model.Trainer;

import java.util.List;

public interface CourseDAO {

    List<Course> searchCourses() throws DAOException, NoResultException;


    Course retrieveCourseById(int courseId) throws DAOException, NoResultException;

    void addCourse(Course course) throws DAOException;
    void removeCourse(Course course) throws DAOException;
    void createAssociation(Course course, Trainer trainer) throws DAOException;
}