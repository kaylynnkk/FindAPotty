package com.example.findapotty.search;

import com.example.findapotty.model.RestroomsManager;

import java.util.Comparator;

public class NearbyRestroomsManager extends RestroomsManager<NearbyRestroom> {

    private static NearbyRestroomsManager instance;

    private NearbyRestroomsManager() {
    }

    public static NearbyRestroomsManager getInstance() {
        if (instance == null) {
            instance = new NearbyRestroomsManager();
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

    public void sortByDistance() {
        getRestroomsList().sort(new Comparator<NearbyRestroom>() {
            @Override
            public int compare(NearbyRestroom nearbyRestroom, NearbyRestroom other) {
                return Long.compareUnsigned(nearbyRestroom.getCurrentDistance(), other.getCurrentDistance());
            }
        });
    }
}
