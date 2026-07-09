package org.example.project3.view.commandline;

import org.example.project3.controller.CourseListController;
import org.example.project3.beans.*;
import org.example.project3.patterns.state.AbstractState;
import org.example.project3.patterns.state.StateMachineConcrete;
import org.example.project3.utilities.others.Printer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CourseListCLI extends AbstractState {

    private final  List<CourseBean> courses = new ArrayList<>();
    protected CustomerBean user;
    protected CourseListController courseListController = new CourseListController();

    Scanner scanner = new Scanner(System.in);

    public CourseListCLI(CustomerBean user) {
        this.user = user;
    }


    public void loadCourses(){

        List<CourseBean> course = new ArrayList<>();
        courseListController.retrieveCourses(course);
        if(course.isEmpty()) {
            Printer.errorPrint("Non ci sono corsi");
            return;
        }
        courses.clear();
        courses.addAll(course);
    }


    private int getCourseIndex(){
        int courseIndex = -1;
        boolean valid = false;
        while(!valid){
            try{
                Printer.println("Seleziona la richiesta");
                courseIndex = Integer.parseInt(scanner.nextLine().trim()) - 1;
                if(courseIndex < 0 || courseIndex >= courses.size()){
                    Printer.errorPrint("Errore nella scelta1");
                }else {
                    valid = true;
                }
            }catch(NumberFormatException _){
                Printer.errorPrint("Errore nella scelta2");
            }
        }

        return courseIndex;
    }


    @Override
    public void action(StateMachineConcrete context){

        loadCourses();

        Printer.println("----------------Lista Corsi--------------------");
        for(int i=0; i<courses.size(); i++){
            Printer.println((i+1) + ". " + courses.get(i).getCourseName() + " " + courses.get(i).getRemainingSlots() + " " + courses.get(i).getDay() + " " + courses.get(i).getHour() + " " + courses.get(i).getLevel());
        }

        Printer.println(" ");

        int selectedIndex = getCourseIndex();

        if(selectedIndex >= 0 && selectedIndex < courses.size()) {

            showMenu();
            int scelta = -1;
            boolean validChoice = false;

            while (!validChoice) {
                try {
                    Printer.println("Scelta:");
                    scelta = Integer.parseInt(scanner.nextLine().trim());
                    validChoice = true;
                } catch (NumberFormatException _) {
                    Printer.println("Scelta non valida. Inserisci un numero.");
                }
            }

            switch (scelta) {

                case 1 -> {
                    sendRequest(selectedIndex);
                    goNext(context,new CourseListCLI(user));
                }

                case 2 -> {
                    CourseBean selectedCourse = courses.get(selectedIndex);
                    goNext(context, new TrainerDetailCLI(user, selectedCourse));
                }

                case 3 -> goBack(context);
                default -> Printer.errorPrint("Scelta non valida. ");
            }

        }else{
            Printer.errorPrint("Errore nella scelta");
        }
    }

    private void sendRequest(int selected){

        CourseBean selectedCourse = courses.get(selected);
        ReservationBean reservationBean = new ReservationBean(user, selectedCourse, selectedCourse.getDay(), selectedCourse.getHour());
        if(!courseListController.alreadyHasRequest(reservationBean)){
            courseListController.sendReservationReq(reservationBean);
            Printer.println( " Richiesta inviata ");
        }else{
            Printer.errorPrint( " Hai già inviato una richiesta ");
        }
    }

    @Override
    public void showMenu() {
        Printer.println("1.Prenota Corso");
        Printer.println("2.Visualizza Dettagli Trainer");
        Printer.println("3.Indietro");
        Printer.print("Scelta:");
    }

    @Override
    public void stampa() {
        Printer.println(" ");
        Printer.println("-------------------Benvenuto pagina prenotazione corsi -------------------");
    }

    @Override
    public void enter(StateMachineConcrete context) {
        loadCourses();
        stampa();
    }
}