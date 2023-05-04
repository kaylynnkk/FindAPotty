package com.example.findapotty.search;

import com.example.findapotty.model.Restroom;
import com.example.findapotty.model.RestroomsManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    public int getRestroomIndexByMarkerId(String markId) {
        for (int i = 0; i < getRestroomsList().size(); i++) {
            if (getRestroomByIndex(i).getMarkerId().equals(markId)) {
                return i;
            }
        }
        return -1;
    }

    public void sortByDistance() {
        getRestroomsList().sort(
                (nearbyRestroom, other) ->
                        Long.compareUnsigned(nearbyRestroom.getCurrentDistance(), other.getCurrentDistance())
        );
    }

    //addded this method
    public void sortByReview()
    {
        getRestroomsList().sort(
                (nearbyRestroom, other) ->
                        Float.compare(nearbyRestroom.getRating(), other.getRating())
        );
        Collections.reverse(getRestroomsList());
    }

    //sorts by distance first and then highest review
    public void sortByDistanceAndReview()
    {
        Comparator<NearbyRestroom> compareByNameAndDistance =
                Comparator.comparing(NearbyRestroom::getCurrentDistance).thenComparing(NearbyRestroom::getRating, (dbl1,dbl2) -> Double.compare(dbl2,dbl1));
        getRestroomsList().sort(compareByNameAndDistance);
    }
}
