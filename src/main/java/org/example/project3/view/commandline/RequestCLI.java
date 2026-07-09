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

public class RequestCLI extends AbstractState implements Observer {

    protected ScheduleController scheduleController = new ScheduleController();
    protected List<ScheduleBean> scheduleBeans=new ArrayList<>();
    protected List<ExerciseBean> exerciseBeans=new ArrayList<>();
    protected RequestBean requestBean;
    protected RequestModifyController requestModifyController = new RequestModifyController();
    protected LoggedUserBean user;
    public RequestCLI(LoggedUserBean user) {
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
                        if (displaySchedules()) {
                            loadExercises(requestBean);
                            if (displayExercises(requestBean)) {
                                Printer.println("Specifica la motivazione della modifica:");
                                String motivazione=scanner.nextLine();
                                requestBean.setReason(motivazione);
                                requestModifyController.sendRequest(requestBean);
                                update();
                                goNext(context, new CustomerHomepageCLI( user ));
                            } else {
                                Printer.errorPrint("L'esercizio inserito non è presente!");
                            }
                        } else {
                            Printer.errorPrint("È state inserito un ID non corretto!");
                        }
                    }
                    case 2 -> goNext(context, new CustomerHomepageCLI( user ));
                    case 0 ->{
                        exit=true;
                        goNext(context, new InitialState());
                    }
                    default -> Printer.errorPrint("Scelta non valida!");
                }
            }catch(Exception _){
                Printer.errorPrint("Errore durante la richiesta. Riprova più tardi.");
                scanner.nextLine();
            }
        }
    }

    private void loadSchedules(){
        try {
            scheduleBeans.clear();
            scheduleController.retriveScheduleDetails((CustomerBean) user, scheduleBeans);
        }catch(DAOException e){
            handleException(e);
            scanner.nextLine();
        }
    }

    private boolean displaySchedules(){
        try {
            if (scheduleBeans.isEmpty()) {
                Printer.errorPrint("Non è stata trovata nessuna scheda!");
                return false;
            } else {
                Printer.println("--------------------Lista delle schede--------------------\n");
                Printer.println("(1)ID | (2)Nome | (3)Utente | (4)Trainer\n");
                for (ScheduleBean scheduleBean : scheduleBeans) {
                    Printer.println(scheduleBean.getId() + " | " + scheduleBean.getName() + " | " + scheduleBean.getCustomerBean().getCredentialsBean().getMail() + " | " + scheduleBean.getTrainerBean().getCredentialsBean().getMail());
                }
                Printer.println("Inserici l'ID della scheda che vuoi modificare");
                Long id = Long.parseLong(scanner.nextLine());
                for (ScheduleBean scheduleBean : scheduleBeans) {
                    if ((id.equals(scheduleBean.getId())) && (!requestModifyController.hasAlreadySentARequest(scheduleBean))) {
                        this.requestBean = new RequestBean(scheduleBean);
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

    private void loadExercises(RequestBean requestBean){
        try {
            exerciseBeans.clear();
            scheduleController.retriveExercises(requestBean.getScheduleBean());
            exerciseBeans.addAll(requestBean.getScheduleBean().getExercisesBean());
        }catch(DAOException _){
            Printer.errorPrint("Errore nel DAO. Riprova.");
            scanner.nextLine();
        }
    }

    private boolean displayExercises(RequestBean requestBean){
        if(exerciseBeans.isEmpty()){
            Printer.errorPrint("Non è stata trovato nessun esercizio!");
            return false;
        }else{
            Printer.println("--------------------Lista degli esercizi della scheda "+requestBean.getScheduleBean().getName()+"--------------------\n");
            Printer.println("(1)Nome | (2)Descrizione | (3)Numero di serie | (4)Numero di ripetizioni | (5)Tempo di recupero\n");
            for(ExerciseBean exerciseBean : exerciseBeans){
                Printer.println(exerciseBean.getName()+" | "+exerciseBean.getDescription()+" | "+exerciseBean.getNumberSeries()+" | "+exerciseBean.getNumberReps()+" | "+ exerciseBean.getRestTime().getId()+" secondi");
            }
            Printer.println("Inserici il nome dell'esercizio che vuoi modificare");
            String name=scanner.nextLine();
            for(ExerciseBean exerciseBean : exerciseBeans){
                if(name.equalsIgnoreCase(exerciseBean.getName())){
                    requestBean.setExercise(exerciseBean);
                    return true;
                }
            }
            return false;
        }
    }

    private void handleException(Exception e){
        Printer.println(String.format("Errore nel DAO. Riprova. %s", e.getMessage()));
    }

    @Override
    public void showMenu() {
        Printer.println("1. Scegli scheda"); //in questo caso facciamo il login in automatico
        Printer.println("2. Indietro");
        Printer.println("0. Esci");
        Printer.print("Opzione scelta: ");
    }

    @Override
    public void stampa(){
        Printer.println(" ");
        Printer.println("-------------------Modifica scheda-------------------");
        Printer.println("Ciao"+ " " + user.getName()+ ",scegli quale scheda modificare:");
    }

    @Override
    public void enter(StateMachineConcrete context){
        loadSchedules();
        showMenu();
        action(context);
    }

    @Override
    public void update(){
        Printer.println("Richiesta inviata con successo");
    }
}
