package org.example.project3.view.gui;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import org.example.project3.utilities.others.FXMLPathConfig;
import org.example.project3.utilities.others.mappers.Session;

public class DashboardGUI extends CommonGUI {
    public DashboardGUI(FXMLPathConfig fxmlPathConfig, Session session) { super(session, fxmlPathConfig); }

    @FXML
    public void startButton(MouseEvent event){
        goToLoginAndRegister(event);
    }
}
