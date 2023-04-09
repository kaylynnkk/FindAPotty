package com.example.findapotty.discuss.post.comment.reply;

import com.example.findapotty.model.Item;
import com.example.findapotty.user.User;

public class Reply extends Item {

    private String creatorName = User.getInstance().getUserName();
    private String parentReplyId;
    private String content;

    public Reply() {}
    public Reply(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public String getCreatorName() {
        return creatorName;
    }
}
