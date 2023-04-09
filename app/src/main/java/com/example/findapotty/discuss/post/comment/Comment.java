package com.example.findapotty.discuss.post.comment;

import com.example.findapotty.discuss.post.comment.reply.RepliesManager;
import com.example.findapotty.model.Item;
import com.google.firebase.database.Exclude;

public class Comment extends Item {

    private String uploadTime;
    private String userAvatar;
    private String userName;
    private String content;
    private RepliesManager repliesManager = new RepliesManager();

    public Comment(String uploadTime, String userName, String content) {
        this.uploadTime = uploadTime;
        this.userName = userName;
        this.content = content;
    }

    public Comment() {}

    public String getUploadTime() {
        return uploadTime;
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

    @Exclude
    public RepliesManager getRepliesManager() {
        return repliesManager;
    }
}
