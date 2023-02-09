package com.example.findapotty;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.findapotty.databinding.DiscussionBoardSinglePostBinding;


public class SinglePostDiscussionBoard extends Fragment {

    private DiscussionBoardSinglePostBinding binding;
    private DiscussionPost discussionPost;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.discussion_board_single_post, container, false);

        discussionPost = SinglePostDiscussionBoardArgs.fromBundle(getArguments()).getDisuccionPost();
        initPostPage();

        return binding.getRoot();
    }

    private void initPostPage() {
        Glide.with(getContext())
                .asBitmap()
                .dontAnimate()
                .load(discussionPost.getUserAvatar())
                .into(binding.dbspUserAvatar);
//        binding.dbspUserAvatar.setImageURI(Uri.parse(discussionPost.getUserAvatar()));
        binding.dbspUserName.setText(discussionPost.getUserName());
        binding.dbspPostTitle.setText(discussionPost.getPostTitle());
        binding.dbspPostContent.setText(discussionPost.getPostContent());

    }
}
