package org.example.project3.view.gui;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import org.example.project3.beans.CredentialsBean;
import org.example.project3.beans.CustomerBean;
import org.example.project3.beans.TrainerBean;
import org.example.project3.controller.LoginController;
import org.example.project3.exceptions.EmptyFieldException;
import org.example.project3.exceptions.LoadingException;
import org.example.project3.exceptions.NoResultException;
import org.example.project3.exceptions.WrongEmailOrPasswordException;
import org.example.project3.utilities.enums.Role;
import org.example.project3.utilities.others.FXMLPathConfig;
import org.example.project3.utilities.others.mappers.Session;

import java.io.IOException;

public class LoginGUI extends CommonGUI{
    public LoginGUI(FXMLPathConfig fxmlPathConfig, Session session) { super(session, fxmlPathConfig); }

    private final LoginController loginController=new LoginController();
    @FXML
    TextField email;
    @FXML
    PasswordField password;
    @FXML
    Text error;

    @FXML
    private void setCredentials(MouseEvent event) throws IOException {
        error.setVisible(false);
        try{
            validateFields();
            CredentialsBean credentialsBean= new CredentialsBean(email.getText(), password.getText(), null);
            loginController.login(credentialsBean);
            if(credentialsBean.getRole()==null){
                throw new WrongEmailOrPasswordException("Mail o password errati!");
            }
            if(credentialsBean.getRole().equals(Role.CLIENT)){
                CustomerBean customerBean= new CustomerBean(credentialsBean);
                loginController.retrieveCustomer(customerBean);
                session.setUser(customerBean);
                goToCustomerHomepage(event);
            }
            else if(credentialsBean.getRole().equals(Role.TRAINER)){
                TrainerBean trainerBean= new TrainerBean(credentialsBean);
                loginController.retrieveTrainer(trainerBean);
                session.setUser(trainerBean);

                goToTrainerHome(event);
            }

        }catch(WrongEmailOrPasswordException | EmptyFieldException e){
            error.setText(e.getMessage());
            error.setVisible(true);
        }catch(NoResultException e){
            throw new LoadingException("Errore durante il caricamento della scena", e);
        }
    }

    private void validateFields() throws EmptyFieldException {
        if (email.getText().isEmpty() || password.getText().isEmpty()) {
            throw new EmptyFieldException("Compila tutti campi.");
        }
    }


}
