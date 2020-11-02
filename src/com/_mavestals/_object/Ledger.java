package com._mavestals._object;

import java.io.Serializable;

/**
 * @author Mandela aka puumInc
 * @version 1.0.0
 */
public class Ledger implements Serializable {

    public static final long serialVersionUID = 3L;

    private String date;
    private String time;
    private String purpose;
    private Double amountSpent;
    private Boolean paidAsCash;

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

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public Double getAmountSpent() {
        return amountSpent;
    }

    public void setAmountSpent(Double amountSpent) {
        this.amountSpent = amountSpent;
    }

    public Boolean getPaidAsCash() {
        return paidAsCash;
    }

    public void setPaidAsCash(Boolean paidAsCash) {
        this.paidAsCash = paidAsCash;
    }

}
