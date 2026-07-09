package org.example.project3.view.gui;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.example.project3.controller.ReservationListController;
import org.example.project3.exceptions.NoResultException;
import org.example.project3.utilities.others.mappers.Session;
import org.example.project3.utilities.others.FXMLPathConfig;
import org.example.project3.beans.*;


public class CustomerDetailGUI extends CommonGUI{

    protected CustomerDetailGUI(Session session, FXMLPathConfig fxmlPathConfig) {
        super(session, fxmlPathConfig);
    }

    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField mail;
    @FXML
    private TextField dbirth;
    @FXML
    private TextField injury;
    @FXML
    private TextField gender;

    public void loadCustomerDetail(ReservationBean reservationBean){

        CustomerBean customerBean = reservationBean.getCustomer();
        printCustomerDetail(customerBean);
    }

    public void printCustomerDetail(CustomerBean customerBean)throws NoResultException{

        firstName.setText(customerBean.getName());
        lastName.setText(customerBean.getSurname());
        mail.setText(customerBean.getCredentialsBean().getMail());
        dbirth.setText(String.valueOf(customerBean.getBirthday()));
        gender.setText(customerBean.getGender());
        injury.setText(customerBean.getInjury());

    }

    @FXML
    public void goBack(MouseEvent event){

        try{

            List<ReservationBean> reservationReqBean = new ArrayList<>();

            ReservationListController reservationController = new ReservationListController();
            reservationController.getReservationReq((TrainerBean)session.getUser(), reservationReqBean);

            goToCourseReservationRequest(event, reservationReqBean);

        }catch(NoResultException _){
            throw new NoResultException("Errore recupero richieste");
        }
    }

}
