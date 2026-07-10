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
import org.example.project3.view.gui.DashboardGUI;
import org.example.project3.dao.demo.shared.SharedResources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        MapperRegistration.registerMappers();

        // --- 1. SETUP DELL'AMBIENTE ---
        setupEnvironment();

        // --- 2. SCELTA DELL'INTERFACCIA ---
        Scanner scanner = new Scanner(System.in);
        boolean validInput = false;
        while (!validInput) {
            try {
                showMenu();
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consuma l'invio residuo
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
            } catch (Exception _) {
                Printer.println("Errore: inserisci un numero valido.");
                scanner.nextLine(); // Pulisce il buffer dello scanner in caso di input testuale
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void setupEnvironment() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                Printer.errorPrint("Attenzione: file config.properties non trovato! Avvio con impostazioni di default.");
                return;
            }
            properties.load(input);

            // Cerca la chiave "persistence.type" nel file properties
            String persistenceType = properties.getProperty("persistence.type", "mysql");

            if ("demo".equalsIgnoreCase(persistenceType)) {
                SharedResources.getInstance().populateData();
                Printer.println("[SYSTEM] Avvio in modalità DEMO: Dati fittizi caricati in memoria.");
            } else if ("json".equalsIgnoreCase(persistenceType)) {
                Printer.println("[SYSTEM] Avvio in modalità JSON.");
            } else {
                Printer.println("[SYSTEM] Avvio in modalità MySQL.");
            }

        } catch (IOException e) {
            Printer.errorPrint("Errore nella lettura del file di configurazione: " + e.getMessage());
        }
    }

    public void graphicInterface(Stage stage) throws IOException {
        FXMLPathConfig fxmlPathConfig = new FXMLPathConfig("/paths.properties");
        Session session = new Session();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPathConfig.getFXMLPath("HOMEPAGE")));
        loader.setControllerFactory(c -> new DashboardGUI(fxmlPathConfig, session));
        Parent rootParent = loader.load();
        Scene scene = new Scene(rootParent);
        stage.setTitle("Bodybuild");
        stage.setScene(scene);
        stage.setResizable(false);

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

    public void interfaceCLI(){
        StateMachineConcrete context = new StateMachineConcrete();
        while(context.getCurrentState() != null) {
            context.goNext();
        }
        Printer.println("Arrivederci");
    }
}