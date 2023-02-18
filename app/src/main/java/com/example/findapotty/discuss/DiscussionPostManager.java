package com.example.findapotty.discuss;

import java.util.ArrayList;

public class DiscussionPostManager {

    private static DiscussionPostManager instance;
    private ArrayList<DiscussionPost> discussionPosts = new ArrayList<>();
    private int maxPostCount = 4;

    private DiscussionPostManager() {
    }

    public static DiscussionPostManager getInstance() {
        if (instance == null) {
            instance = new DiscussionPostManager();
        }
        return instance;
    }

    public ArrayList<DiscussionPost> getDiscussionPosts() {
        return discussionPosts;
    }

    public DiscussionPost getDiscussionPostByIndex(int index) {
        return discussionPosts.get(index);
    }

    public void addDiscussionPost(DiscussionPost discussionPost, DiscussionPostRecyclerViewAdaptor adaptor) {
        discussionPosts.add(0, discussionPost);
        if (discussionPosts.size() > maxPostCount) {
            discussionPosts.remove(discussionPosts.size() - 1);
            adaptor.notifyItemRemoved(discussionPosts.size() - 1);
        }
    }

    public void removeDiscussionPost(DiscussionPost discussionPost, DiscussionPostRecyclerViewAdaptor adaptor) {
        discussionPosts.remove(discussionPost);
    }

    public void clearDiscussionPosts(DiscussionPostRecyclerViewAdaptor adaptor) {
        discussionPosts.clear();
    }

    public int getPostCount() {
        return discussionPosts.size();
    }
}
