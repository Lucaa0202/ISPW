package org.example.project3.beans;


public class ReservationBean {

    private CustomerBean customerBean;
    private CourseBean courseBean;
    private String day;
    private String hour;

    public ReservationBean(CustomerBean customerBean, CourseBean courseBean, String day, String hour){
        this.customerBean = customerBean;
        this.courseBean = courseBean;
        this.day = day;
        this.hour = hour;
    }

    public void setCourse(CourseBean courseBean) {
        this.courseBean = courseBean;
    }

    public void setCustomer(CustomerBean customerBean) {
        this.customerBean = customerBean;
    }

    public void setday(String day) {
        this.day= day;
    }

    public void setHour(String hour) {this.hour = hour;}

    public CustomerBean getCustomer() {
        return customerBean;
    }

    public CourseBean getCourse() {
        return courseBean;
    }

    public String getHour() {
        return hour;
    }

    public String getDay() { return day; }
}