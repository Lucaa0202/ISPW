package org.example.project3.model;

public class Reservation {

    private Customer customer;
    private Course course;
    private String day;
    private String hour;


    public Reservation(Customer customer, Course course, String day, String hour) {
        this.customer = customer;
        this.course = course;
        this.day = day;
        this.hour = hour;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Course getCourse() {
        return course;
    }

    public String getDay() {
        return day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }
}
