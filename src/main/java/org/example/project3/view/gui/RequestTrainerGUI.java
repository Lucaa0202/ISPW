package org.example.project3.view.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.project3.beans.ExerciseBean;
import org.example.project3.beans.RequestBean;
import org.example.project3.controller.RequestModifyController;
import org.example.project3.controller.ScheduleController;
import org.example.project3.exceptions.DAOException;
import org.example.project3.exceptions.LoadingException;
import org.example.project3.model.Request;
import org.example.project3.patterns.observer.Observer;
import org.example.project3.patterns.observer.RequestManagerConcreteSubject;
import org.example.project3.utilities.others.FXMLPathConfig;
import org.example.project3.utilities.others.mappers.Session;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RequestTrainerGUI extends CommonGUI implements Observer {
    private final RequestManagerConcreteSubject requestManagerConcreteSubject;
    public RequestTrainerGUI(Session session, FXMLPathConfig fxmlPathConfig) {
        super(session, fxmlPathConfig);
        this.requestManagerConcreteSubject = RequestManagerConcreteSubject.getInstance();}

    @FXML
    TableView<RequestBean> requestList;
    @FXML
    TableColumn<RequestBean, String> customerName;
    @FXML
    TableColumn<RequestBean, String> schedule;
    @FXML
    TableColumn<RequestBean, String> exercise;
    @FXML
    TableColumn<RequestBean, String> reason;
    @FXML
    TableColumn<RequestBean, Void> acceptButton;
    @FXML
    TableColumn<RequestBean, Void> refuseButton;
    @FXML
    Text error;

    private List<RequestBean> requestBeans=new ArrayList<>();

    public void initializeObserver() {requestManagerConcreteSubject.addObserver(this);}

    private final RequestModifyController requestModifyController = new RequestModifyController();
    private final ScheduleController scheduleController = new ScheduleController();

    private TableCell<RequestBean, Void> createButtonCell(String buttonText, boolean isAccept){
        return new TableCell<>() {
            private final Button button = createButton(buttonText, isAccept);
            private Button createButton(String buttonText, boolean isAccept) {
                Button btn = new Button(buttonText);
                btn.setOnMouseClicked(event -> {
                    RequestBean requestBean = getTableView().getItems().get(getIndex());
                    manageRequest(requestBean, isAccept, event);

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

    private void manageRequest(RequestBean requestBean, boolean isAccept, MouseEvent event) {
        if(isAccept){
            completeRequest(event,requestBean);
        }else{
            try {
                requestModifyController.deleteRequest(requestBean);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Rifiuta richiesta");
                alert.setHeaderText(null);
                alert.setContentText("Rimozione effettuata con successo");
                alert.showAndWait();

            }catch(DAOException e){
                error.setText(e.getMessage());
                error.setVisible(true);
            }
        }
    }

    @FXML
    public void loadRequests(List<RequestBean> requests){
        customerName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getScheduleBean().getCustomerBean().getCredentialsBean().getMail()));
        schedule.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getScheduleBean().getName()));
        exercise.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getExerciseBean().getName()));
        reason.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getReason()));


        requestBeans.addAll(requests);
        refreshTableView();

        acceptButton.setCellFactory(param -> createButtonCell("Accetta", true));
        refuseButton.setCellFactory(param -> createButtonCell("Rifiuta", false));

    }


    @Override
    public void update(){
        //restituzione lista aggionrata
        List<Request> request = requestManagerConcreteSubject.getRequests();
        javafx.application.Platform.runLater(() -> {
            // aggiorna lista dei bean
            requestBeans.clear();
            for(Request req : request){
                requestBeans.add(requestModifyController.createRequestBean(req));
            }
            refreshTableView();
        });
    }

    private void refreshTableView() {
        requestList.getItems().clear();
        requestList.getItems().addAll(requestBeans);
        requestList.refresh();
    }

    protected void completeRequest(MouseEvent event, RequestBean requestBean){
        try {
            String fxmlPath = fxmlPathConfig.getFXMLPath(REQUEST_EXERCISE);
            URL resourceUrl = getClass().getResource(fxmlPath);
            FXMLLoader fxmlLoader = new FXMLLoader(resourceUrl);
            fxmlLoader.setControllerFactory(c -> new ModifyExerciseGUI( fxmlPathConfig,session));
            Parent root = fxmlLoader.load();
            List<ExerciseBean> exerciseBeansParam = new ArrayList<>();
            scheduleController.retriveAllExercises(requestBean,exerciseBeansParam);
            ((ModifyExerciseGUI)fxmlLoader.getController()).loadExercises(requestBean,exerciseBeansParam);
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        }catch(IOException e){
            throw new LoadingException("Errore durante il caricamento della scena", e);
        }
    }

}
