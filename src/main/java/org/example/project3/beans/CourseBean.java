package org.example.project3.beans;

public class CourseBean {

    private int courseID;
    private String courseName;
    private int remainingSlots;
    private String duration;
    private String level;
    private String day;
    private String hour;

    public CourseBean(Integer id, String courseName,int remainingSlots, String duration, String level, String day, String hour) {

        this.courseID = id;
        this.courseName = courseName;
        this.remainingSlots = remainingSlots;
        this.duration = duration;
        this.level = level;
        this.day = day;
        this.hour = hour;

    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getRemainingSlots() {
        return remainingSlots;
    }

    public void setRemainingSlots(int remainingSlots) {
        this.remainingSlots = remainingSlots;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }
}