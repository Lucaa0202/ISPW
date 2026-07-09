package org.example.project3.view.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.project3.beans.CustomerBean;
import org.example.project3.beans.RequestBean;
import org.example.project3.beans.ScheduleBean;
import org.example.project3.controller.RequestModifyController;
import org.example.project3.controller.ScheduleController;
import org.example.project3.exceptions.DAOException;
import org.example.project3.exceptions.LoadingException;
import org.example.project3.exceptions.NoResultException;
import org.example.project3.utilities.others.FXMLPathConfig;
import org.example.project3.utilities.others.mappers.Session;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RequestScheduleGUI extends CommonGUI{

    public RequestScheduleGUI(FXMLPathConfig fxmlPathConfig, Session session) { super(session, fxmlPathConfig); }

    @FXML
    Button indietro;

    @FXML
    ImageView cancellaRicerca;

    @FXML
    TextField ricerca;

    @FXML
    Button cerca;

    @FXML
    TableView<ScheduleBean> scheduleChoice;

    @FXML
    TableColumn<ScheduleBean, String> name;

    @FXML
    TableColumn<ScheduleBean, String> trainer;

    @FXML
    TableColumn<ScheduleBean, Void> seleziona;

    @FXML
    Text error;

    private List<ScheduleBean> originalSchedules = new ArrayList<>();

    private RequestModifyController requestModifyController= new RequestModifyController();
    private ScheduleController scheduleController = new ScheduleController();

    private TableCell<ScheduleBean, Void> createButtonCell(String buttonText) {
        return new TableCell<>() {
            private final Button button = createButton(buttonText);
            private Button createButton(String buttonText) {
                Button btn = new Button(buttonText);
                btn.setOnMouseClicked(event -> {
                    try {
                        ScheduleBean scheduleBean = getTableView().getItems().get(getIndex());
                        scheduleController.retriveExercises(scheduleBean);
                        scheduleController.retrieveTrainer(scheduleBean);
                        RequestBean requestBean = new RequestBean(scheduleBean);
                        completeRequest(requestBean, event);
                    }catch(DAOException _){
                        error.setText("Errore nel DAO");
                        error.setVisible(true);
                    }
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
    public void loadSchedule(List<ScheduleBean> scheduleBeansParam){
        error.setVisible(false);
        if(this.originalSchedules.isEmpty()&&!scheduleBeansParam.isEmpty()){
            this.originalSchedules.addAll(scheduleBeansParam);
        }
        //Imposto valori della tabella
        name.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        trainer.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTrainerBean().getCredentialsBean().getMail()));
        seleziona.setCellFactory(param -> createButtonCell("Seleziona"));
        scheduleChoice.getItems().clear();
        scheduleChoice.getItems().addAll(scheduleBeansParam);

    }

    private void completeRequest(RequestBean requestBean, MouseEvent event){
        try {
            String fxmlPath = fxmlPathConfig.getFXMLPath(SCHEDULE_DETAILS);
            URL resourceUrl = getClass().getResource(fxmlPath);
            FXMLLoader loader = new FXMLLoader(resourceUrl);
            loader.setControllerFactory(c -> new RequestExerciseGUI(fxmlPathConfig, session));
            Parent root = loader.load();
            ((RequestExerciseGUI)loader.getController()).loadExercises(requestBean);
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new LoadingException("Errore durante il caricamento della scena", e);
        }
    }

    @FXML
    private void cancelSearch(){
        loadSchedule(originalSchedules);
        ricerca.setText("");
        cancellaRicerca.setVisible(false);

    }

    @FXML
    private void searchButton() {
        String searchText = ricerca.getText();
        cancellaRicerca.setVisible(true);

        if (searchText != null && !searchText.trim().isEmpty()) {
            // Il campo non è vuoto, esegui la ricerca
            List<ScheduleBean> schedulesTemp = new ArrayList<>();
            try {
                scheduleController.searchSchedules(schedulesTemp, searchText, (CustomerBean) session.getUser());
                loadSchedule(schedulesTemp);
            }catch (NoResultException | DAOException e){
                error.setText(e.getMessage());
                error.setVisible(true);
            }

        } else {
            loadSchedule(originalSchedules);
        }
    }

}
