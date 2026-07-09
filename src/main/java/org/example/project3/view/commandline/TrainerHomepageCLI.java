package org.example.project3.view.commandline;


import org.example.project3.beans.LoggedUserBean;
import org.example.project3.patterns.state.AbstractState;
import org.example.project3.patterns.state.InitialState;
import org.example.project3.patterns.state.StateMachineConcrete;
import org.example.project3.utilities.others.Printer;

import java.util.Scanner;


public class TrainerHomepageCLI extends AbstractState{

    protected LoggedUserBean user;
    private final Scanner scanner = new Scanner(System.in);
    public TrainerHomepageCLI(LoggedUserBean user) {
        this.user = user;
    }


    @Override
    public void action(StateMachineConcrete context){
        boolean exit=false;
        while(!exit){
            try{
                int choice=Integer.parseInt(scanner.nextLine());
                switch(choice){
                    case 1 -> goNext(context, new RequestTrainerCLI(user));
                    case 2 -> goNext(context, new ModifyScheduleCLI(user));
                    case 0 -> {
                        exit=true;
                        goNext(context, new InitialState());
                    }
                    default -> Printer.errorPrint("Scelta non valida!");

                }
            }catch(Exception _){
                Printer.errorPrint("Scelta non valida!");
                scanner.nextLine();
            }
        }
    }

    @Override
    public void enter(StateMachineConcrete context){
        stampa();
        showMenu();
    }

    @Override
    public void stampa(){
        Printer.println(" ");
        Printer.println("-------------------Benvenuto nella home del trainer-------------------");
    }

    @Override
    public void showMenu(){
        Printer.println("1. Visualizza Richieste Corsi");
        Printer.println("2. Visualizza Richieste Modifiche Schede");
        Printer.println("0. Logout");

    }

}