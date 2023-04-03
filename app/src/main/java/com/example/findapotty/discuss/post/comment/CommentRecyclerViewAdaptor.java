package com.example.findapotty.discuss.post.comment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findapotty.databinding.DiscussionBoardSinglePostSingleCommentPreviewBinding;
import com.example.findapotty.discuss.post.comment.reply.RepliesManager;
import com.example.findapotty.discuss.post.comment.reply.ReplyRecyclerViewAdaptor;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentRecyclerViewAdaptor extends RecyclerView.Adapter<CommentRecyclerViewAdaptor.ViewHolder> {

    private static final String TAG = "CommentRecyclerViewAdaptor";
    Context context;
    private DiscussionBoardSinglePostSingleCommentPreviewBinding binding;
    private CommentsManager discussionPostManager = CommentsManager.getInstance();

    public CommentRecyclerViewAdaptor(Context context) {
        this.context = context;
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
