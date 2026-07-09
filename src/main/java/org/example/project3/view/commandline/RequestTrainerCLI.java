package org.example.project3.view.commandline;


import org.example.project3.beans.TrainerBean;
import org.example.project3.beans.ReservationBean;
import org.example.project3.beans.LoggedUserBean;
import org.example.project3.controller.ReservationListController;
import org.example.project3.controller.ReservationReqApplicationController;

import org.example.project3.patterns.observer.Observer;
import org.example.project3.patterns.observer.ReservationManagerConcreteSubject;
import org.example.project3.patterns.state.AbstractState;
import org.example.project3.patterns.state.StateMachineConcrete;
import org.example.project3.utilities.others.Printer;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;


public class RequestTrainerCLI extends AbstractState implements Observer {

    private final List<ReservationBean> reservations = new ArrayList<>();
    protected ReservationListController reservationListController = new ReservationListController();
    protected ReservationReqApplicationController reservationReqApplicationController = new ReservationReqApplicationController();
    private final ReservationManagerConcreteSubject reservationManagerConcreteSubject = ReservationManagerConcreteSubject.getInstance();

    protected LoggedUserBean user;
    private final Scanner scanner = new Scanner(System.in);

    public RequestTrainerCLI(LoggedUserBean user){
        this.user = user;
        this.reservationManagerConcreteSubject.addObserver(this);
    }

    @Override
    public void action(StateMachineConcrete context){
        boolean running = true;
        while(running){
            try{
                int choice = Integer.parseInt(scanner.nextLine().trim());
                switch(choice){
                    case 1 ->{
                        displayReservation();
                        showMenu();

                    }
                    case 2 -> {
                        handleReservation(true);
                        showMenu();

                    }
                    case 3 -> {
                        handleReservation(false);
                        showMenu();

                    }
                    case 4 -> {
                        viewDetail();
                        showMenu();

                    }
                    case 5 -> {
                        goBack(context);
                        running = false;
                    }
                    default -> Printer.errorPrint("Scelta non valida");
                }

            }catch(Exception _){
                Printer.errorPrint("Errore durante l'elaborazione");
            }
        }
    }


    private void handleReservation(boolean accept){
        if(reservations.isEmpty()){
            Printer.errorPrint("Nessuna richiesta trovata!");
            return;
        }

        displayReservation();
        int reservationIndex =  getReservationIndex();
        if(reservationIndex >= 0 && reservationIndex < reservations.size()){
            ReservationBean reservation = reservations.get(reservationIndex);
            processReservation(reservation, accept);
            reservations.remove(reservationIndex);
            loadReservation();
        }
    }

    private void processReservation(ReservationBean reservation, boolean accept){
        reservationReqApplicationController.deleteReservationReq(reservation);
        if(accept){
            reservationReqApplicationController.addReservation(reservation);
            Printer.println("Richiesta accettata prenotazione registrata");
        }else{
            Printer.errorPrint("Richiesta rifiutata");
        }

    }


    private int getReservationIndex(){
        int reservationIndex = -1;
        boolean valid = false;
        while(!valid){
            try{
                Printer.println("Seleziona la richiesta");
                reservationIndex = Integer.parseInt(scanner.nextLine().trim()) -1;
                if(reservationIndex < 0 || reservationIndex >= reservations.size()){
                    Printer.errorPrint("Errore nella scelta");
                }else {
                    valid = true;
                }
            }catch(NumberFormatException _){
                Printer.errorPrint("Errore nella scelta");
            }
        }

        return reservationIndex;
    }

    private void displayReservation(){
        if(reservations.isEmpty()){
            Printer.errorPrint("Non sono presneti richieste");
        }else{
            Printer.println("\n ----------------- Lista di Richieste -----------------\n");
            int index = 1;
            for ( ReservationBean reservation : reservations) {
                Printer.println(index + " Richiesta da: " + reservation.getCustomer().getName() + " | "
                        + "Corso: " + reservation.getCourse().getCourseName() + " | " + reservation.getDay() + " | " + reservation.getHour());
                index++;
            }
            Printer.println(" ");
        }
    }


    public void loadReservation(){
        List<ReservationBean> reservationsReq = new ArrayList<>();
        reservationListController.getReservationReq((TrainerBean)user, reservationsReq);

        if(reservationsReq.isEmpty()){
            Printer.errorPrint("Richieste non presenti");
            return;
        }
        reservations.clear();
        reservations.addAll(reservationsReq);
    }


    private void showDetail(ReservationBean reservation){

        Printer.println("-------------------Customer-------------------");
        Printer.println("Name: " + reservation.getCustomer().getName());
        Printer.println("Surname: " + reservation.getCustomer().getSurname());
        Printer.println("Mail" + reservation.getCustomer().getCredentialsBean().getMail());
        Printer.println("gender: " + reservation.getCustomer().getGender());
        Printer.println("Injury " + reservation.getCustomer().getInjury());
        Printer.println("--------------------------------------");
        Printer.println(" ");

    }

    private void viewDetail(){

        if(reservations.isEmpty()){
            Printer.errorPrint("Richieste non trovate");
            return;
        }

        displayReservation();

        int reservationIndex =  getReservationIndex();
        if(reservationIndex >= 0 && reservationIndex < reservations.size()){
            ReservationBean reservation = reservations.get(reservationIndex);
            showDetail(reservation);
        }
    }


    @Override
    public void showMenu(){
        Printer.println("1.Visualizza richieste");
        Printer.println("2.Accetta Richiesta");
        Printer.println("3.Rifiuta richiesta");
        Printer.println("4.Visualizza dettagli richiesta");
        Printer.println("5.Indietro");
        Printer.print("Opzione scelta");

    }

    @Override
    public void stampa(){
        Printer.println(" ");
        Printer.println("----------------------Richieste-----------------------");

    }

    @Override
    public void enter(StateMachineConcrete context){
        loadReservation();
        stampa();
        showMenu();
        action(context);
    }

    //pattern observer
    @Override
    public void update(){
        loadReservation();
        if(reservations.isEmpty()){
            Printer.errorPrint("Non sono presenti richieste");
        }else{
            Printer.println("Aggiornamento Richieste");
        }
    }

}
