package com.example.findapotty.discuss.post;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.findapotty.databinding.DiscussionBoardSinglePostCommentsBinding;

public class DiscussionBoardSinglePostComments extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DiscussionBoardSinglePostCommentsBinding binding =
                DiscussionBoardSinglePostCommentsBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}
