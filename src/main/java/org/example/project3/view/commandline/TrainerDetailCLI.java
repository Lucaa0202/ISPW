package org.example.project3.view.commandline;

import org.example.project3.beans.*;
import org.example.project3.controller.TrainerDescriptionController;
import org.example.project3.patterns.state.AbstractState;
import org.example.project3.patterns.state.StateMachineConcrete;
import org.example.project3.utilities.others.Printer;


import java.util.Scanner;
import java.util.List;

public class TrainerDetailCLI extends AbstractState  {

    protected CustomerBean user;
    protected CourseBean selectedCourse;

    protected TrainerDescriptionController trainerDescriptionController= new TrainerDescriptionController();

    Scanner scanner = new Scanner(System.in);

    public TrainerDetailCLI(CustomerBean user, CourseBean selectedBean) {
        this.user = user;
        this.selectedCourse = selectedBean;
    }

    @Override
    public void action(StateMachineConcrete context){

        TrainerBean trainerBean = trainerDescriptionController.trainerDescription(selectedCourse);

        if(trainerBean == null){
            Printer.errorPrint("Trainer non trovato");
            goBack(context);
        }

        Printer.println("-------------------Trainer-------------------");
        Printer.println("Name: " + trainerBean.getName());
        Printer.println("Surname: " + trainerBean.getSurname());
        Printer.println("Mail" + trainerBean.getCredentialsBean().getMail());
        Printer.println("gender: " + trainerBean.getGender());

        List<String> specializations = trainerBean.getSpecializations();

        Printer.println("Specializations: ");

        for (String spec : specializations  ) {
            Printer.println(spec);
        }

        int scelta;
        while((scelta = Integer.parseInt(scanner.nextLine())) != 0){
            try{

                if (scelta == 1) {
                    goBack(context);
                } else {
                    Printer.errorPrint("errore di selezione");
                }

            }catch (Exception _){
                Printer.errorPrint("Scelta non valida");
            }
        }
    }


    @Override
    public void showMenu() {
        Printer.println("1.Torna indietro");
        Printer.println("Opzione scelta:");
    }


    @Override
    public void stampa() {
        Printer.println(" ");
        Printer.println("-------------------Trainer-------------------");
        Printer.println("Queste sono le informazioni del trainer");
    }


    @Override
    public void enter(StateMachineConcrete context) {
        stampa();
        showMenu();
    }
}
