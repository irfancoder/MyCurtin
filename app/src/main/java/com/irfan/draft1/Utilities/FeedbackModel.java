package com.irfan.draft1.Utilities;

import java.util.Date;

public class FeedbackModel {

    private String comment;
    private String user;
    private Date date;
    private String uid;
    private String email;


    public FeedbackModel() {
    }


    public FeedbackModel(String comment, String user, Date date, String uid, String email) {
        this.comment = comment;
        this.user = user;
        this.date = date;
        this.uid = uid;
        this.email = email;
    }
    public String getComment() {
        return comment;
    }

    public String getUser() {
        return user;
    }

    public Date getDate() {
        return date;
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }



}
