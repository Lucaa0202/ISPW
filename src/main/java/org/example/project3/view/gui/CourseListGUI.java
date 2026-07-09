package org.example.project3.view.gui;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.text.Text;
import org.example.project3.beans.*;
import org.example.project3.controller.CourseListController;
import org.example.project3.utilities.others.FXMLPathConfig;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import org.example.project3.utilities.others.mappers.Session;
import javafx.collections.ObservableList;
import org.example.project3.patterns.observer.Observer;

import javafx.scene.control.*;
import java.util.List;

public class CourseListGUI extends CommonGUI implements Observer{

    protected CourseListGUI(Session session, FXMLPathConfig fxmlPathConfig) {
        super(session, fxmlPathConfig);
    }

    @FXML
    private TableView <CourseBean> listCourse;
    @FXML
    private TableColumn<CourseBean, String> courseNameColumn;
    @FXML
    private TableColumn<CourseBean, Void> trainerButtonColumn;
    @FXML
    private TableColumn<CourseBean, Integer> remainingColumn;
    @FXML
    private TableColumn<CourseBean, String> durationColumn;
    @FXML
    private TableColumn<CourseBean, String> dayColumn;
    @FXML
    private TableColumn<CourseBean, String> hourColumn;
    @FXML
    private TableColumn<CourseBean, Void> buttonColumn;
    @FXML
    private Text message;


    private TableCell<CourseBean, Void> createButtonCell(String buttonText){
        return new TableCell<>(){
            private final Button button = createButton(buttonText);
            private Button createButton(String buttonText){
                Button btn = new Button(buttonText);
                btn.setOnMouseClicked(event -> {
                    CourseBean courseBean = getTableView().getItems().get(getIndex());
                    manageReservationReq(courseBean);
                    goToCustomerHomepage(event);
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

    private TableCell<CourseBean, Void> createViewCell(String buttonText) {

        return new TableCell<>() {
            private final Button button = createButton(buttonText);

            private Button createButton(String buttonText) {
                Button btn = new Button(buttonText);
                btn.setOnMouseClicked(event -> {
                    CourseBean courseBean = getTableView().getItems().get(getIndex());
                    goToTrainerDetail(event, courseBean);
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


    @FXML
    public void printCourseList(List<CourseBean> courseBeans){

        //Associazione colonne ai campi
        ObservableList<CourseBean> courseBeanList = FXCollections.observableList(courseBeans);
        courseNameColumn.setCellValueFactory(celldata->new SimpleStringProperty(celldata.getValue().getCourseName()));
        remainingColumn.setCellValueFactory(celldata->new ReadOnlyObjectWrapper<>(celldata.getValue().getRemainingSlots()));
        durationColumn.setCellValueFactory(celldata->new SimpleStringProperty(celldata.getValue().getDuration()));
        dayColumn.setCellValueFactory(celldata->new SimpleStringProperty(celldata.getValue().getDay()));
        hourColumn.setCellValueFactory(celldata->new SimpleStringProperty(celldata.getValue().getHour()));
        buttonColumn.setCellFactory(param -> createButtonCell("Iscriviti"));
        trainerButtonColumn.setCellFactory(param -> createViewCell("Detail"));

        listCourse.setItems(courseBeanList);

    }

    private void manageReservationReq(CourseBean courseBean){

        CustomerBean customerBean = (CustomerBean) session.getUser();
        String day = courseBean.getDay();
        String hour = courseBean.getHour();
        ReservationBean reservationReqBean = new ReservationBean(customerBean, courseBean, day, hour);
        CourseListController courseListController = new CourseListController();
        if(!courseListController.alreadyHasRequest(reservationReqBean)){
            courseListController.sendReservationReq(reservationReqBean);
            update();
        }else{
            message.setText("Hai già inviato una richiesta!");
            message.setVisible(true);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Invio richiesta");
            alert.setHeaderText(null);
            alert.setContentText("Hai già inviato una richiesta!");
            alert.showAndWait();
        }

    }


    @Override
    public void update(){
        message.setText("Richiesta inviata con successo");
        message.setVisible(true);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Invio richiesta");
        alert.setHeaderText(null);
        alert.setContentText("Richiesta inviata con successo");
        alert.showAndWait();
    }


    @FXML
    public void goBack(MouseEvent event){
        goToHomepage(event);
    }
}
