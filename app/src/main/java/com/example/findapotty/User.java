package com.example.findapotty;

import android.net.Uri;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.Exclude;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class User {

    private static User instance;
    private String userName;
    private String avatarPath = "avatars/default_avatar.jpg";
    private Uri avatarUrl;
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

    public String getAvatarPath() {
        return avatarPath;
    }

    @Exclude
    public Uri getAvatarUrl() {
        return avatarUrl;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public void setAvatarUrl(Uri avatarUrl) {
        this.avatarUrl = avatarUrl;
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

            retrievedFavoriteRestrooms.forEach((key, value) -> {
                favoriteRestroomsList.add(value);
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
