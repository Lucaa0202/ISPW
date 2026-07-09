package org.example.project3.dao.demo.shared;


import org.example.project3.model.*;
import org.example.project3.utilities.enums.RestTime;
import org.example.project3.utilities.enums.Role;
import org.example.project3.utilities.others.Printer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SharedResources {

    // Mappe condivise per i vari entità
    private final Map<String, Trainer> trainers = new ConcurrentHashMap<>();
    private final Map<String, Course> courses = new ConcurrentHashMap<>();
    private final Map<String, Customer> customers = new ConcurrentHashMap<>();
    private final Map<Integer, Subscription> subscriptions = new ConcurrentHashMap<>();
    private final Map<Long, Schedule> schedules = new ConcurrentHashMap<>();
    private final Map<Long, Exercise> exercises = new ConcurrentHashMap<>();
    private final Map<String, Credentials> userTable = new ConcurrentHashMap<>();
    private final Map<Long, List<Request>> requestsSent = new HashMap<>();

    //associazione corso-trainer
    private final Map<String, Trainer> trainerCourse = new ConcurrentHashMap<>();
    private final Map<String, List<Reservation>> reservations = new HashMap<>();
    private final Map<String, List<Reservation>> reservationRequests = new HashMap<>();

    // Mappa per tenere traccia delle relazioni scheda-esercizi e utente-schede
    private final  Map<String, List<Schedule>> customerSchedules = new HashMap<>();
    private final Map<Long,List<Exercise>> exerciseSchedules = new HashMap<>();
    private final Map<String, List<Request>> requestTrainer = new HashMap<>();


    // Singleton per garantire che ci sia una sola istanza della classe
    private static SharedResources instance=null;

    private SharedResources() {
    }

    // Metodo per ottenere l'istanza Singleton
    public static synchronized SharedResources getInstance() {
        if(instance==null){
            instance = new SharedResources();
        }
        return instance;
    }

    // Getter per le mappe condivise
    public Map<String, Trainer> getTrainers() {
        return trainers;
    }

    public Map<String, Customer> getCustomers() {
        return customers;
    }

    public Map<Integer, Subscription> getSubscriptions() {
        return subscriptions;
    }
    public Map<Long,Schedule> getSchedules(){
        return schedules;
    }
    public Map<Long,Exercise> getExercises(){
        return exercises;
    }
    public Map<String,Credentials> getUserTable(){
        return userTable;
    }
    public Map<String,List<Schedule>> getCustomerSchedules(){
        return customerSchedules;
    }
    public Map<Long,List<Request>> getRequestsSent(){
        return requestsSent;
    }

    public Map<String, Course> getCourses() { return courses; }

    public Map<String, List<Reservation>> getReservations() { return reservations; }
    public Map<String, List<Reservation>> getReservationRequests() { return reservationRequests; }
    public Map<String, Trainer> getTrainerCourse() {
        return trainerCourse;
    }
    public Map<Long, List<Exercise>> getExerciseSchedules() {
        return exerciseSchedules;
    }
    public Map<String, List<Request>> getRequestTrainer() {
        return requestTrainer;
    }

    public void populateData(){
        Exercise exercise1=new Exercise(10,"Push ups","basic push ups",4,10, RestTime.SECONDS60);
        Exercise exercise2=new Exercise(20,"Squat","basic squat",4,10, RestTime.SECONDS60);
        Exercise exercise3=new Exercise(30,"Crunch","basic crunch",4,10, RestTime.SECONDS60);
        Exercise exercise4=new Exercise(40,"Lat machine","prone lat machine",4,10, RestTime.SECONDS60);
        exercises.put(exercise1.getId(),exercise1);
        exercises.put(exercise2.getId(),exercise2);
        exercises.put(exercise3.getId(),exercise3);
        exercises.put(exercise4.getId(),exercise4);

        Customer customer= new Customer(new Credentials("mario.rossi@gmail.com","mariorossi", Role.CLIENT),"mario","rossi","maschio",true, LocalDate.now().minusYears(20));
        customers.put(customer.getCredentials().getMail(),customer);
        userTable.put(customer.getCredentials().getMail(),customer.getCredentials());

        Trainer trainer= new Trainer(new Credentials("mattia.verdi@gmail.com","mattiaverdi", Role.TRAINER),"mattia","verdi","maschio",true, LocalDate.now().minusYears(20));
        trainers.put(trainer.getCredentials().getMail(),trainer);
        userTable.put(trainer.getCredentials().getMail(),trainer.getCredentials());

        List<Exercise> exerciseList1= new ArrayList<>();
        exerciseList1.add(exercise1);
        exerciseList1.add(exercise2);
        exerciseList1.add(exercise3);
        for(Exercise exercise:exerciseList1){
            Printer.println(" "+exercise.getId()+" "+exercise.getName() +" "+exercise.getDescription());
        }
        List<Exercise> exerciseList2= new ArrayList<>();
        exerciseList2.add(exercise2);
        exerciseList2.add(exercise3);
        exerciseList2.add(exercise4);
        for(Exercise exercise: exerciseList2){
            Printer.println(" "+exercise.getId()+" "+exercise.getName() +" "+exercise.getDescription());
        }
        List<Exercise> exerciseList3= new ArrayList<>();
        exerciseList3.add(exercise1);
        exerciseList3.add(exercise3);
        for(Exercise exercise:exerciseList3){
            Printer.println(" "+exercise.getId()+" "+exercise.getName() +" "+exercise.getDescription());
        }
        Schedule schedule1=new Schedule(1,"full body",customer,trainer,exerciseList1);
        Schedule schedule2=new Schedule(2,"upper",customer,trainer,exerciseList2);
        Schedule schedule3=new Schedule(3,"gambe",customer,trainer,exerciseList3);
        schedules.put(schedule1.getId(),schedule1);
        exerciseSchedules.put(schedule1.getId(),exerciseList1);
        schedules.put(schedule2.getId(),schedule2);
        exerciseSchedules.put(schedule2.getId(),exerciseList2);
        schedules.put(schedule3.getId(),schedule3);
        exerciseSchedules.put(schedule3.getId(),exerciseList3);



        // DEBUGGING: Stampa il contenuto della mappa exerciseSchedules
        Printer.println("----------------------------------------------");
        Printer.println("Contenuto della mappa exerciseSchedules:");

// Itera su tutte le entry (chiave-valore) della mappa
        for (Map.Entry<Long, List<Exercise>> entry : exerciseSchedules.entrySet()) {
            Long scheduleId = entry.getKey();
            List<Exercise> exs = entry.getValue();

            Printer.println("\nID Scheda: " + scheduleId);
            if (exs != null && !exs.isEmpty()) {
                Printer.println("Esercizi:");
                // Itera sulla lista di esercizi per ogni scheda
                for (Exercise exercise : exs) {
                    Printer.println("  - " + exercise.getName() + " (ID: " + exercise.getId() + ")");
                }
            } else {
                Printer.println("  - Nessun esercizio trovato per questa scheda.");
            }
        }
        Printer.println("----------------------------------------------");

        List<Schedule> scheduleList1= new ArrayList<>();
        scheduleList1.add(schedule1);
        scheduleList1.add(schedule2);
        scheduleList1.add(schedule3);
        customerSchedules.put(customer.getCredentials().getMail(),scheduleList1);


    }


}
