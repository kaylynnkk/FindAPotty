package com.example.findapotty.search;

import com.example.findapotty.model.RestroomsManager;

public class NearbyRestroomManager extends RestroomsManager<NearbyRestroom> {

    private static NearbyRestroomManager instance;

    private NearbyRestroomManager() {

    }

    public static NearbyRestroomManager getInstance() {
        if (instance == null) {
            instance = new NearbyRestroomManager();
        }
        return instance;
    }

    public NearbyRestroom getRestroomByMarkerId(String markId) {
        for (NearbyRestroom restroom : getRestroomsList()) {
            if (restroom.getMarkerId().equals(markId)) {
                return restroom;
            }
        }
        return null;
    }
}
