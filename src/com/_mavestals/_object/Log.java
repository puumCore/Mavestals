package com._mavestals._object;

import java.io.Serializable;

/**
 * @author Mandela aka puumInc
 * @version 1.0.0
 */

public class Log implements Serializable {

    public static final long serialVersionUID = 6L;

    private String date;
    private String time;
    private String username;
    private String action;
    private String description;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
