package org.example.project3.controller;


import org.example.project3.dao.RequestDAO;
import org.example.project3.exceptions.NoResultException;
import org.example.project3.dao.factory.DAOFactory;
import org.example.project3.patterns.factory.BeanAndModelMapperFactory;

import org.example.project3.beans.*;
import org.example.project3.model.*;
import org.example.project3.patterns.observer.ReservationManagerConcreteSubject;

import java.util.List;

public class ReservationListController {

    private final BeanAndModelMapperFactory factory;
    private final RequestDAO reservationDAO;

    public ReservationListController() {
        this.factory = BeanAndModelMapperFactory.getInstance();
        this.reservationDAO = DAOFactory.getInstance().getRequestDAO();
    }

    public void getReservationReq(TrainerBean trainer, List<ReservationBean> reservationReqBean){

        try{
            List<Reservation> reservationReq;
            Trainer ttrainer = factory.fromBeanToModel(trainer, TrainerBean.class);
            reservationReq = reservationDAO.retrieveCourseRequest(ttrainer);
            ReservationBean reservationBean;

            for(Reservation reservation : reservationReq){
                reservationBean = factory.fromModelToBean(reservation, Reservation.class);
                reservationReqBean.add(reservationBean);
            }

            ReservationManagerConcreteSubject reservationManagerConcreteSubject = ReservationManagerConcreteSubject.getInstance();
            reservationManagerConcreteSubject.loadReservations(reservationReq);

        }catch(Exception _){
            throw new NoResultException("Errore recupero descrizione trainer");
        }
    }
}
