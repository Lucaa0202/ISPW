package org.example.project3.model;

import java.time.LocalDateTime;

public class Request {
    private long id;
    private Schedule schedule;
    private Exercise exercise;
    private String reason;
    private LocalDateTime dateTime;

    public Request(long id, Schedule schedule, Exercise exercise, String reason, LocalDateTime dateTime) {
        this.id = id;
        this.schedule = schedule;
        this.exercise = exercise;
        this.reason = reason;
        this.dateTime = dateTime;
    }

    public Request(Schedule schedule) {
        this.id = 0;
        this.schedule = schedule;
        this.exercise = null;
        this.reason = "";
        this.dateTime = null;
    }

    public void  setId(long id) {
        this.id = id;
    }

    public long getID() {
        return id;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public String getReason() {
        return reason;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}