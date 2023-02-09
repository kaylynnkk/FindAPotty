package com.example.findapotty;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.findapotty.databinding.DiscussionBoardSinglePostPreviewBinding;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DiscussionPostRecyclerViewAdaptor extends RecyclerView.Adapter<DiscussionPostRecyclerViewAdaptor.ViewHolder>{

    private static final String TAG = "DiscussionPostRecyclerViewAdaptor";
//    private ArrayList<DiscussionPost> discussionPosts;
    Context context;
    private DiscussionBoardSinglePostPreviewBinding binding;
    private DiscussionPostManager discussionPostManager  = DiscussionPostManager.getInstance();

//    public DiscussionPostRecyclerViewAdaptor(Context context, ArrayList<DiscussionPost> discussionPosts) {
    public DiscussionPostRecyclerViewAdaptor(Context context) {
//        this.discussionPosts = discussionPosts;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(context), R.layout.discussion_board_single_post_preview, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        DiscussionPost discussionPost = discussionPostManager.getDiscussionPostByIndex(position);
        Glide.with(context)
                .asBitmap()
                .dontAnimate()
                .load(discussionPost.getUserAvatar())
                .into(holder.userAvatar);
        holder.userName.setText(discussionPost.getUserName());
        holder.postTitle.setText(discussionPost.getPostTitle());
        holder.postContent.setText(discussionPost.getPostContent());
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController controller = Navigation.findNavController(view);
//                NavDirections action =
                DiscussFragmentDirections.ActionNavDiscussToSinglePostDiscussionBoard action =
                        DiscussFragmentDirections.actionNavDiscussToSinglePostDiscussionBoard(discussionPost);
                controller.navigate(action);
            }
        });

    }

    @Override
    public int getItemCount() {
//        return discussionPosts.size();
        return discussionPostManager.getPostCount();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userAvatar;
        TextView userName;
        TextView postTitle;
        TextView postContent;
        RelativeLayout parentLayout;

        public ViewHolder(DiscussionBoardSinglePostPreviewBinding binding) {
            super(binding.getRoot());
            userAvatar = binding.dbsppUserAvatar;
            userName = binding.dbsppUserName;
            postTitle = binding.dbsppTitle;
            postContent = binding.dbsppContent;
            parentLayout = binding.discussionBoardItem;
        }
    }
}
