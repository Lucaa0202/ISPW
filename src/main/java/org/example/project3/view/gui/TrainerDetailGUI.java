package org.example.project3.view.gui;


import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import org.example.project3.controller.CourseListController;
import org.example.project3.controller.TrainerDescriptionController;
import org.example.project3.exceptions.LoadingException;
import org.example.project3.exceptions.NoResultException;
import org.example.project3.utilities.others.mappers.Session;
import org.example.project3.utilities.others.FXMLPathConfig;
import org.example.project3.beans.*;

public class TrainerDetailGUI extends CommonGUI{

    protected TrainerDetailGUI(Session session, FXMLPathConfig fxmlPathConfig) {
        super(session, fxmlPathConfig);
    }

    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField mail;
    @FXML
    private TextField gender;
    @FXML
    private TextField specialization;

    public void trainerDetail(CourseBean courseBean) throws NoResultException {

        TrainerDescriptionController trainerDescriptionController = new TrainerDescriptionController();
        TrainerBean trainer = trainerDescriptionController.trainerDescription(courseBean);
        printTrainerDetail(trainer);

    }


    @FXML
    public void goBack(MouseEvent event){

        try{

            List<CourseBean> coursesBean = new ArrayList<>();
            CourseListController courseListController = new CourseListController();
            courseListController.retrieveCourses(coursesBean);
            goToCourseList(event, coursesBean);

        }catch(NoResultException e){
            throw new LoadingException("Errore durante il caricamento della scena", e);
        }
    }


    public void printTrainerDetail(TrainerBean trainer) throws NoResultException {

        firstName.setText(trainer.getName());
        lastName.setText(trainer.getSurname());
        mail.setText(trainer.getCredentialsBean().getMail());
        gender.setText(trainer.getGender());

        List<String> specs = trainer.getSpecializations();
        if (specs != null && !specs.isEmpty()) {
            specialization.setText(String.join(", ", specs));
        } else {
            specialization.setText("Nessuna specializzazione");
        }
    }
}