package org.example.project3.patterns.state;

import org.example.project3.utilities.others.Printer;
import org.example.project3.view.commandline.LoginCLI;
import org.example.project3.view.commandline.RegisterCLI;

import java.util.Scanner;

public class  InitialState extends AbstractState{
    public InitialState() {super();}
    @Override
    public void action(StateMachineConcrete context){
        AbstractState nextState;
        Scanner scanner = new Scanner(System.in);
        int choice;
        while(context.getCurrentState()!=null){
            try{
                this.stampa();
                this.showMenu();
                choice = scanner.nextInt();
                switch(choice){
                    case 1:
                        context.setState();
                        System.exit(0);
                        return;
                    case 2:
                        nextState = new LoginCLI();
                        goNext(context,nextState);
                        break;
                    case 3:
                        nextState = new RegisterCLI();
                        goNext(context,nextState);
                        break;
                    default:
                        Printer.errorPrint("Opzione non valida!");
                        break;
                }
            } catch (Exception _) {
                Printer.errorPrint("Errore nella scelta!");
                scanner.nextLine();
            }
        }
    }

    @Override
    public void showMenu() {
        Printer.println("1) Esci");
        Printer.println("2) Login");
        Printer.println("3) Registrati");
        Printer.print("Scelta: ");
    }
    @Override
    public void stampa() {
        Printer.println("");
        Printer.println("--------------------Benvenuto in Bodybuild--------------------");
        Printer.println("---------necessario effettuare il login o registrarsi---------");
    }
    @Override
    public void exit(StateMachineConcrete stateMachine){
        Printer.println("Uscita dallo stato iniziale");
    }
}