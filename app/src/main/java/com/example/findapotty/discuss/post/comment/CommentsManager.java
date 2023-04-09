package com.example.findapotty.discuss.post.comment;

import com.example.findapotty.discuss.DiscussionPostManager;
import com.example.findapotty.model.ItemsManager;

public class CommentsManager extends ItemsManager<Comment> {

    private static CommentsManager instance;

    private CommentsManager() {}

    public static CommentsManager getInstance() {
        if (instance == null) {
            instance = new CommentsManager();
        }
        return instance;
    }
}
