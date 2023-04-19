package com.example.findapotty.search.restroompage;

import android.widget.ImageView;

public class RestroomReview {

    private String avatarUrl;
    private String username;
    private ImageView avatar;

    public Float rating;
    public String comment;
    public String timestamp;

    public RestroomReview(String avatarUrl, String username, Float rating, String comment, String timestamp) {
        this.avatarUrl = avatarUrl;
        this.username = username;
        this.rating = rating;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    public RestroomReview(ImageView avatar, String username, Float rating, String comment, String timestamp) {
        this.username = username;
        this.avatar = avatar;
        this.rating = rating;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ImageView getAvatar() {
        return avatar;
    }

    public void setAvatar(ImageView avatar) {
        this.avatar = avatar;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
