package org.example.project3.view.commandline;

import org.example.project3.beans.CustomerBean;
import org.example.project3.beans.LoggedUserBean;
import org.example.project3.patterns.state.AbstractState;
import org.example.project3.patterns.state.InitialState;
import org.example.project3.patterns.state.StateMachineConcrete;
import org.example.project3.utilities.others.Printer;

import java.util.Scanner;

public class CustomerHomepageCLI extends AbstractState {

    protected LoggedUserBean user;
    public CustomerHomepageCLI(LoggedUserBean user) {
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
                    case 1 -> goNext(context, new CourseListCLI((CustomerBean) user));
                    case 2 -> goNext(context, new RequestCLI(user));
                    case 0-> {
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
    public void showMenu(){
        Printer.println("1. Prenotati per un corso");
        Printer.println("2. Richiedi la modifica di una scheda");
        Printer.println("0. Logout");
        Printer.print("Opzione scelta:");
    }


    @Override
    public void enter(StateMachineConcrete context) {
        stampa();
        showMenu();
    }

    @Override
    public void stampa(){
        Printer.println(" ");
        Printer.println("-------------------Benvenuto nella home di"+ " " + user.getName()+ " "+ user.getSurname() +"-------------------");
        Printer.println("Ciao"+ " " + user.getName()+ " "+ user.getSurname() +",scegli cosa vuoi fare:");
    }
}
