package com.example.findapotty;

import java.util.ArrayList;
import java.util.List;

public class User {

    private static User instance;
    private String userName;
    private String userId;
    private List<String> favoriteRestrooms = new ArrayList<>();

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

    public List<String> getFavoriteRestrooms() {
        return favoriteRestrooms;
    }

    public void addFavoriteRestroom(String placeId) {
        favoriteRestrooms.add(placeId);
    }

    public void removeFavoriteRestroom(String placeId) {
        favoriteRestrooms.remove(placeId);
    }
}
