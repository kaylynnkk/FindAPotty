package com.example.findapotty.discuss.post.comment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findapotty.databinding.DiscussionBoardSinglePostBinding;
import com.example.findapotty.databinding.DiscussionBoardSinglePostSingleCommentPreviewBinding;
import com.example.findapotty.discuss.post.CommentBoxManager;
import com.example.findapotty.discuss.post.comment.reply.RepliesManager;
import com.example.findapotty.discuss.post.comment.reply.ReplyRecyclerViewAdaptor;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentRecyclerViewAdaptor extends RecyclerView.Adapter<CommentRecyclerViewAdaptor.ViewHolder> {

    private static final String TAG = "CommentRecyclerViewAdaptor";
    Context context;
    private DiscussionBoardSinglePostSingleCommentPreviewBinding binding;
    private DiscussionBoardSinglePostBinding postBinding;
    private InputMethodManager imm;
    private CommentBoxManager commentBoxManager;

    public CommentRecyclerViewAdaptor(Context context, CommentBoxManager commentBoxManager) {
        this.context = context;
        this.commentBoxManager = commentBoxManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DiscussionBoardSinglePostSingleCommentPreviewBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = CommentsManager.getInstance().getItemByIndex(position);
        holder.userName.setText(comment.getUserName());
        holder.content.setText(comment.getContent());
        holder.dateTime.setText(comment.getUploadTime());
        // set reply recycler view
        holder.replySection.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        holder.replySection.setAdapter(new ReplyRecyclerViewAdaptor(
                binding.getRoot().getContext(), comment.getRepliesManager()));
        // https://stackoverflow.com/a/28596779
        holder.content.setOnClickListener(view -> {
            //
            EditText commentBox = commentBoxManager.getEditText();
            commentBox.requestFocus();
            commentBoxManager.getInputMethodManager()
                    .toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            commentBoxManager.setOnComment(false);
            commentBoxManager.setDatabaseReference(
                    FirebaseDatabase.getInstance().getReference("discussion_posts")
                            .child(commentBoxManager.getPostId()).child("comments").child(comment.getId()).child("replies")
            );
            commentBox.setHint("Replying to " + comment.getId());
        });
    }

    @Override
    public int getItemCount() {
        return CommentsManager.getInstance().getCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userAvatar;
        TextView userName;
        TextView content;
        TextView dateTime;
        RecyclerView replySection;
        ConstraintLayout parentLayout;

        public ViewHolder(DiscussionBoardSinglePostSingleCommentPreviewBinding binding) {
            super(binding.getRoot());
            userAvatar = binding.dbspscUserAvatar;
            userName = binding.dbspscUserName;
            content = binding.dbspscContent;
            dateTime = binding.dbspscDateTime;
            replySection = binding.dbspscReplySection;
            parentLayout = binding.dbspscComment;
        }
    }
}
