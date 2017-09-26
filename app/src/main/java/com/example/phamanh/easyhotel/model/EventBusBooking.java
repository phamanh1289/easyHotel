package com.example.phamanh.easyhotel.model;


public class EventBusBooking {
    public String action;
    private String item;

    public EventBusBooking(String action) {
        this.action = action;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getItem() {
        return item;
    }
}
