package com.example.findapotty.search.restroompage;

import android.widget.ImageView;

public class RestroomReview {

    private String avatarUrl = null;
    private String username = null;
    private ImageView avatar = null;

    public RestroomReview(String avatarUrl, String username) {
        this.avatarUrl = avatarUrl;
        this.username = username;
    }

    public RestroomReview(ImageView avatar, String username) {
        this.avatar = avatar;
        this.username = username;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getUsername() {
        return username;
    }

}
