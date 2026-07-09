package org.example.project3.view.gui;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import org.example.project3.beans.ExerciseBean;
import org.example.project3.beans.RequestBean;
import org.example.project3.beans.ScheduleBean;
import org.example.project3.controller.RequestModifyController;
import org.example.project3.controller.ScheduleController;
import org.example.project3.exceptions.DAOException;
import org.example.project3.exceptions.EmptyFieldException;
import org.example.project3.exceptions.NoResultException;
import org.example.project3.patterns.observer.Observer;
import org.example.project3.patterns.observer.RequestManagerConcreteSubject;
import org.example.project3.utilities.others.FXMLPathConfig;
import org.example.project3.utilities.others.Printer;
import org.example.project3.utilities.others.mappers.Session;

import java.util.ArrayList;
import java.util.List;

public class RequestExerciseGUI extends CommonGUI implements Observer {

    private final RequestManagerConcreteSubject requestManagerConcreteSubject;
    public RequestExerciseGUI(FXMLPathConfig fxmlPathConfig, Session session) { super(session, fxmlPathConfig);
        this.requestManagerConcreteSubject = RequestManagerConcreteSubject.getInstance();}

    @FXML
    Button indietro;

    @FXML
    ImageView cancellaRicerca;

    @FXML
    TextField ricerca;

    @FXML
    Button invia;

    @FXML
    Button cerca;

    @FXML
    TextField motivazione;

    @FXML
    TableView<ExerciseBean> exerciseChoice;

    @FXML
    TableColumn<ExerciseBean, Long> id;

    @FXML
    TableColumn<ExerciseBean, String> name;

    @FXML
    TableColumn<ExerciseBean, String> description;

    @FXML
    TableColumn<ExerciseBean, Integer> series;

    @FXML
    TableColumn<ExerciseBean, Integer> reps;

    @FXML
    TableColumn<ExerciseBean, String> restTime;

    @FXML
    TableColumn<ExerciseBean, Void> check;

    @FXML
    Text error;

    public void initializeObserver() {requestManagerConcreteSubject.addObserver(this);}

    private final RequestModifyController requestModifyController = new RequestModifyController();

    private final ToggleGroup rowToggleGroup = new ToggleGroup();

    private RequestBean requestBean;

    private List<ExerciseBean> exerciseList= new ArrayList<>();

    private class MyRadioButtonCell extends TableCell<ExerciseBean, Void> {
        private final RadioButton radioButton = new RadioButton();

        public MyRadioButtonCell() { // Ora è un costruttore esplicito valido
            super(); // Chiama il costruttore della superclasse

            radioButton.setToggleGroup(rowToggleGroup);

            radioButton.setOnAction(e -> {
                if (getIndex() >= 0 && getIndex() < getTableView().getItems().size()) {
                    ExerciseBean selected = getTableView().getItems().get(getIndex());
                    requestBean.setExercise(selected);
                    motivazione.setVisible(true);
                    invia.setVisible(true);
                }
            });
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || getIndex() >= getTableView().getItems().size()) {
                setGraphic(null);
            } else {
                ExerciseBean current = getTableView().getItems().get(getIndex());

                // Se questa riga è quella selezionata, evidenziala
                radioButton.setSelected((requestBean!=null)&&(current.equals(requestBean.getExerciseBean())));

                setGraphic(radioButton);
            }
        }
    }

    private TableCell<ExerciseBean, Void> createButtonCell() {
        return new MyRadioButtonCell(); // Istanzia la classe nominata
    }

    @FXML
    public void loadExercises(RequestBean request) {
        error.setVisible(false);
        List<ExerciseBean> exerciseBeansParam = request.getScheduleBean().getExercisesBean();
        if(this.requestBean==null&&this.exerciseList.isEmpty()){
            this.requestBean = request;
            this.exerciseList.addAll(exerciseBeansParam);
        }
        //Imposto valori della tabella
        id.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));
        name.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        description.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        series.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getNumberSeries()));
        reps.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getNumberReps()));
        restTime.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getRestTime().getId())+" secondi"));
        check.setCellFactory(param -> createButtonCell());
        exerciseChoice.getItems().clear();
        exerciseChoice.getItems().addAll(exerciseBeansParam);
    }

    @FXML
    public void send(MouseEvent event) throws EmptyFieldException {
        try{
            validateFields();
            requestBean.setReason(motivazione.getText());
            if(!requestModifyController.hasAlreadySentARequest(requestBean.getScheduleBean())){
                requestModifyController.sendRequest(requestBean);
                update();
                goToCustomerHomepage(event);
            }else{
                Printer.errorPrint("Hai già inviato una richiesta");
                error.setText("Hai già inviato una richiesta per questa scheda!");
                error.setVisible(true);
            }
        }catch(EmptyFieldException | DAOException e){
            error.setText(e.getMessage());
            error.setVisible(true);
        }

    }

    @FXML
    public void cancelSearch(){
        this.requestBean.getScheduleBean().setExercisesBean(exerciseList);
        loadExercises(this.requestBean);
        ricerca.setText("");
        cancellaRicerca.setVisible(false);
    }


    @Override
    public void update() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Invio richiesta");
        alert.setHeaderText(null);
        alert.setContentText("Richiesta inviata con successo");
        alert.showAndWait();
    }

    private void validateFields() throws EmptyFieldException {
        if (motivazione.getText().isEmpty()) {
            throw new EmptyFieldException("Inserisci una motivazione!");
        }
    }

    private ScheduleController scheduleController = new ScheduleController();

    @FXML
    private void searchButton() {
        String searchText = ricerca.getText();
        cancellaRicerca.setVisible(true);

        if (searchText != null && !searchText.trim().isEmpty()) {
            // Il campo non è vuoto, esegui la ricerca
            List<ExerciseBean> exercisesTemp = new ArrayList<>();
            try{
                scheduleController.searchExercises(exercisesTemp, searchText,this.requestBean.getScheduleBean());
                RequestBean requestTemp= new RequestBean(new ScheduleBean(this.requestBean.getScheduleBean().getId(),exercisesTemp));
                loadExercises(requestTemp);
            }catch(NoResultException|DAOException e){
                error.setText(e.getMessage());
                error.setVisible(true);
            }

        } else {
            loadExercises(this.requestBean);
        }
    }


}
