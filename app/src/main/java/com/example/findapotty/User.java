package com.example.findapotty;

import java.util.ArrayList;

public class User {

    private static User instance;
    private String userName;
    private String userId;
    private ArrayList<Restroom> favoriteRestrooms = new ArrayList<>();

    private User(String userName, String userId) {
        this.userName = userName;
        this.userId = userId;
    }

    public static User getInstance(String userName, String userId) {
        if (instance == null) {
            instance = new User(userName, userId);
        }
        return instance;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

    public ArrayList<Restroom> getFavoriteRestrooms() {
        return favoriteRestrooms;
    }

    public void addFavoriteRestroom(Restroom restroom) {
        favoriteRestrooms.add(restroom);
    }
}
