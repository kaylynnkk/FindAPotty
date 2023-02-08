package com.example.findapotty;

import de.hdodenhof.circleimageview.CircleImageView;

public class DiscussionPost {

    private String userAvatar;
    private String userName;

    private String postTitle;
    private String postContent;

    private String postTag;
    private int postCommentCount;
    private int postLikeCount;

    public DiscussionPost(String userAvatar, String userName, String postTitle, String postContent) {
        this.userAvatar = userAvatar;
        this.userName = userName;
        this.postTitle = postTitle;
        this.postContent = postContent;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public String getUserName() {
        return userName;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public String getPostContent() {
        return postContent;
    }
}
