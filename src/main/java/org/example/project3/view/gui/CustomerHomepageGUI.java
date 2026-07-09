package org.example.project3.view.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.project3.beans.CourseBean;
import org.example.project3.beans.CustomerBean;
import org.example.project3.beans.ScheduleBean;
import org.example.project3.controller.CourseListController;
import org.example.project3.controller.ScheduleController;
import org.example.project3.exceptions.LoadingException;
import org.example.project3.exceptions.NoResultException;
import org.example.project3.utilities.others.FXMLPathConfig;
import org.example.project3.utilities.others.mappers.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomerHomepageGUI extends CommonGUI{

    public CustomerHomepageGUI(FXMLPathConfig fxmlPathConfig, Session session) { super(session, fxmlPathConfig); }


    @FXML
    ButtonBar richiediModifica;

    @FXML
    Button modifica;

    @FXML
    Button prenota;

    @FXML
    public void seeSchedules(MouseEvent event) {
        try{
            ArrayList<ScheduleBean> scheduleBeans = new ArrayList<>();
            ScheduleController scheduleController = new ScheduleController();
            scheduleController.retriveScheduleDetails((CustomerBean)session.getUser(), scheduleBeans);
            goToRequest(scheduleBeans, event);
        }catch(NoResultException _){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Nessuna scheda");
            alert.setHeaderText(null);
            alert.setContentText("Nessuna scheda presente");
            alert.showAndWait();
        }

    }

    private void goToRequest(List<ScheduleBean> scheduleBeans, MouseEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPathConfig.getFXMLPath(REQUEST_MODIFY)));
            loader.setControllerFactory(c -> new RequestScheduleGUI(fxmlPathConfig, session));
            Parent root = loader.load();
            ((RequestScheduleGUI)loader.getController()).loadSchedule(scheduleBeans);
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new LoadingException("Errore durante il caricamento della scena", e);
        }
    }

    @FXML
    protected void retrieveCourse(MouseEvent event){
        try{
            List<CourseBean> coursesBean = new ArrayList<>();
            CourseListController courseListController = new CourseListController();
            courseListController.retrieveCourses(coursesBean);
            goToCourseList(event, coursesBean);

        }catch(NoResultException e){
            throw new LoadingException("Errore durante il caricamento della scena", e);
        }

    }
}
