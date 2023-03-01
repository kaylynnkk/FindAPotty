package com.example.findapotty.discuss;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Iterator;

public class DiscussionPostManager {

    private static DiscussionPostManager instance;
    private ArrayList<DiscussionPost> discussionPosts = new ArrayList<>();
    private int maxPostCount = 100;
    public boolean onInit = true;
    public int currentStartIndex = 0;
    public Iterator<DataSnapshot> iterator = null;

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
        adaptor.notifyItemInserted(0);
        if (discussionPosts.size() > maxPostCount) {
            discussionPosts.remove(discussionPosts.size() - 1);
            adaptor.notifyItemRemoved(discussionPosts.size() - 1);
        }
    }

    public void removeDiscussionPost(DiscussionPost discussionPost, DiscussionPostRecyclerViewAdaptor adaptor) {
        discussionPosts.remove(discussionPost);
    }

    public void clearDiscussionPosts(DiscussionPostRecyclerViewAdaptor adaptor) {
        int size = discussionPosts.size();
        discussionPosts.clear();
        adaptor.notifyItemRangeRemoved(0, size);
    }

    public int getPostCount() {
        return discussionPosts.size();
    }
}
