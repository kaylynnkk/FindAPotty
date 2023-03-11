package com.example.findapotty.user;

import com.example.findapotty.Restroom;

public class VisitedRestroom extends Restroom {

    private String dateTime;
    private int frequency;

    public VisitedRestroom(
            Restroom restroom, String dateTime, int frequency) {
        super(restroom);
        this.dateTime = dateTime;
        this.frequency = frequency;
    }

    public VisitedRestroom() {}

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String getDateTime() {
        return dateTime;
    }

    public int getFrequency() {
        return frequency;
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public String getAddress() {
        return super.getAddress();
    }

    @Override
    public String getPhotoPath() {
        return super.getPhotoPath();
    }
}
