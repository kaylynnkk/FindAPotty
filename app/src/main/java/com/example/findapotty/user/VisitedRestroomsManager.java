package com.example.findapotty.user;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;

public class VisitedRestroomsManager extends RestroomsManager<VisitedRestroom>{

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
