package com.irfan.draft1.Schedule;

/**
 * Created by irfan on 11/06/2017.
 */


import java.io.Serializable;
import java.util.Date;

/**
 * Created by User on 6/1/2017.
 */


public class Schedule implements Serializable {
    private int id;
    private boolean state;
    private Date time;
    private String location;
//    private Notification notification;



    public Schedule()
    {}
    public Schedule(Date time, String location, int id, boolean state) { // Notification notification
        this.time = time;
        this.location = location;
        this.id = id;
        this.state = state;
//        this.notification = notification;
    }



    public Date getTime() {
        return time;
    }

    public void setTime(Date name) {
        this.time = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public boolean getState()
    {
        return state;
    }
    public void setState(boolean state)
    {
        this.state = state;
    }


//    public Notification getNotification() {
//        return notification;
//    }
//
//    public void setNotification(Notification notification) {
//        this.notification = notification;
//    }
}