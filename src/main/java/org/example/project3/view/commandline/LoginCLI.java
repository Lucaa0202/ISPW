package org.example.project3.view.commandline;

import org.example.project3.beans.CredentialsBean;
import org.example.project3.beans.CustomerBean;
import org.example.project3.beans.LoggedUserBean;
import org.example.project3.beans.TrainerBean;
import org.example.project3.controller.LoginController;
import org.example.project3.exceptions.NoResultException;
import org.example.project3.exceptions.WrongEmailOrPasswordException;
import org.example.project3.patterns.state.AbstractState;
import org.example.project3.patterns.state.StateMachineConcrete;
import org.example.project3.utilities.enums.Role;
import org.example.project3.utilities.others.Printer;

import java.util.Scanner;

public class LoginCLI extends AbstractState {
    LoggedUserBean user;
    private final Scanner scanner = new Scanner(System.in);
    private final LoginController login = new LoginController();

    @Override
    public void action(StateMachineConcrete context) {
        //metodo effettivo per il login
        Printer.println("---Email: ");
        String email = scanner.next();
        Printer.println("---Password: ");
        String password = scanner.next();
        //controllo se l'utente è registrato
        //se si, setto l'utente loggato
        //altrimenti stampo errore

        try {
            //creazione delle credenziali
            CredentialsBean credentials = new CredentialsBean(email, password, null);
            //login e verifica credenziali
            login.login(credentials);
            //determino il ruolo e prepara lo stato successivo
            AbstractState homeCLI;
            if(credentials.getRole().equals(Role.TRAINER)){
                user = setupTrainer(login, credentials);
                homeCLI = new TrainerHomepageCLI(user);
            } else {
                user = setupCustomer(login, credentials);
                homeCLI = new CustomerHomepageCLI(user);
            }
            goNext(context, homeCLI);
        } catch (WrongEmailOrPasswordException | NoResultException _) {
            Printer.errorPrint("Email o password errati");
            action(context);
        }
    }
    private TrainerBean setupTrainer(LoginController login, CredentialsBean credentials) throws NoResultException {
        TrainerBean trainerBean = new TrainerBean(credentials);
        login.retrieveTrainer(trainerBean);
        return trainerBean;
    }
    private CustomerBean setupCustomer(LoginController login, CredentialsBean credentials) throws NoResultException {
        CustomerBean customerBean = new CustomerBean(credentials);
        login.retrieveCustomer(customerBean);
        return customerBean;
    }

    /*Pattern state*/
    @Override
    public void enter(StateMachineConcrete context) {
        stampa();
    }

    @Override
    public void stampa() {
        Printer.println(" ");
        Printer.println("-------------- LOGIN --------------");
    }
}
