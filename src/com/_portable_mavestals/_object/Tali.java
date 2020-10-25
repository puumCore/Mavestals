package com._portable_mavestals._object;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.io.Serializable;

/**
 * @author Mandela aka puumInc
 * @version 1.0.0
 */
public class Tali implements Serializable {

    public static final long serialVersionUID = 4L;

    private Double cashAmount;
    private Double mpesaAmount;
    private Double savings;
    private Boolean isActive;
    private JsonArray ledgers;

    public Double getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(Double cashAmount) {
        this.cashAmount = cashAmount;
    }

    public Double getMpesaAmount() {
        return mpesaAmount;
    }

    public void setMpesaAmount(Double mpesaAmount) {
        this.mpesaAmount = mpesaAmount;
    }

    public Double getSavings() {
        return savings;
    }

    public void setSavings(Double savings) {
        this.savings = savings;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public JsonArray getLedgers() {
        return ledgers;
    }

    public void setLedgers(JsonArray ledgers) {
        this.ledgers = ledgers;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this, Tali.class);
    }
}
