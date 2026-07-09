package org.example.project3.patterns.factory;


import org.example.project3.dao.*;
import org.example.project3.dao.demo.*;
import org.example.project3.dao.full.json.CredentialsDAOJSON;
import org.example.project3.dao.full.json.CustomerDAOJSON;
import org.example.project3.dao.full.json.TrainerDAOJSON;
import org.example.project3.dao.full.sql.*;
import org.example.project3.utilities.others.Printer;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.function.Supplier;

public class FactoryDAO {
    private FactoryDAO(){}
    private static final String CONFIG_FILE = "config.properties";
    private static final Properties properties = new Properties();
    private static final String PERSISTENCE_TYPE = "persistence.type";
    private static CredentialsDAO credentialsDAO;
    private static CustomerDAO customerDAO;
    private static ExerciseDAO exerciseDAO;
    private static RequestDAO requestDAO;
    private static ScheduleDAO scheduleDAO;
    private static SubscriptionDAO subscriptionDAO;
    private static TrainerDAO trainerDAO;
    private static CourseDAO courseDAO;
    private static ReservationDAO reservationDAO;


    // Caricamento delle proprietà una sola volta
    private static void loadProperties() {
        try (InputStream input = FactoryDAO.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new IOException("Properties file not found: " + CONFIG_FILE);
            }
            properties.load(input);
        } catch (IOException e) {
            Printer.errorPrint("Error loading properties file: " + e.getMessage());
        }
    }

    // Verifica che il tipo di persistenza sia valido
    private static String getPersistenceType() {
        if(properties.isEmpty()){
            loadProperties();
        }
        String type = properties.getProperty(PERSISTENCE_TYPE);
        if (type == null) {
            throw new IllegalArgumentException("Persistence type not found in properties file.");
        }
        return type;
    }

    // Metodo generico per creare DAO
    private static <T> T createDAO(String type, Supplier<T> mysqlSupplier, Supplier<T> demoSupplier, Supplier<T> jsonSupplier) {
        return switch (type) {
            case "mysql" -> mysqlSupplier.get();
            case "demo" -> demoSupplier.get();
            case "json" -> {
                if (jsonSupplier == null) {
                    throw new IllegalArgumentException("DAO JSON non supportato.");
                }
                yield jsonSupplier.get();
            }
            default -> throw new IllegalArgumentException("Tipo di DAO non valido: " + type);
        };
    }

    // Getters per i DAO
    // Sincronizzazione per evitare problemi di concorrenza
    public static synchronized CredentialsDAO getDAO() {
        if (credentialsDAO == null) {
            credentialsDAO = createDAO(
                    getPersistenceType(),
                    CredentialsDAOSQL::new,
                    CredentialsDAOP::new,
                    CredentialsDAOJSON::getInstance
            );
        }
        return credentialsDAO;
    }

    public static synchronized CustomerDAO getCustomerDAO() {
        if (customerDAO == null) {
            customerDAO = createDAO(
                    getPersistenceType(),
                    CustomerDAOSQL::new,
                    CustomerDAOP::new,
                    CustomerDAOJSON::getInstance
            );
        }
        return customerDAO;
    }

    public static synchronized ExerciseDAO getExerciseDAO() {
        if (exerciseDAO == null) {
            exerciseDAO = createDAO(
                    getPersistenceType(),
                    ExerciseDAOSQL::new,
                    ExerciseDAOP::new,
                    null
            );
        }
        return exerciseDAO;
    }

    public static synchronized RequestDAO getRequestDAO() {
        if (requestDAO == null) {
            requestDAO = createDAO(
                    getPersistenceType(),
                    RequestDAOSQL::new,
                    RequestDAOP::new,
                    null
            );
        }
        return requestDAO;
    }

    public static synchronized ScheduleDAO getScheduleDAO() {
        if (scheduleDAO == null) {
            scheduleDAO = createDAO(
                    getPersistenceType(),
                    ScheduleDAOSQL::new,
                    ScheduleDAOP::new,
                    null
            );
        }
        return scheduleDAO;
    }

    public static synchronized SubscriptionDAO getSubscriptionDAO() {
        if (subscriptionDAO == null) {
            subscriptionDAO = createDAO(
                    getPersistenceType(),
                    SubscriptionDAOSQL::new,
                    SubscriptionDAOP::new,
                    null
            );
        }
        return subscriptionDAO;
    }

    public static synchronized TrainerDAO getTrainerDAO() {
        if (trainerDAO == null) {
            trainerDAO = createDAO(
                    getPersistenceType(),
                    TrainerDAOSQL::new,
                    TrainerDAOP::new,
                    TrainerDAOJSON::getInstance
            );
        }
        return trainerDAO;
    }

    public static synchronized CourseDAO getCourseDAO() {
        if (courseDAO == null) {
            courseDAO = createDAO(
                    getPersistenceType(),
                    CourseDAOSQL::new,
                    CourseDAOP::new,
                    null
            );
        }
        return courseDAO;
    }

    public static synchronized ReservationDAO getReservationDAO() {
        if (reservationDAO == null) {
            reservationDAO = createDAO(
                    getPersistenceType(),
                    ReservationDAOSQL::new,
                    ReservationDAOP::new,
                    null
            );
        }
        return reservationDAO;
    }

}
