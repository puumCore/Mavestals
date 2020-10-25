package com._portable_mavestals._object;

import com.google.gson.JsonArray;

import java.io.Serializable;

/**
 * @author Mandela aka puumInc
 * @version 1.0.0
 */
public class Account implements Serializable {

    public static final long serialVersionUID = 1L;

    private String username;
    private String image;
    private User user;
    private JsonArray talis;
    private JsonArray activities;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public JsonArray getTalis() {
        return talis;
    }

    public void setTalis(JsonArray talis) {
        this.talis = talis;
    }

    public JsonArray getActivities() {
        return activities;
    }

    public void setActivities(JsonArray activities) {
        this.activities = activities;
    }

}
