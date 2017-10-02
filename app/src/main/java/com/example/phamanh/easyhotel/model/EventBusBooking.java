package com.example.phamanh.easyhotel.model;


public class EventBusBooking {
    private String action;
    private String value;

    public EventBusBooking(String action) {
        this.action = action;
    }

    public EventBusBooking(String action, String value) {
        this.action = action;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
