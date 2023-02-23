package com.example.findapotty;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class User {

    private static User instance;
    private String userName;
    private String userId;
//    private List<String> favoriteRestrooms = new ArrayList<>();
    private HashMap<String, LatLng> favoriteRestrooms = new HashMap<>();

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

    public HashMap<String, LatLng> getFavoriteRestrooms() {
        return favoriteRestrooms;
    }

    public void setFavoriteRestrooms(HashMap<String, LatLng> hashMap) {
        favoriteRestrooms = hashMap;
    }

    public void addFavoriteRestroom(String placeId, LatLng latLng) {
        favoriteRestrooms.put(placeId, latLng);
    }

    public void removeFavoriteRestroom(String placeId) {
        favoriteRestrooms.remove(placeId);
    }
}
