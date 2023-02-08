package com.example.findapotty;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.findapotty.databinding.DboardSinglePostBinding;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DiscussionPostRecyclerViewAdaptor extends RecyclerView.Adapter<DiscussionPostRecyclerViewAdaptor.ViewHolder>{

    private static final String TAG = "DiscussionPostRecyclerViewAdaptor";
    private ArrayList<DiscussionPost> discussionPosts;
    Context context;
    private DboardSinglePostBinding binding;

    public DiscussionPostRecyclerViewAdaptor(Context context, ArrayList<DiscussionPost> discussionPosts) {
        this.discussionPosts = discussionPosts;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(context), R.layout.dboard_single_post, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context)
                .asBitmap()
//                .dontAnimate()
                .load(discussionPosts.get(position).getUserAvatar())
                .into(holder.userAvatar);
        holder.userName.setText(discussionPosts.get(position).getUserName());

    }

    @Override
    public int getItemCount() {
        return discussionPosts.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userAvatar;
        TextView userName;
        TextView postTitle;
        TextView postContent;
        RelativeLayout parentLayout;

        public ViewHolder(DboardSinglePostBinding binding) {
            super(binding.getRoot());
            userAvatar = binding.dbSpUserAvatar;
            userName = binding.dbSpUserName;
            postTitle = binding.dbSpTitle;
            postContent = binding.dbSpContent;
            parentLayout = binding.discussionBoardItem;
        }
    }
}
