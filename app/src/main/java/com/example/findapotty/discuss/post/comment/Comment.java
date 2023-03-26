package com.example.findapotty.discuss.post.comment;

import com.example.findapotty.model.Item;

public class Comment extends Item {

    private String dateTime;
    private String userAvatar;
    private String userName;
    private String content;

    public Comment(String dateTime, String userName, String content) {
        this.dateTime = dateTime;
        this.userName = userName;
        this.content = content;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public String getUserName() {
        return userName;
    }

    public String getContent() {
        return content;
    }
}
