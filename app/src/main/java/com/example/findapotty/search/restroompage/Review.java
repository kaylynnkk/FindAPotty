package com.example.findapotty.search.restroompage;

// Create class for review all related data (rating, comment, etc)
// can be referred to as a single object
public class Review {
    public Float rating;
    public String comment;
    public String timestamp;
    public Integer helpfulness;
    public String imgUrl;
    public String userName;

    public Review() {
    }

    public Review(Float rating, String comment, String timestamp, Integer helpfulness, String imgUrl, String userName) {
        this.rating = rating;
        this.comment = comment;
        this.timestamp = timestamp;
        this.helpfulness = helpfulness;
        this.imgUrl = imgUrl;
        this.userName = userName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getUserName() {
        return userName;
    }

    public Float getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Integer getHelpfulness() {
        return helpfulness;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setHelpfulness(Integer helpfulness) {
        this.helpfulness = helpfulness;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
