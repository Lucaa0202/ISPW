package org.example.project3.view.commandline;

import org.example.project3.beans.*;
import org.example.project3.controller.RequestModifyController;
import org.example.project3.controller.ScheduleController;
import org.example.project3.exceptions.DAOException;
import org.example.project3.patterns.observer.Observer;
import org.example.project3.patterns.state.AbstractState;
import org.example.project3.patterns.state.InitialState;
import org.example.project3.patterns.state.StateMachineConcrete;
import org.example.project3.utilities.others.Printer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ModifyScheduleCLI extends AbstractState implements Observer {

    protected RequestModifyController requestModifyController = new RequestModifyController();
    protected ScheduleController scheduleController = new ScheduleController();
    protected List<RequestBean> requestBeans=new ArrayList<>();
    protected List<ExerciseBean> exerciseBeans=new ArrayList<>();
    protected RequestBean requestBean;
    protected LoggedUserBean user;
    public ModifyScheduleCLI(LoggedUserBean user) {
        this.user = user;
    }
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void action(StateMachineConcrete context){
        boolean exit=false;
        while(!exit){
            try{
                int choice=Integer.parseInt(scanner.nextLine());
                switch(choice){
                    case 1 -> {
                        stampa();
                        requestChoice(context);
                    }
                    case 2 -> goNext(context, new CustomerHomepageCLI( user ));
                    case 0 ->{
                        exit=true;
                        goNext(context, new InitialState());
                    }
                    default -> Printer.errorPrint("Scelta non valida!");
                }
            }catch(Exception e){
                Printer.errorPrint("Errore durante la richiesta. Riprova più tardi."+e.getMessage());
                scanner.nextLine();
            }
        }
    }


    private boolean displayRequests(){
        try {
            if (requestBeans.isEmpty()) {
                Printer.println("Non c'è nessuna richiesta!");
                return false;
            } else {
                Printer.println("--------------------Lista delle richieste--------------------\n");
                Printer.println("(1)ID | (2)Cliente | (3)Scheda | (4)Esercizio | (5)Motivazione\n");
                for (RequestBean request : requestBeans) {
                    Printer.println(request.getID() + " | " + request.getScheduleBean().getCustomerBean().getCredentialsBean().getMail() + " | " + request.getScheduleBean().getName() + " | " + request.getExerciseBean().getName() + " | " + request.getReason());
                }
                Printer.println("Inserici l'ID della richiesta che vuoi gestire");
                Long id = Long.parseLong(scanner.nextLine());
                for (RequestBean requestTemp : requestBeans) {
                    if ((id.equals(requestTemp.getID()))) {
                        this.requestBean = requestTemp;
                        return true;
                    }
                }
                return false;
            }
        }catch(DAOException e){
            handleException(e);
            return false;
        }
    }


    private void requestChoice(StateMachineConcrete context) {
        try {
            if (displayRequests()) {
                Printer.println("1. Accetta modifica"); //in questo caso facciamo il login in automatico
                Printer.println("2. Rifiuta modifica");
                Printer.print("Opzione scelta: ");
                handleRequestOption(context);
            } else if (!requestBeans.isEmpty()) {
                Printer.errorPrint("È stato inserito un ID non corretto!");
                requestChoice(context);
            } else {
                goNext(context, new TrainerHomepageCLI(user));
            }
        }catch(DAOException _){
            printException();
        }
    }


    private void loadRequests(){
        try {
            requestBeans.clear();
            requestModifyController.retrieveRequests((TrainerBean) user, requestBeans);
        }catch(DAOException e){
            handleException(e);
            scanner.nextLine();
        }
    }

    private void handleRequestOption(StateMachineConcrete context) {
        try {
            int scelta = Integer.parseInt(scanner.nextLine());
            switch (scelta) {
                case 1 -> handleAcceptRequest(context);
                case 2 -> handleRejectRequest(context);
                default -> Printer.errorPrint("Scelta non valida!");
            }
        }catch(DAOException _){
            printException();
        }
    }

    private void handleAcceptRequest(StateMachineConcrete context) {
        try {
            loadExercises();
            if (displayExercises(requestBean)) {
                update();
                goNext(context, new TrainerHomepageCLI(user));
            } else if (!exerciseBeans.isEmpty()) {
                Printer.errorPrint("È stato inserito un nome non corretto!");
                handleAcceptRequest(context);
            } else {
                goNext(context, new TrainerHomepageCLI(user));
            }
        }catch(DAOException _){
            printException();
        }
    }

    private void handleRejectRequest(StateMachineConcrete context) {
        try {
            deleteRequest(requestBean);
            Printer.println("Richiesta eliminata con successo!");
            goNext(context, new TrainerHomepageCLI(user));
        }catch(DAOException _){
            printException();
        }
    }

    private boolean displayExercises(RequestBean requestBean){
        try {
            if (exerciseBeans.isEmpty()) {
                Printer.errorPrint("Non è stata trovato nessun esercizio con cui poterlo sostituire!");
                return false;
            } else {
                Printer.println("--------------------Lista degli esercizi ----------------------\n");
                Printer.println("(1)Nome | (2)Descrizione | (3)Numero di serie | (4)Numero di ripetizioni | (5)Tempo di recupero\n");
                for (ExerciseBean exerciseBean : exerciseBeans) {
                    Printer.println(exerciseBean.getName() + " | " + exerciseBean.getDescription() + " | " + exerciseBean.getNumberSeries() + " | " + exerciseBean.getNumberReps() + " | " + exerciseBean.getRestTime().getId() + " secondi");
                }
                Printer.println("Inserici il nome dell'esercizio con cui sostituire quello da modificare");
                String name = scanner.nextLine();
                for (ExerciseBean exerciseBean : exerciseBeans) {
                    if (name.equalsIgnoreCase(exerciseBean.getName())) {
                        scheduleController.updateSchedule(requestBean, exerciseBean);
                        requestModifyController.deleteRequest(requestBean);
                        return true;
                    }
                }
                return false;
            }
        }catch(DAOException e){
            handleException(e);
            return false;
        }
    }

    private void loadExercises(){
        try {
            exerciseBeans.clear();
            List<ExerciseBean> exerciseTemp=new ArrayList<>();
            scheduleController.retriveAllExercises(requestBean,exerciseTemp);
            exerciseBeans.addAll(exerciseTemp);
        }catch(DAOException _){
            printException();
        }
    }

    private void deleteRequest(RequestBean requestBean){
        try {
            requestModifyController.deleteRequest(requestBean);
        }catch(DAOException _){
            printException();
        }
    }


    @Override
    public void showMenu() {
        Printer.println("1. Scegli richiesta"); //in questo caso facciamo il login in automatico
        Printer.println("2. Indietro");
        Printer.println("0. Esci");
        Printer.print("Opzione scelta: ");
    }

    private void printException(){
        Printer.errorPrint("Errore nel DAO. Riprova.");
    }

    private void handleException(Exception e){
        Printer.println(String.format("Errore nel DAO. Riprova. %s", e.getMessage()));
    }



    @Override
    public void update(){

        Printer.println("Scheda modificata con successo");
    }



    @Override
    public void enter(StateMachineConcrete context){

        loadRequests();
        showMenu();
        action(context);
    }

    @Override
    public void stampa(){
        Printer.println(" ");
        Printer.println("-------------------Richieste Modifiche schede-------------------");
        Printer.println("Ciao"+ " " + user.getName()+ ",scegli quale scheda modificare:");
    }
}
