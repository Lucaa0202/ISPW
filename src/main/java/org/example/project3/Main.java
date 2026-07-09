package org.example.project3;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.example.project3.patterns.state.StateMachineConcrete;
import org.example.project3.utilities.others.FXMLPathConfig;
import org.example.project3.utilities.others.Printer;
import org.example.project3.utilities.others.mappers.MapperRegistration;
import org.example.project3.utilities.others.mappers.Session;
import org.example.project3.view.gui.CustomerHomepageGUI;
import org.example.project3.view.gui.DashboardGUI;

import java.io.IOException;
import java.util.Scanner;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        MapperRegistration.registerMappers();   //Registrazione dei mappers che convertono bean in model e viceversa
        Scanner scanner = new Scanner(System.in);
        boolean validInput = false;
        while (!validInput) {
            try {
                showMenu();
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        graphicInterface(stage);
                        validInput = true;
                        break;
                    case 2:
                        interfaceCLI();
                        validInput = true;
                        break;
                    default:
                        Printer.errorPrint("Scelta non valida");
                }
            } catch (Exception e) {
                Printer.println(e.getMessage());
                scanner.nextLine();
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void graphicInterface(Stage stage) throws IOException {
        FXMLPathConfig fxmlPathConfig = new FXMLPathConfig("/paths.properties");
        Session session = new Session();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPathConfig.getFXMLPath("HOMEPAGE")));
        loader.setControllerFactory(c -> new DashboardGUI(fxmlPathConfig, session)); //Controller homepage
        Parent rootParent = loader.load();
        Scene scene = new Scene(rootParent);
        stage.setTitle("Bodybuild");
        stage.setScene(scene);
        stage.setResizable(false);
        // Add an event filter to the primary stage to handle the ESC key
        stage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                event.consume();
                Platform.exit();
            }
        });
        stage.show();
    }

    public void showMenu() {
        Printer.println(" ");
        Printer.println("-------------- Bodybuild --------------");
        Printer.println("Scegli l'interfaccia da utilizzare:");
        Printer.println("1. Interfaccia grafica");
        Printer.println("2. Interfaccia a riga di comando");
        Printer.print("Scelta: ");
    }

    public  void interfaceCLI(){
        StateMachineConcrete context= new StateMachineConcrete();
        while(context.getCurrentState()!=null) {
            context.goNext();

        }
        Printer.println("Arrivederci");
    }
}