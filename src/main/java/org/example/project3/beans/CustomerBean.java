package org.example.project3.beans;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CustomerBean extends LoggedUserBean {
    private SubscriptionBean subscriptionBean;
    private ArrayList<ScheduleBean> schedulesBean;
    private String injury;
    private Date startDate;
    private Date endDate;

    public CustomerBean(CredentialsBean credentialsBean, String name, String surname, String gender, boolean isOnline, LocalDate birthday) {
        super(credentialsBean, name, surname, gender, isOnline, birthday);
        this.subscriptionBean = null;
        this.schedulesBean = new ArrayList<>();
        this.startDate = new Date();
        this.endDate = new Date();
    }

    public CustomerBean(CredentialsBean credentialsBean) {
        super(credentialsBean);
        this.subscriptionBean = null;
        this.schedulesBean = new ArrayList<>();
        this.startDate = new Date();
        this.endDate = new Date();
    }

    public SubscriptionBean getSubscriptionBean() {
        return subscriptionBean;
    }

    public void setSubscriptionBean(SubscriptionBean subscriptionBean) {
        this.subscriptionBean = subscriptionBean;
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

    public void setSchedules(List<ScheduleBean> schedulesBean) {
        this.schedulesBean = (ArrayList<ScheduleBean>) schedulesBean;
    }

    public void addScheduleBean(ScheduleBean scheduleBean){
        this.schedulesBean.add(scheduleBean);
    }

    public List<ScheduleBean> getSchedulesBean() {
        return schedulesBean;
    }

    public void setInjury(String injury) {
        this.injury = injury;
    }

    public String getInjury() {
        return injury;
    }

}
