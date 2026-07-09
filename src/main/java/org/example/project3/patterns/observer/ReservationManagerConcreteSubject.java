package org.example.project3.patterns.observer;

import org.example.project3.model.Reservation;

import java.util.ArrayList;
import java.util.List;

public class ReservationManagerConcreteSubject extends Subject{

    private static ReservationManagerConcreteSubject instance = null;
    private List<Reservation> reservationsReq = new ArrayList<>();

    //Singleton
    public static ReservationManagerConcreteSubject getInstance(){
        if(instance == null){
            instance = new ReservationManagerConcreteSubject();
        }
        return instance;
    }

    private ReservationManagerConcreteSubject(){}


    public void addReservationReq(Reservation reservationReq){
        reservationsReq.add(reservationReq);
        notifyObservers();
    }

    public void removeReservationReq(Reservation reservation){

        reservationsReq.removeIf(r-> r.getCustomer().getCredentials().getMail().equals(reservation.getCustomer().getCredentials().getMail()) &&
                r.getCourse().getCourseName().equals(reservation.getCourse().getCourseName())
                && r.getDay().equals(reservation.getDay())
                && r.getHour().equals(reservation.getHour()));
        notifyObservers();

    }

    public List<Reservation> getReservationsReq(){
        return reservationsReq;
    }

    public void loadReservations(List<Reservation> reservations){
        this.reservationsReq = reservations;
    }
}
