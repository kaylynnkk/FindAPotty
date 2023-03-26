package com.example.findapotty.discuss.post.comment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findapotty.databinding.DiscussionBoardSinglePostSingleCommentBinding;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentRecyclerViewAdaptor extends RecyclerView.Adapter<CommentRecyclerViewAdaptor.ViewHolder> {

    private static final String TAG = "DiscussionPostRecyclerViewAdaptor";
    Context context;
    private DiscussionBoardSinglePostSingleCommentBinding binding;
    private CommentsManager discussionPostManager = CommentsManager.getInstance();

    public CommentRecyclerViewAdaptor(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DiscussionBoardSinglePostSingleCommentBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = CommentsManager.getInstance().getItemByIndex(position);
        holder.userName.setText(comment.getUserName());
        holder.content.setText(comment.getContent());
        holder.dateTime.setText(comment.getUploadTime());
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
        ConstraintLayout parentLayout;

        public ViewHolder(DiscussionBoardSinglePostSingleCommentBinding binding) {
            super(binding.getRoot());
            userAvatar = binding.dbspscUserAvatar;
            userName = binding.dbspscUserName;
            content = binding.dbspscContent;
            dateTime = binding.dbspscDateTime;
            parentLayout = binding.dbspscComment;
        }
    }
}
