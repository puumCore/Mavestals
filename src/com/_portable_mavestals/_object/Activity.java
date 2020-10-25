package com._portable_mavestals._object;

import java.io.Serializable;

/**
 * @author Mandela aka puumInc
 * @version 1.0.0
 */
public class Activity implements Serializable {

    public static final long serialVersionUID = 2L;

    private String date;
    private String time;
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
