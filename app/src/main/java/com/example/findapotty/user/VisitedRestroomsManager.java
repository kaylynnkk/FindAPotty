package com.example.findapotty.user;

import com.example.findapotty.Restroom;

import java.util.ArrayList;
import java.util.HashMap;

public class VisitedRestroomsManager extends RestroomsManager{

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
