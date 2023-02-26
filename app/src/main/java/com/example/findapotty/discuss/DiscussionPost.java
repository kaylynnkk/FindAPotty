package com.example.findapotty.discuss;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;

public class DiscussionPost implements Parcelable {

    private String userAvatar;
    private String userName;

    private String postTitle;
    private String postContent;

    private String postTag;
    private int postCommentCount;
    private int postLikeCount;


    public DiscussionPost() {}

    public DiscussionPost(String userAvatar, String userName, String postTitle, String postContent) {
        this.userAvatar = userAvatar;
        this.userName = userName;
        this.postTitle = postTitle;
        this.postContent = postContent;
    }

    protected DiscussionPost(Parcel in) {
        userAvatar = in.readString();
        userName = in.readString();
        postTitle = in.readString();
        postContent = in.readString();
        postTag = in.readString();
        postCommentCount = in.readInt();
        postLikeCount = in.readInt();
    }

    public static final Creator<DiscussionPost> CREATOR = new Creator<DiscussionPost>() {
        @Override
        public DiscussionPost createFromParcel(Parcel in) {
            return new DiscussionPost(in);
        }

        @Override
        public DiscussionPost[] newArray(int size) {
            return new DiscussionPost[size];
        }
    };

    @Exclude
    public String getUserAvatar() {
        return userAvatar;
    }

    @Exclude
    public String getUserName() {
        return userName;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public String getPostContent() {
        return postContent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(userAvatar);
        parcel.writeString(userName);
        parcel.writeString(postTitle);
        parcel.writeString(postContent);
        parcel.writeString(postTag);
        parcel.writeInt(postCommentCount);
        parcel.writeInt(postLikeCount);
    }
}
