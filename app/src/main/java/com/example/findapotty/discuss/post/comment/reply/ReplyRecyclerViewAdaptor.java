package com.example.findapotty.discuss.post.comment.reply;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findapotty.databinding.DiscussionBoardSinglePostSingleCommentSingleReplyPreviewBinding;

public class ReplyRecyclerViewAdaptor extends RecyclerView.Adapter<ReplyRecyclerViewAdaptor.ViewHolder> {

    private static final String TAG = "ReplyRecyclerViewAdaptor";
    Context context;
    private RepliesManager repliesManager;
    private DiscussionBoardSinglePostSingleCommentSingleReplyPreviewBinding binding;

    public ReplyRecyclerViewAdaptor(Context context, RepliesManager repliesManager) {
        this.context = context;
        this.repliesManager = repliesManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DiscussionBoardSinglePostSingleCommentSingleReplyPreviewBinding.inflate(
                LayoutInflater.from(context), parent, false);
        return new ReplyRecyclerViewAdaptor.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reply reply = repliesManager.getItemByIndex(position);
        SpannableStringBuilder content = new SpannableStringBuilder(
                reply.getCreatorName() + ": " + reply.getContent()
        );
        if (reply.getCreatorName() != null) {
            content.setSpan(
                    new ForegroundColorSpan(Color.parseColor("#008ac5")),
                    0,
                    reply.getCreatorName().length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
            content.setSpan(
                    new ForegroundColorSpan(Color.BLACK),
                    reply.getCreatorName().length(),
                    content.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }
        holder.content.setText(content);
    }

    @Override
    public int getItemCount() {
        return repliesManager.getCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        RelativeLayout parentLayout;

        public ViewHolder(DiscussionBoardSinglePostSingleCommentSingleReplyPreviewBinding binding) {
            super(binding.getRoot());
            content = binding.dbspscsrpContent;
            parentLayout = binding.dbspscsrpReply;
        }
    }
}
