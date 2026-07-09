package org.example.project3.dao;

import org.example.project3.exceptions.DbOperationException;
import org.example.project3.exceptions.NoResultException;
import org.example.project3.model.Course;
import org.example.project3.model.Trainer;

import java.util.List;

public interface CourseDAO {

    void searchCourses(List<Course> courses) throws NoResultException;
    void addCourse(Course course) throws NoResultException;
    void removeCourse(Course course) throws DbOperationException;
    void createAssociation(Course course, Trainer trainer) throws NoResultException;

}
