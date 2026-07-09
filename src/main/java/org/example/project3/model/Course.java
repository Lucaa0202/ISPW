package org.example.project3.model;

// correzione costruttore by bean
public class Course {

    private int courseID;
    private String courseName;
    private int remainingSlots;
    private String duration;
    private String level;
    private String day;
    private String hour;

    public Course(Integer id,String courseName, int remainingSlots, String duration, String level, String day, String hour) {
        this.courseID = id;
        this.courseName = courseName;
        this.duration = duration;
        this.remainingSlots = remainingSlots;
        this.level = level;
        this.day = day;
        this.hour = hour;

    }

    public Course(String courseName, int remainingSlots, String duration, String level, String day, String hour) {
        this.courseName = courseName;
        this.hour = hour;
        this.remainingSlots = remainingSlots;
        this.day = day;
        this.level = level;
        this.duration = duration;
    }

    public Course(String coursName){
        this.courseName = coursName;
    }   // verificare se utile per reservation

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getCourseID() {
        return courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getDuration() {
        return duration;
    }

    public String getLevel() {
        return level;
    }

    public String getDay(){
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getHour() {
        return hour;
    }

    public int getRemainingSlots() {
        return remainingSlots;
    }

    public void setRemainingSlots(int remainingSlots) {
        this.remainingSlots = remainingSlots;
    }

}
