package com.example.findapotty.search.restroompage;

import android.widget.ImageView;

public class RestroomReview {
    public String restroomId;
    public String reviewId;

    private String avatarUrl;
    private String username;
    private String userid;
    private ImageView avatar;

    public Float rating;
    public String comment;
    public String timestamp;
    public Integer helpfulness;

    public RestroomReview() {
    }
    public RestroomReview(String restroomId, String reviewId, ImageView avatar, String username,
                          String userid, Float rating, String comment, String timestamp, Integer helpfulness) {
        this.restroomId = restroomId;
        this.reviewId = reviewId;
        this.username = username;
        this.userid = userid;
        this.avatar = avatar;
        this.rating = rating;
        this.comment = comment;
        this.timestamp = timestamp;
        this.helpfulness = helpfulness;
    }
    public RestroomReview(String restroomId, String reviewId, String avatarUrl, String username,
                          String userid, Float rating, String comment, String timestamp, Integer helpfulness) {
        this.restroomId = restroomId;
        this.reviewId = reviewId;
        this.avatarUrl = avatarUrl;
        this.username = username;
        this.userid = userid;
        this.rating = rating;
        this.comment = comment;
        this.timestamp = timestamp;
        this.helpfulness = helpfulness;
    }
    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public Integer getHelpfulness() {
        return helpfulness;
    }

    public void setHelpfulness(Integer helpfulness) {
        this.helpfulness = helpfulness;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getRestroomId() {
        return restroomId;
    }

    public void setRestroomId(String restroomId) {
        this.restroomId = restroomId;
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
