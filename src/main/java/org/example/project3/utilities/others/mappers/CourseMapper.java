package org.example.project3.utilities.others.mappers;


import org.example.project3.model.Course;
import org.example.project3.beans.CourseBean;

public class CourseMapper implements BeanAndModelMapper<CourseBean, Course> {

    TrainerMapper trainerMapper = new TrainerMapper();
    @Override
    public Course fromBeanToModel(CourseBean bean) {
        return new Course(bean.getCourseID(), bean.getCourseName(), bean.getRemainingSlots(), bean.getDuration(), bean.getLevel(), bean.getDay(), bean.getHour());
    }

    @Override
    public CourseBean fromModelToBean(Course model) {
        return new CourseBean(model.getCourseID(), model.getCourseName(), model.getRemainingSlots(), model.getDuration(), model.getLevel(), model.getDay(), model.getHour());
    }
}
