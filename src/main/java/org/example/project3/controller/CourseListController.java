package org.example.project3.controller;

import org.example.project3.dao.CourseDAO;
import org.example.project3.dao.RequestDAO;
import org.example.project3.exceptions.NoResultException;
import org.example.project3.patterns.factory.BeanAndModelMapperFactory;
import org.example.project3.patterns.factory.FactoryDAO;
import org.example.project3.patterns.observer.ReservationManagerConcreteSubject;

import org.example.project3.beans.*;
import org.example.project3.model.*;

import java.util.ArrayList;
import java.util.List;

public class CourseListController
{
    private final BeanAndModelMapperFactory factory;
    private final CourseDAO courseDAO;
    private final RequestDAO requestDAO;

    public CourseListController(){
        this.factory = BeanAndModelMapperFactory.getInstance();
        this.courseDAO = FactoryDAO.getCourseDAO();
        this.requestDAO = FactoryDAO.getRequestDAO();
    }

    public void retrieveCourses(List<CourseBean> coursesBean) {

        try {
            List<Course> courses = new ArrayList<>();
            courseDAO.searchCourses(courses);
            CourseBean courseBean;

            for (Course course : courses) {
                courseBean = factory.fromModelToBean(course, Course.class);
                coursesBean.add(courseBean);
            }
        }catch(Exception _) {
            throw new NoResultException("Errore recupero corsi");
        }
    }

    public void sendReservationReq(ReservationBean reservationBean) throws NoResultException {

        Reservation reservation = factory.fromBeanToModel(reservationBean, ReservationBean.class);

        try{
            requestDAO.addCourseRequest(reservation);
            ReservationManagerConcreteSubject reservationManagerConcreteSubject = ReservationManagerConcreteSubject.getInstance();
            reservationManagerConcreteSubject.addReservationReq(reservation);

        }catch(Exception _) {
            throw new NoResultException("Errore invio richiesta");
        }
    }

    public boolean alreadyHasRequest(ReservationBean reservationBean) throws NoResultException {
        Reservation reservation = factory.fromBeanToModel(reservationBean, ReservationBean.class);
        try{
            return requestDAO.alreadyHasReservation(reservation);
        }catch(Exception _){
            throw new NoResultException("Errore nel controllo delle richieste");
        }
    }

}