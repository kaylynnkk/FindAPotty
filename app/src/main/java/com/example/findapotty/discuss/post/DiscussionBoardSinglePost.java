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
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class DiscussionBoardSinglePost extends Fragment {

    private DiscussionBoardSinglePostBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DiscussionBoardSinglePostBinding.inflate(inflater, container, false);
        setViewPagerAdapter();
        return binding.getRoot();
    }

    public void setViewPagerAdapter() {
        SinglePostStateAdaptor singlePostStateAdaptor = new SinglePostStateAdaptor(this);
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        DiscussionPost post = DiscussionBoardSinglePostArgs.fromBundle(getArguments()).getPost();
        fragmentList.add(new DiscussionBoardSinglePostMain(post));
        fragmentList.add(new DiscussionBoardSinglePostComments());
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
