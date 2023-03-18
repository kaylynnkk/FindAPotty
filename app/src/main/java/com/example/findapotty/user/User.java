package com.example.findapotty.user;

import android.net.Uri;

import com.google.firebase.database.Exclude;

import java.util.HashMap;

public class User {

    private static User instance;
    private String userName;
    private String avatarPath = "avatars/default_avatar.jpg";
    private Uri avatarUrl;
    private String userId;
    private final HashMap<String, FavoriteRestroom> favoriteRestrooms =
        FavoriteRestroomsManager.getInstance().getRestrooms();
    private final HashMap<String, VisitedRestroom> visitedRestrooms =
            VisitedRestroomsManager.getInstance().getRestrooms();

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

    public HashMap<String, VisitedRestroom> getVisitedRestrooms() {
        return visitedRestrooms;
    }

    public HashMap<String, FavoriteRestroom> getFavoriteRestrooms() {
        return favoriteRestrooms;
    }
}
