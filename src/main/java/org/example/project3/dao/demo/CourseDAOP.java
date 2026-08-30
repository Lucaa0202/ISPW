package org.example.project3.dao.demo;

import org.example.project3.dao.CourseDAO;
import org.example.project3.dao.demo.shared.SharedResources;
import org.example.project3.exceptions.NoResultException;
import org.example.project3.model.Course;
import org.example.project3.model.Trainer;

import java.util.*;

public class CourseDAOP implements CourseDAO {


    @Override
    public List<Course> searchCourses() throws NoResultException {
        // Prendiamo tutti i corsi direttamente dalle SharedResources
        List<Course> courses = new ArrayList<>(SharedResources.getInstance().getCourses().values());

        if (courses.isEmpty()) {
            throw new NoResultException("Nessun corso trovato.");
        }

        return courses;
    }

    @Override
    public void addCourse(Course course){
        SharedResources.getInstance().getCourses().
                putIfAbsent(course.getCourseName(), course);
    }

    @Override
    public void removeCourse(Course course) {
        SharedResources.getInstance().getCourses().remove(course.getCourseName());
    }

    @Override
    public void createAssociation(Course course, Trainer trainer){
        SharedResources.getInstance().getTrainerCourse().putIfAbsent(course.getCourseName(), trainer);
    }


    @Override
    public Course retrieveCourseById(int id) throws NoResultException {
        for (Course course : SharedResources.getInstance().getCourses().values()) {
            if (course.getCourseID() == id) {
                return course;
            }
        }
        throw new NoResultException("Corso non trovato con ID: " + id);
    }
}