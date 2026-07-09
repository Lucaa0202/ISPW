package test;

import org.example.project3.dao.*;
import org.example.project3.exceptions.DAOException;
import org.example.project3.exceptions.LoginAndRegistrationException;
import org.example.project3.exceptions.MailAlreadyExistsException;
import org.example.project3.exceptions.WrongEmailOrPasswordException;
import org.example.project3.model.*;
import org.example.project3.patterns.factory.FactoryDAO;
import org.example.project3.utilities.enums.RestTime;
import org.example.project3.utilities.enums.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

class TestRequest {
    //Belforte Luca, matricola 0312557

    //Parametri il test di alcuni metodi
    private String passwordCustomer;
    private String passwordTrainer;
    private String testEmailCustomer;
    private String testEmailTrainer;
    private final LocalDate testBirthday = LocalDate.now();

    private static final String TEST_NAME = "TestName";
    private static final String TEST_SURNAME = "TestSurname";
    private static final String TEST_GENDER = "Gender";
    private static final boolean TEST_ONLINE = true;

    //DAO
    private CustomerDAO customerDAO;
    private CredentialsDAO credentialsDAO;
    private ExerciseDAO exerciseDAO;
    private ScheduleDAO scheduleDAO;
    private TrainerDAO trainerDAO;
    private RequestDAO requestDAO;

    //Cliente di test
    private Customer testCustomer;
    private Trainer testTrainer;
    private List<Exercise> testExercises;
    private List<Schedule> testSchedules;
    private Request testRequest;

    //Azione che viene svolta prima di ogni test
    @BeforeEach
    void setUp() throws LoginAndRegistrationException {
        // genero una password
        passwordCustomer = generatePassword();
        passwordTrainer = generatePassword();
        //test email
        testEmailCustomer = generateTestEmail();
        testEmailTrainer = generateTestEmail();

        // Inizializziamo i DAO una sola volta per evitare chiamate ripetute alla Factory
        customerDAO = FactoryDAO.getCustomerDAO();
        credentialsDAO = FactoryDAO.getDAO();
        exerciseDAO = FactoryDAO.getExerciseDAO();
        scheduleDAO = FactoryDAO.getScheduleDAO();
        trainerDAO = FactoryDAO.getTrainerDAO();
        requestDAO = FactoryDAO.getRequestDAO();
        // Creo un cliente di test
        testCustomer = createCustomer();
        testTrainer = createTrainer();
        testExercises = createExercises();
        testSchedules = createSchedules();
        testRequest = createRequest();
        // Registriamo il paziente
        registerCustomer();
        registerTrainer();
    }

    // Azione che viene svolta dopo ogni test
    @AfterEach
    void tearDown() {
        try {
            if (testCustomer != null) {
                requestDAO.deleteRequest(testRequest);
                for (Exercise exercise : testExercises) {
                    exerciseDAO.deleteExercise(exercise);
                }
                for (Schedule s : testSchedules) {
                    scheduleDAO.deleteSchedule(s);
                }
                customerDAO.removeCustomer(testCustomer);
                trainerDAO.removeTrainer(testTrainer);
            }
        } catch(DAOException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    // test per assicurarmi che un cliente dopo essersi registrato può svolgere il login
    @Test
    void testCredential() {
        Credentials credentials = new Credentials(testEmailCustomer, passwordCustomer, Role.CLIENT);
        Assertions.assertDoesNotThrow(() -> credentialsDAO.login(credentials), "Errore durante il login");
    }

    //Test per il corretto recupero delle schede
    @Test
    void testSchedules() {
        Assertions.assertDoesNotThrow(() -> {
            registerSchedules();
            registerExercises();
            List<Schedule> schedules = new ArrayList<>();
            scheduleDAO.retrieveSchedule(testCustomer, schedules);

            for (int i = 0; i < schedules.size(); i++) {
                Schedule actual = schedules.get(i);
                Schedule expected = testSchedules.get(i);

                Assertions.assertEquals(expected.getId(), actual.getId(),
                        "Gli ID delle schede non corrispondono.");
                Assertions.assertEquals(expected.getName(), actual.getName(),
                        "I nomi delle schede non corrispondono.");
                Assertions.assertEquals(expected.getCustomer().getCredentials().getMail(), actual.getCustomer().getCredentials().getMail(),
                        "I clienti delle schede non corrispondono.");
                Assertions.assertEquals(expected.getTrainer().getCredentials().getMail(), actual.getTrainer().getCredentials().getMail(),
                        "I trainer delle schede non corrispondono.");
            }
        }, "Errore nel recupero delle schede");
    }

    //Test per la corretta registrazione della richiesta
    @Test
    void testRequest() {
        Assertions.assertDoesNotThrow(() -> {
            registerSchedules();
            registerExercises();
            requestDAO.sendRequest(testRequest);

            Assertions.assertTrue(requestDAO.hasAlreadySentRequest(testRequest),
                    "La richiesta non è stata registrata correttamente.");
        }, "Errore durante il test della richiesta");
    }

    private Request createRequest() {
        Random random = new Random();
        int baseId = random.nextInt(1000000);
        return new Request(baseId + 20, testSchedules.get(0), testExercises.get(0), "i don't like", LocalDateTime.now());
    }

    private List<Schedule> createSchedules(){
        Random random = new Random();
        int baseId = random.nextInt(1000000);
        List<Exercise> exercisesForScheduleA = new ArrayList<>();
        exercisesForScheduleA.add(testExercises.get(0));
        exercisesForScheduleA.add(testExercises.get(1));
        Schedule scheduleA = new Schedule(baseId + 1, "Beginner Full Body", testCustomer, testTrainer, exercisesForScheduleA);
        List<Exercise> exercisesForScheduleB = new ArrayList<>();
        exercisesForScheduleB.add(testExercises.get(2));
        Schedule scheduleB = new Schedule(baseId + 2, "Cardio Focus", testCustomer, testTrainer, exercisesForScheduleB);
        List<Exercise> exercisesForScheduleC = new ArrayList<>();
        exercisesForScheduleC.add(testExercises.get(0));
        exercisesForScheduleC.add(testExercises.get(1));
        exercisesForScheduleC.add(testExercises.get(2));
        Schedule scheduleC = new Schedule(baseId + 3, "Advanced Strength", testCustomer, testTrainer, exercisesForScheduleC);
        return Arrays.asList(scheduleA, scheduleB, scheduleC);
    }

    private List<Exercise> createExercises(){
        Random random = new Random();
        int baseId = random.nextInt(1000000);
        Exercise exercise1 = new Exercise(baseId + 4, "Push-ups", "Standard push-up exercise.", 3, 10, RestTime.SECONDS60);
        Exercise exercise2 = new Exercise(baseId + 5, "Squats", "Bodyweight squats.", 3, 10, RestTime.SECONDS60);
        Exercise exercise3 = new Exercise(baseId + 6, "Plank", "Core plank hold.", 3, 10, RestTime.SECONDS60);
        return Arrays.asList(exercise1, exercise2, exercise3);
    }

    //creo un cliente
    private Customer createCustomer() {
        return new Customer(
                new Credentials(testEmailCustomer, passwordCustomer, Role.CLIENT),
                TEST_NAME,
                TEST_SURNAME,
                TEST_GENDER,
                TEST_ONLINE,
                testBirthday
        );
    }

    private Trainer createTrainer() {
        return new Trainer(
                new Credentials(testEmailTrainer, passwordTrainer, Role.TRAINER),
                TEST_NAME,
                TEST_SURNAME,
                TEST_GENDER,
                TEST_ONLINE,
                testBirthday
        );
    }
    //genero una password
    private String generatePassword() {
        return "test" + System.currentTimeMillis();
    }
    //genero un email di test
    private String generateTestEmail() {
        return "t_" + UUID.randomUUID().toString() + "@ex.com";
    }
    //registro il cliente
    private void registerCustomer() throws LoginAndRegistrationException {
        try {
            customerDAO.registerCustomer(testCustomer);
        } catch (MailAlreadyExistsException e) {
            throw new LoginAndRegistrationException("Errore nella registrazione del cliente", e);
        }
    }

    private void registerTrainer() throws LoginAndRegistrationException {
        try {
            trainerDAO.registerTrainer(testTrainer);
        } catch (MailAlreadyExistsException e) {
            throw new LoginAndRegistrationException("Errore nella registrazione del trainer" + e.getMessage(), e);
        }
    }

    private void registerExercises(){
        try {
            exerciseDAO.addExercise(testExercises.get(0));
            exerciseDAO.addExercise(testExercises.get(1));
            exerciseDAO.addExercise(testExercises.get(2));
            exerciseDAO.addExerciseSchedule(testSchedules.get(0), testExercises.get(0));
            exerciseDAO.addExerciseSchedule(testSchedules.get(0), testExercises.get(1));
            exerciseDAO.addExerciseSchedule(testSchedules.get(1), testExercises.get(2));
            exerciseDAO.addExerciseSchedule(testSchedules.get(2), testExercises.get(0));
            exerciseDAO.addExerciseSchedule(testSchedules.get(2), testExercises.get(1));
            exerciseDAO.addExerciseSchedule(testSchedules.get(2), testExercises.get(2));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void registerSchedules() {
        try {
            scheduleDAO.addSchedule(testSchedules.get(0));
            scheduleDAO.addSchedule(testSchedules.get(1));
            scheduleDAO.addSchedule(testSchedules.get(2));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
