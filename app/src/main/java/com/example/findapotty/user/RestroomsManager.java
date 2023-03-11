package com.example.findapotty.user;

import com.example.findapotty.Restroom;
import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;

public class RestroomsManager<T extends Restroom> {

    private HashMap<String, T> restrooms = new HashMap<>();
    private ArrayList<T> restroomList = new ArrayList<>();

    public HashMap<String, T> getRestrooms() {
        return restrooms;
    }


    public ArrayList<T> getRestroomsList() {
        return restroomList;
    }

    public T getRestroomByIndex(int index) {
        return restroomList.get(index);
    }

    public void setRestrooms(HashMap<String, T> retrievedRestrooms) {
        if (retrievedRestrooms != null){
            restrooms = retrievedRestrooms;

            retrievedRestrooms.forEach((key, value) -> {
                restroomList.add(value);
            });
        }
    }

    public void addRestroom(String placeId, T restroom) {
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
