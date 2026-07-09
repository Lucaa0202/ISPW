package org.example.project3.dao.demo;

import org.example.project3.dao.CourseDAO;
import org.example.project3.dao.demo.shared.SharedResources;
import org.example.project3.exceptions.NoResultException;
import org.example.project3.model.Course;
import org.example.project3.model.Trainer;

import java.util.*;

public class CourseDAOP implements CourseDAO {

    @Override
    public void searchCourses(List<Course> courses) throws NoResultException {
        courses.addAll(SharedResources.getInstance().getCourses().values());
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

}