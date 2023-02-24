package com.example.findapotty;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class User {

    private static User instance;
    private String userName;
    private String userId;
//    private List<Restroom> favoriteRestrooms = new ArrayList<>();
    private HashMap<String, Restroom> favoriteRestrooms = new HashMap<>();

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

    public void setFavoriteRestrooms(HashMap<String, Restroom> retrievedFavoriteRestrooms) {
        if (retrievedFavoriteRestrooms != null){
            favoriteRestrooms = retrievedFavoriteRestrooms;
        }
    }

    public void addFavoriteRestroom(String placeId, Restroom restroom) {
        favoriteRestrooms.put(placeId, restroom);
    }

    public void removeFavoriteRestroom(String placeId) {
        favoriteRestrooms.remove(placeId);
    }
}
