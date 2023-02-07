package com.example.findapotty;

import java.util.ArrayList;

public class RestroomManager {

    private static RestroomManager instance;

    private ArrayList<Restroom> restrooms = new ArrayList<>();

    private RestroomManager() {

    }

    public static RestroomManager getInstance() {
        if (instance == null) {
            instance = new RestroomManager();
        }
        return instance;
    }

    public void addRestroom(Restroom restroom) {
        restrooms.add(restroom);
    }

    public ArrayList<Restroom> getRestrooms() {
        return restrooms;
    }

    public Restroom getRestroomByIndex(int index) {
        return restrooms.get(index);
    }

    public Restroom getRestroomByMarkerId(String markId) {
        for (Restroom restroom : restrooms) {
            if (restroom.getMarkerId().equals(markId)) {
                return restroom;
            }
        }
        return null;
    }
}
