package org.example.project3.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Customer extends LoggedUser{
    private Subscription subscription;
    private ArrayList<Schedule> schedules;
    private String injury;
    private Date startDate;
    private Date endDate;

    public Customer(Credentials credentials, String name, String surname, String gender, boolean isOnline, LocalDate birthday) {
        super(credentials, name, surname, gender, isOnline, birthday);
        this.subscription = null;
        this.schedules = new ArrayList<>();
        this.startDate = new Date();
        this.endDate = new Date();
        this.injury = "";
    }

    public Customer(Credentials credentials) {
        super(credentials);
        this.subscription = null;
        this.schedules = new ArrayList<>();
        this.startDate = new Date();
        this.endDate = new Date();
        this.injury = "";
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = (ArrayList<Schedule>) schedules;
    }

    public void addSchedule(Schedule schedule){
        this.schedules.add(schedule);
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setInjury(String injury) {
        this.injury = injury;
    }

    public String getInjury() {
        return injury;
    }

}
