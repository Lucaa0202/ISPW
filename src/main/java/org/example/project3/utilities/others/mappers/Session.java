package org.example.project3.utilities.others.mappers;

import org.example.project3.beans.LoggedUserBean;

public class Session {
    private LoggedUserBean user;

    public Session() {
        this.user = null;
    }

    public LoggedUserBean getUser(){
        return this.user;
    }

    public void setUser(LoggedUserBean user){
        this.user = user;
    }

}
