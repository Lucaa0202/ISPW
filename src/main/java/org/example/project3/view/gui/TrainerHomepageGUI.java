package org.example.project3.view.gui;


import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.text.Text;
import org.example.project3.controller.RequestModifyController;
import org.example.project3.controller.ReservationListController;
import org.example.project3.exceptions.NoResultException;
import org.example.project3.utilities.others.mappers.Session;
import org.example.project3.utilities.others.FXMLPathConfig;
import org.example.project3.beans.*;

public class TrainerHomepageGUI extends CommonGUI{

    @FXML
    private Text errorMessage;

    protected TrainerHomepageGUI(Session session, FXMLPathConfig fxmlPathConfig) {
        super(session, fxmlPathConfig);
    }

    @FXML
    protected void retrieveReservationReq(MouseEvent event){
        try{
            List<ReservationBean> reservationReqBean = new ArrayList<>();

            ReservationListController reservationController = new ReservationListController();
            reservationController.getReservationReq((TrainerBean)session.getUser(), reservationReqBean);

            goToCourseReservationRequest(event, reservationReqBean);

        }catch(NoResultException exception){

            errorMessage.setText(exception.getMessage());
            errorMessage.setVisible(true);
        }

    }

    @FXML
    protected void retrieveRequests(MouseEvent event){
        try{
            List<RequestBean> requestBeans = new ArrayList<>();

            RequestModifyController requestModifyController = new RequestModifyController();
            requestModifyController.retrieveRequests((TrainerBean)session.getUser(), requestBeans);

            goToTrainerRequests(event, requestBeans);

        }catch(NoResultException exception){

            errorMessage.setText(exception.getMessage());
            errorMessage.setVisible(true);
        }

    }

}