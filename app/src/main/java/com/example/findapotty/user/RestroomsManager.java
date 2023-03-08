package com.example.findapotty.user;

import com.example.findapotty.Restroom;
import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;

public class RestroomsManager {

    private HashMap<String, Restroom> restrooms = new HashMap<>();
    private ArrayList<Restroom> restroomList = new ArrayList<>();

    public HashMap<String, Restroom> getRestrooms() {
        return restrooms;
    }

    @Exclude
    public ArrayList<Restroom> getRestroomsList() {
        return restroomList;
    }

    public Restroom getRestroomByIndex(int index) {
        return restroomList.get(index);
    }

    public void setRestrooms(HashMap<String, Restroom> retrievedRestrooms) {
        if (retrievedRestrooms != null){
            restrooms = retrievedRestrooms;

            retrievedRestrooms.forEach((key, value) -> {
                restroomList.add(value);
            });
        }
    }

    public void addRestroom(String placeId, Restroom restroom) {
        restrooms.put(placeId, restroom);
        restroomList.add(restroom);
    }

    public void removeRestroom(String placeId) {
        restroomList.remove(restrooms.get(placeId));
        restrooms.remove(placeId);
    }

    public int getCount() {
        return restroomList.size();
    }

}
