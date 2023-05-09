package com.example.findapotty.reminder;

//model class is used to set and get the data from database

public class ReminderMessage {
    String reminderId, label, date, time;

    public ReminderMessage() {
    }

    public ReminderMessage(String reminderId, String label, String date, String time) {
        this.reminderId = reminderId;
        this.label = label;
        this.date = date;
        this.time = time;
    }

    public String getReminderId() {
        return reminderId;
    }

    public void setReminderId(String reminderId) {
        this.reminderId = reminderId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

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
}
