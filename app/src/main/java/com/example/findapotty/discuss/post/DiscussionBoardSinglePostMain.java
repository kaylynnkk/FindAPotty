package com.example.findapotty.discuss.post;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.findapotty.R;
import com.example.findapotty.databinding.DiscussionBoardSinglePostMainBinding;
import com.example.findapotty.discuss.DiscussionPost;


public class DiscussionBoardSinglePostMain extends Fragment {

    private DiscussionBoardSinglePostMainBinding binding;
    private final DiscussionPost discussionPost;

    public DiscussionBoardSinglePostMain(DiscussionPost post) {
        discussionPost = post;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.discussion_board_single_post_main, container, false);
        initPostPage();
        return binding.getRoot();
    }

    private void initPostPage() {
        requireActivity().setTitle(discussionPost.getPostTitle());
        Glide.with(getContext())
                .asBitmap()
                .dontAnimate()
                .load(discussionPost.getUserAvatar())
                .into(binding.dbspmUserAvatar);
        binding.dbspmUserName.setText(discussionPost.getUserName());
        binding.dbspmPostTitle.setText(discussionPost.getPostTitle());
        binding.dbspmPostContent.setText(discussionPost.getPostContent());

    }

}
