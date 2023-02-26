package com.example.findapotty.discuss;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
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
import com.example.findapotty.discuss.DiscussFragmentDirections;
import com.example.findapotty.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class DiscussionPostRecyclerViewAdaptor extends FirebaseRecyclerAdapter<DiscussionPost, DiscussionPostRecyclerViewAdaptor.ViewHolder> {
    private static final String TAG = "DiscussionPostRecyclerViewAdaptor";
    Context context;
    private DiscussionBoardSinglePostPreviewBinding binding;
    public DiscussionPostRecyclerViewAdaptor(Context context, FirebaseRecyclerOptions<DiscussionPost> options) {
        super(options);
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
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull DiscussionPost discussionPost) {

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
                NavDirections action =
                        DiscussFragmentDirections.actionNavDiscussToSinglePostDiscussionBoard(discussionPost);
                controller.navigate(action);
            }
        });

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
