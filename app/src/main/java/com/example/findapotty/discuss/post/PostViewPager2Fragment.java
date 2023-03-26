package com.example.findapotty.discuss.post;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.findapotty.databinding.DiscussionBoardSinglePostBinding;
import com.example.findapotty.discuss.DiscussionPost;
import com.example.findapotty.discuss.post.comment.CommentFragment;
import com.example.findapotty.discuss.post.main.MainFragment;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class PostViewPager2Fragment extends Fragment {

    private DiscussionBoardSinglePostBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DiscussionBoardSinglePostBinding.inflate(inflater, container, false);
        setViewPagerAdapter();
        return binding.getRoot();
    }

    public void setViewPagerAdapter() {
        PostStateAdaptor singlePostStateAdaptor = new PostStateAdaptor(this);
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        DiscussionPost post = PostViewPager2FragmentArgs.fromBundle(getArguments()).getPost();
        fragmentList.add(new MainFragment(post));
        fragmentList.add(new CommentFragment());
        singlePostStateAdaptor.setData(fragmentList);
        binding.dbspPages.setAdapter(singlePostStateAdaptor);
        new TabLayoutMediator(binding.dbspTabs, binding.dbspPages,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Post");
                            break;
                        case 1:
                            tab.setText("Comments");
                            break;
                    }
                }).attach();
    }
}
