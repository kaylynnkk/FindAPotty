package com.example.findapotty.user;

import com.example.findapotty.model.RestroomsManager;

public class VisitedRestroomsManager extends RestroomsManager<VisitedRestroom> {

    private static VisitedRestroomsManager instance;
    private VisitedRestroomsManager() {
    }

    public static VisitedRestroomsManager getInstance() {
        if (instance == null) {
            instance = new VisitedRestroomsManager();
        }
        return instance;
    }
}
