package org.example.project3.dao;

import org.example.project3.model.Reservation;
import org.example.project3.model.Customer;
import java.util.List;

public interface ReservationDAO {

    void removeReservation(Reservation reservation);
    void addReservation(Reservation reservation);
    void retrieveReservation(Customer customer, List<Reservation> reservations);

    //reservation --- in memory ---- interfacce --- pattern ---- gui

}
