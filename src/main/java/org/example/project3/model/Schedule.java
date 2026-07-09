package org.example.project3.model;

import java.util.List;

public class Schedule {
    private long id;
    private String name;
    private Customer customer;
    private Trainer trainer;
    private List<Exercise> exercises;

    public Schedule(long id, String name, Customer customer, Trainer trainer, List<Exercise> exercises) {
        this.id = id;
        this.name = name;
        this.customer = customer;
        this.trainer = trainer;
        this.exercises = exercises;
    }

    public Schedule(long id, String name, Customer customer, Trainer trainer) {
        this.id = id;
        this.name = name;
        this.customer = customer;
        this.trainer = trainer;
        this.exercises = null;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public void addExercise(Exercise exercise) {
        this.exercises.add(exercise);
    }

    public void setId(long id) {
        this.id = id;
    }
}
