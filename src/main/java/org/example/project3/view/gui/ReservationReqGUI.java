package org.example.project3.view.gui;

import javafx.beans.property.SimpleStringProperty;
import org.example.project3.beans.*;
import org.example.project3.controller.CourseListController;
import org.example.project3.controller.ReservationReqApplicationController;
import org.example.project3.model.Reservation;
import org.example.project3.patterns.observer.ReservationManagerConcreteSubject;
import org.example.project3.utilities.others.FXMLPathConfig;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import org.example.project3.utilities.others.mappers.Session;
import org.example.project3.patterns.observer.Observer;
import javafx.scene.control.Button;


import javafx.scene.control.*;
import java.util.List;
import java.util.ArrayList;

public class ReservationReqGUI extends CommonGUI implements Observer {

    private List<ReservationBean> reservationBeans = new ArrayList<>();
    private final ReservationReqApplicationController reservationReqApplicationController = new ReservationReqApplicationController();
    private final ReservationManagerConcreteSubject reservationManagerConcreteSubject;

    protected ReservationReqGUI(Session session, FXMLPathConfig fxmlPathConfig) {

        super(session, fxmlPathConfig);
        this.reservationManagerConcreteSubject = ReservationManagerConcreteSubject.getInstance();
    }

    // name course date hour detail accept refuse

    @FXML
    private TableView<ReservationBean> reservationList;
    @FXML
    private TableColumn<ReservationBean, String> customerName;
    @FXML
    private TableColumn<ReservationBean, String> courseName;
    @FXML
    private TableColumn<ReservationBean, String> date;
    @FXML
    private TableColumn<ReservationBean, String> hour;
    @FXML
    private TableColumn<ReservationBean, Void> detailButton;
    @FXML
    private TableColumn<ReservationBean, Void> acceptButton;
    @FXML
    private TableColumn<ReservationBean, Void> refuseButton;
    @FXML
    private Label errorMessage;


    // Metodo per inizializzare l'observer
    public void initializeObserver() {
        reservationManagerConcreteSubject.addObserver(this); // Aggiungi l'osservatore
    }




    private TableCell<ReservationBean, Void> createButtonCell(String buttonText, boolean isAccept){
        return new TableCell<>() {
            private final Button button = createButton(buttonText, isAccept);
            private Button createButton(String buttonText, boolean isAccept) {
                Button btn = new Button(buttonText);
                btn.setOnMouseClicked(event -> {
                    ReservationBean reservationBean = getTableView().getItems().get(getIndex());
                    manageReservationReq(reservationBean, isAccept);
                });
                return btn;
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(button);
                }
            }
        };
    }

    private TableCell<ReservationBean, Void> createDetailCell(String buttonText) {
        return new TableCell<>() {
            private final Button button = createButton(buttonText);

            private Button createButton(String buttonText) {
                Button btn = new Button(buttonText);
                btn.setOnMouseClicked(event -> {
                    ReservationBean reservationBean = getTableView().getItems().get(getIndex());
                    goToCustomerDetail(event, reservationBean);
                });
                return btn;
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(button);
                }
            }
        };
    }


    private void manageReservationReq(ReservationBean reservationBean, boolean isAccept) {

        //controller da implementare
        reservationReqApplicationController.deleteReservationReq(reservationBean);
        if(isAccept){
            reservationReqApplicationController.addReservation(reservationBean);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Accetta richiesta");
            alert.setHeaderText(null);
            alert.setContentText("Operazione effettuata con successo");
            alert.showAndWait();
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Rifiuta richiesta");
            alert.setHeaderText(null);
            alert.setContentText("Rimozione effettuata con successo");
            alert.showAndWait();
        }
    }


    @FXML
    public void loadReservationReq(List<ReservationBean> reservations){
        customerName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomer().getName()));
        courseName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCourse().getCourseName()));
        date.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getDay()));
        hour.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHour()));

        reservationBeans.addAll(reservations);
        refreshTableView();

        detailButton.setCellFactory(param -> createDetailCell("vedi"));
        acceptButton.setCellFactory(param -> createButtonCell("Accetta", true));
        refuseButton.setCellFactory(param -> createButtonCell("Rifiuta", false));

    }


    @Override
    public void update(){
        //restituzione lista aggionrata
        List<Reservation> reservations = reservationManagerConcreteSubject.getReservationsReq();
        // aggiorna lista dei vean
        reservationBeans.clear();
        for(Reservation reservation : reservations){
            reservationBeans.add(reservationReqApplicationController.createReservationBean(reservation));
        }
        refreshTableView();
    }


    @FXML
    public void goBack(MouseEvent event){

        List<CourseBean> coursesBean = new ArrayList<>();

        CourseListController courseListController = new CourseListController();
        courseListController.retrieveCourses(coursesBean);
        goToCourseList(event, coursesBean);

    }

    private void refreshTableView() {
        reservationList.getItems().clear();
        reservationList.getItems().addAll(reservationBeans);
    }

}
