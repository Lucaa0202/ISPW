package org.example.project3.dao.factory;


import org.example.project3.dao.*;
import org.example.project3.utilities.others.Printer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class DAOFactory {

    private static DAOFactory instance;
    private static final String CONFIG_FILE = "config.properties";
    private static final Properties properties = new Properties();
    private static final String PERSISTENCE_TYPE = "persistence.type";

    // Istanze singole dei DAO
    protected CredentialsDAO credentialsDAO;
    protected CustomerDAO customerDAO;
    protected ExerciseDAO exerciseDAO;
    protected RequestDAO requestDAO;
    protected ScheduleDAO scheduleDAO;
    protected SubscriptionDAO subscriptionDAO;
    protected TrainerDAO trainerDAO;
    protected CourseDAO courseDAO;
    protected ReservationDAO reservationDAO;

    protected DAOFactory() {}

    private static void loadProperties() {
        try (InputStream input = DAOFactory.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new IOException("Properties file not found: " + CONFIG_FILE);
            }
            properties.load(input);
        } catch (IOException e) {
            Printer.errorPrint("Error loading properties file: " + e.getMessage());
        }
    }

    public static synchronized DAOFactory getInstance() {
        if (instance == null) {
            loadProperties();
            String type = properties.getProperty(PERSISTENCE_TYPE);

            if (type == null) {
                throw new IllegalArgumentException("Persistence type not found in properties file.");
            }

            switch (type) {
                case "mysql":
                    instance = new SqlDAOFactory();
                    break;
                case "demo":
                    instance = new DemoDAOFactory();
                    break;
                case "csv":
                    instance = new CsvDAOFactory();
                    break;
                default:
                    throw new IllegalArgumentException("Tipo di DAO non valido: " + type);
            }
        }
        return instance;
    }

    // ====== METODI ASTRATTI DI CREAZIONE ======
    protected abstract CredentialsDAO createCredentialsDAO();
    protected abstract CustomerDAO createCustomerDAO();
    protected abstract ExerciseDAO createExerciseDAO();
    protected abstract RequestDAO createRequestDAO();
    protected abstract ScheduleDAO createScheduleDAO();
    protected abstract SubscriptionDAO createSubscriptionDAO();
    protected abstract TrainerDAO createTrainerDAO();
    protected abstract CourseDAO createCourseDAO();
    protected abstract ReservationDAO createReservationDAO();

    // ====== GETTER  ======
    public CredentialsDAO getCredentialsDAO() {
        if (credentialsDAO == null) credentialsDAO = createCredentialsDAO();
        return credentialsDAO;
    }

    public CustomerDAO getCustomerDAO() {
        if (customerDAO == null) customerDAO = createCustomerDAO();
        return customerDAO;
    }

    public ExerciseDAO getExerciseDAO() {
        if (exerciseDAO == null) exerciseDAO = createExerciseDAO();
        return exerciseDAO;
    }

    public RequestDAO getRequestDAO() {
        if (requestDAO == null) requestDAO = createRequestDAO();
        return requestDAO;
    }

    public ScheduleDAO getScheduleDAO() {
        if (scheduleDAO == null) scheduleDAO = createScheduleDAO();
        return scheduleDAO;
    }

    public SubscriptionDAO getSubscriptionDAO() {
        if (subscriptionDAO == null) subscriptionDAO = createSubscriptionDAO();
        return subscriptionDAO;
    }

    public TrainerDAO getTrainerDAO() {
        if (trainerDAO == null) trainerDAO = createTrainerDAO();
        return trainerDAO;
    }

    public CourseDAO getCourseDAO() {
        if (courseDAO == null) courseDAO = createCourseDAO();
        return courseDAO;
    }

    public ReservationDAO getReservationDAO() {
        if (reservationDAO == null) reservationDAO = createReservationDAO();
        return reservationDAO;
    }
}
