package org.example.project3.model;

import org.example.project3.utilities.enums.RestTime;

public class Exercise {
    private long id;
    private String name;
    private String description;
    private Integer numberSeries;
    private Integer numberReps;
    private RestTime restTime;


    public Exercise( String name, String description, Integer numberSeries, Integer numberReps, RestTime restTime) {
        this.id = 0;
        this.name = name;
        this.description = description;
        this.numberSeries = numberSeries;
        this.numberReps = numberReps;
        this.restTime = restTime;
    }

    public Exercise(long id, String name, String description, Integer numberSeries, Integer numberReps, RestTime restTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.numberSeries = numberSeries;
        this.numberReps = numberReps;
        this.restTime = restTime;
    }

    public Exercise(long id,String name) {
        this.id = id;
        this.name = name;
        this.description = null;
        this.numberSeries = null;
        this.numberReps = null;
        this.restTime = null;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumberReps(Integer numberReps) {
        this.numberReps = numberReps;
    }


    public void setNumberSeries(Integer numberSeries) {
        this.numberSeries = numberSeries;
    }

    public void setRestTime(RestTime restTime) {
        this.restTime = restTime;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Integer getNumberSeries() {
        return numberSeries;
    }

    public RestTime getRestTime() {
        return restTime;
    }

    public Integer getNumberReps() {
        return numberReps;
    }

    public void setId(long id) {
        this.id = id;
    }
}
