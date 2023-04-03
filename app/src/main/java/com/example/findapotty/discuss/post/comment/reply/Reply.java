package com.example.findapotty.discuss.post.comment.reply;

import com.example.findapotty.model.Item;

public class Reply extends Item {

    private String userName;
    private String userNameReplyTo;
    private String content;

    public Reply(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
