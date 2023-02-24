package com.example.findapotty;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class User {

    private static User instance;
    private String userName;
    private String userId;
//    private List<Restroom> favoriteRestrooms = new ArrayList<>();
    private HashMap<String, Restroom> favoriteRestrooms = new HashMap<>();
    private ArrayList<Restroom> favoriteRestroomsList = new ArrayList<>();

    private User() {
    }

    public static User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }
    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public HashMap<String, Restroom> getFavoriteRestrooms() {
        return favoriteRestrooms;
    }

    public ArrayList<Restroom> getFavoriteRestroomsList() {
        return favoriteRestroomsList;
    }

    public Restroom getFavoriteRestroomByIndex(int index) {
        return favoriteRestroomsList.get(index);
    }

    public void setFavoriteRestrooms(HashMap<String, Restroom> retrievedFavoriteRestrooms) {
        if (retrievedFavoriteRestrooms != null){
            favoriteRestrooms = retrievedFavoriteRestrooms;

//            Log.d("User", "setFavoriteRestrooms: "+ retrievedFavoriteRestrooms.getClass());

            retrievedFavoriteRestrooms.forEach((key, value) -> {
//                favoriteRestroomsList.add(value);
//                Log.d("User", "setFavoriteRestrooms: "+ value.getClass());
            });
        }
    }

    public void addFavoriteRestroom(String placeId, Restroom restroom) {
        favoriteRestrooms.put(placeId, restroom);
        favoriteRestroomsList.add(restroom);
    }

    public void removeFavoriteRestroom(String placeId) {
        favoriteRestroomsList.remove(favoriteRestrooms.get(placeId));
        favoriteRestrooms.remove(placeId);
    }
}
