package org.example.project3.beans;

import org.example.project3.utilities.enums.RestTime;

public class ExerciseBean {
    private long id;
    private String name;
    private String description;
    private Integer numberSeries;
    private Integer numberReps;
    private RestTime restTime;

    public ExerciseBean(long id, String name, String description, Integer numberSeries, Integer numberReps, RestTime restTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.numberSeries = numberSeries;
        this.numberReps = numberReps;
        this.restTime = restTime;
    }

    public ExerciseBean(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.numberSeries = 0;
        this.numberReps = 0;
        this.restTime = null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNumberSeries(Integer numberSeries) {
        this.numberSeries = numberSeries;
    }

    public void setNumberReps(Integer numberReps) {
        this.numberReps = numberReps;
    }

    public void setRestTime(RestTime restTime) {
        this.restTime = restTime;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getNumberSeries() {
        return numberSeries;
    }

    public Integer getNumberReps() {
        return numberReps;
    }

    public RestTime getRestTime() {
        return restTime;
    }

}