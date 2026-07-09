package org.example.project3.view.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.project3.exceptions.LoadingException;
import org.example.project3.utilities.others.FXMLPathConfig;
import org.example.project3.utilities.others.mappers.Session;
import org.example.project3.beans.*;


import java.io.IOException;
import java.net.URL;
import java.util.List;

public abstract class CommonGUI {
    protected Session session;
    protected FXMLPathConfig fxmlPathConfig;

    protected CommonGUI(Session session, FXMLPathConfig fxmlPathConfig) {
        this.session = session;
        this.fxmlPathConfig = fxmlPathConfig;
    }

    protected static final String CUSTOMER_HOMEPAGE="CUSTOMER_HOMEPAGE";
    protected static final String CUSTOMER_REGISTRATION="CUSTOMER_REGISTRATION";
    protected static final String HOMEPAGE="HOMEPAGE";
    protected static final String LOGIN="LOGIN";
    protected static final String LOGIN_AND_REGISTER="LOGIN_AND_REGISTER";
    protected static final String REQUEST_MODIFY="REQUEST_MODIFY";
    protected static final String SCHEDULE_DETAILS="SCHEDULE_DETAILS";
    protected static final String TRAINER_DETAIL="TRAINER_DETAIL";
    protected static final String RESERVATION_REQ="RESERVATION_REQ";
    protected static final String COURSE_LIST="COURSE_LIST";
    protected static final String CUSTOMER_DETAILS="CUSTOMER_DETAILS";
    protected static final String TRAINER_HOMEPAGE="TRAINER_HOMEPAGE";
    protected static final String REQUEST_TRAINER="REQUEST_TRAINER";
    protected static final String REQUEST_EXERCISE="REQUEST_EXERCISE";



    @FXML
    protected void goToLogin(MouseEvent event) {
        try {
            String fxmlPath = fxmlPathConfig.getFXMLPath(LOGIN);
            URL resourceUrl = getClass().getResource(fxmlPath);
            FXMLLoader fxmlLoader = new FXMLLoader(resourceUrl);
            fxmlLoader.setControllerFactory( c->new LoginGUI(fxmlPathConfig, session));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }catch(IOException e){
            throw new LoadingException("Errore durante il caricamento della scena", e);
        }
    }

    @FXML
    protected void goToCustomerHomepage(MouseEvent event) {
        try {
            String fxmlPath = fxmlPathConfig.getFXMLPath(CUSTOMER_HOMEPAGE);
            URL resourceUrl = getClass().getResource(fxmlPath);
            FXMLLoader fxmlLoader = new FXMLLoader(resourceUrl);
            fxmlLoader.setControllerFactory( c->new CustomerHomepageGUI(fxmlPathConfig, session));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }catch(IOException e){
            throw new LoadingException("Errore durante il caricamento della scena", e);
        }
    }

    @FXML
    protected void goToRequestSchedule(MouseEvent event) {
        try {
            String fxmlPath = fxmlPathConfig.getFXMLPath(REQUEST_MODIFY);
            URL resourceUrl = getClass().getResource(fxmlPath);
            FXMLLoader fxmlLoader = new FXMLLoader(resourceUrl);
            fxmlLoader.setControllerFactory( c->new RequestScheduleGUI(fxmlPathConfig, session));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }catch(IOException e){
            throw new LoadingException("Errore durante il caricamento della scena", e);
        }
    }

    @FXML
    protected void goToRequestExercise(MouseEvent event) {
        try {
            String fxmlPath = fxmlPathConfig.getFXMLPath(SCHEDULE_DETAILS);
            URL resourceUrl = getClass().getResource(fxmlPath);
            FXMLLoader fxmlLoader = new FXMLLoader(resourceUrl);
            fxmlLoader.setControllerFactory( c->new RequestExerciseGUI(fxmlPathConfig, session));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }catch(IOException e){
            throw new LoadingException("Errore durante il caricamento della scena", e);
        }
    }

    @FXML
    protected void goToRegistration(MouseEvent event) {
        try {
            String fxmlPath = fxmlPathConfig.getFXMLPath(CUSTOMER_REGISTRATION);
            URL resourceUrl = getClass().getResource(fxmlPath);
            FXMLLoader fxmlLoader = new FXMLLoader(resourceUrl);
            fxmlLoader.setControllerFactory( c->new RegistrationGUI(fxmlPathConfig, session));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }catch(IOException e){
            throw new LoadingException("Errore durante il caricamento della scena", e);
        }
    }

    @FXML
    protected void goToLoginAndRegister(MouseEvent event) {
        try {
            String fxmlPath = fxmlPathConfig.getFXMLPath(LOGIN_AND_REGISTER);
            URL resourceUrl = getClass().getResource(fxmlPath);
            FXMLLoader fxmlLoader = new FXMLLoader(resourceUrl);
            fxmlLoader.setControllerFactory( c->new LoginAndRegistrationGUI(fxmlPathConfig, session));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }catch(IOException e){
            throw new LoadingException("Errore durante il caricamento della scena", e);
        }
    }

    @FXML
    protected void goToHomepage(MouseEvent event) {
        try {
            String fxmlPath = fxmlPathConfig.getFXMLPath(HOMEPAGE);
            URL resourceUrl = getClass().getResource(fxmlPath);
            FXMLLoader fxmlLoader = new FXMLLoader(resourceUrl);
            fxmlLoader.setControllerFactory( c->new DashboardGUI(fxmlPathConfig, session));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }catch(IOException e){
            throw new LoadingException("Errore durante il caricamento della scena", e);
        }
    }

    @FXML
    protected void goToTrainerDescription(MouseEvent event, CourseBean coursebean){

    }

    @FXML
    protected void goToCourseList(MouseEvent event, List<CourseBean> coursesBean){
        try {
            String fxmlPath = fxmlPathConfig.getFXMLPath(COURSE_LIST);
            URL resourceUrl = getClass().getResource(fxmlPath);
            FXMLLoader fxmlLoader = new FXMLLoader(resourceUrl);
            fxmlLoader.setControllerFactory(c -> new CourseListGUI(session, fxmlPathConfig));
            Parent root = fxmlLoader.load();
            ((CourseListGUI) fxmlLoader.getController()).printCourseList(coursesBean);
            changeScene(root,event);

        }catch(IOException e){
            throw new LoadingException("Errore durante il caricamento della scena", e);
        }
    }

    @FXML
    protected void goToTrainerDetail(MouseEvent event, CourseBean coursebean){
        try {
            String fxmlPath = fxmlPathConfig.getFXMLPath(TRAINER_DETAIL);
            URL resourceUrl = getClass().getResource(fxmlPath);
            FXMLLoader fxmlLoader = new FXMLLoader(resourceUrl);
            fxmlLoader.setControllerFactory(c -> new TrainerDetailGUI(session, fxmlPathConfig));
            Parent root = fxmlLoader.load();
            ((TrainerDetailGUI) fxmlLoader.getController()).trainerDetail(coursebean);
            changeScene(root, event);

        }catch(IOException e){
            throw new LoadingException("Errore durante il caricamento della scena", e);
        }
    }

    @FXML
    protected void goToCourseReservationRequest(MouseEvent event, List<ReservationBean> reservationReqBean){
        try {
            String fxmlPath = fxmlPathConfig.getFXMLPath(RESERVATION_REQ);
            URL resourceUrl = getClass().getResource(fxmlPath);
            FXMLLoader fxmlLoader = new FXMLLoader(resourceUrl);
            fxmlLoader.setControllerFactory(c -> new ReservationReqGUI(session, fxmlPathConfig));
            Parent root = fxmlLoader.load();
            ((ReservationReqGUI)fxmlLoader.getController()).initializeObserver();
            ((ReservationReqGUI) fxmlLoader.getController()).loadReservationReq(reservationReqBean);
            changeScene(root, event);

        }catch(IOException e){
            throw new LoadingException("Errore durante il caricamento della scena", e);
        }
    }

    @FXML
    protected void goToCustomerDetail(MouseEvent event, ReservationBean reservationBean){
        try {
            String fxmlPath = fxmlPathConfig.getFXMLPath(CUSTOMER_DETAILS);
            URL resourceUrl = getClass().getResource(fxmlPath);
            FXMLLoader fxmlLoader = new FXMLLoader(resourceUrl);
            fxmlLoader.setControllerFactory(c -> new CustomerDetailGUI(session, fxmlPathConfig));
            Parent root = fxmlLoader.load();
            ((CustomerDetailGUI) fxmlLoader.getController()).loadCustomerDetail(reservationBean);
            changeScene(root, event);

        }catch(IOException e){
            throw new LoadingException("Errore durante il caricamento della scena", e);
        }
    }

    @FXML
    protected void goToTrainerHome(MouseEvent event){
        try {
            String fxmlPath = fxmlPathConfig.getFXMLPath(TRAINER_HOMEPAGE);
            URL resourceUrl = getClass().getResource(fxmlPath);
            FXMLLoader fxmlLoader = new FXMLLoader(resourceUrl);
            fxmlLoader.setControllerFactory(c -> new TrainerHomepageGUI(session, fxmlPathConfig));
            Parent root = fxmlLoader.load();
            changeScene(root, event);

        }catch(IOException e){
            throw new LoadingException("Errore durante il caricamento della scena", e);
        }
    }

    @FXML
    protected void goToTrainerRequests(MouseEvent event, List<RequestBean> requestBeans){
        try {
            String fxmlPath = fxmlPathConfig.getFXMLPath(REQUEST_TRAINER);
            URL resourceUrl = getClass().getResource(fxmlPath);
            FXMLLoader fxmlLoader = new FXMLLoader(resourceUrl);
            fxmlLoader.setControllerFactory(c -> new RequestTrainerGUI(session, fxmlPathConfig));
            Parent root = fxmlLoader.load();
            ((RequestTrainerGUI) fxmlLoader.getController()).initializeObserver();
            ((RequestTrainerGUI) fxmlLoader.getController()).loadRequests(requestBeans);
            changeScene(root, event);

        }catch(IOException e){
            throw new LoadingException("Errore durante il caricamento della scena", e);
        }
    }



    //Metodo per cambiare finestra
    protected void changeScene(Parent root, MouseEvent event) {
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

}
