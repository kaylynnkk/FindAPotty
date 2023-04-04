package com.example.findapotty.discuss.post;

import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;

public class CommentBoxManager {

    private EditText editText;
    private ImageView postActionImageView;
    private InputMethodManager inputMethodManager;
    private DatabaseReference databaseReference;
    private Boolean onComment = true; // false: onReply
    private String postId;
    private Boolean onPost;
    private String commentId;


    public CommentBoxManager(EditText editText, ImageView postActionImageView, InputMethodManager imm) {
        this.editText = editText;
        this.postActionImageView = postActionImageView;
        this.inputMethodManager = imm;
    }

    public void post() {
        if (onComment) { // post comment
            ;
        } else { // post reply
            ;
        }

    }

    public EditText getEditText() {
        return editText;
    }

    public ImageView getPostActionImageView() {
        return postActionImageView;
    }

    public InputMethodManager getInputMethodManager() {
        return inputMethodManager;
    }

    public String getPostId() {
        return postId;
    }

    public String getCommentId() {
        return commentId;
    }

    public Boolean getOnPost() {
        return onPost;
    }

    public Boolean getOnComment() {
        return onComment;
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public void setDatabaseReference(DatabaseReference ref) {
        this.databaseReference = ref;
    }

    public void setOnComment(Boolean onComment) {
        this.onComment = onComment;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public void setOnPost(Boolean onPost) {
        this.onPost = onPost;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

}
