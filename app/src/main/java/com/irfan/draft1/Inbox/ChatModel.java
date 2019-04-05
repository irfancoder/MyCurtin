package com.irfan.draft1.Inbox;

import java.util.Date;

/**
 * Created by irfan on 27/01/2018.
 */

public class ChatModel  {
    private String message;
    private Date timestamp;
    private String sentBy;

    public ChatModel(String message, Date timestamp,String sentBy) {
        this.message = message;
        this.timestamp = timestamp;
        this.sentBy = sentBy;
    }

    public ChatModel() {
    }
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getSentBy() {
        return sentBy;
    }

    public void setSentBy(String sentBy) {
        this.sentBy = sentBy;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}

