package com._portable_mavestals._object;

import java.io.Serializable;

/**
 * @author Mandela aka puumInc
 * @version 1.0.0
 */
public class User implements Serializable {

    public static final long serialVersionUID = 5L;

    private String password;
    private String firstname;
    private String surname;
    private String dateOfBirth;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

}
