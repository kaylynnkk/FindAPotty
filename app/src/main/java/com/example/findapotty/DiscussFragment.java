package com.example.findapotty;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findapotty.databinding.FragmentDiscussBinding;

import java.util.ArrayList;

public class DiscussFragment extends Fragment {

    private FragmentDiscussBinding binding;
    private RecyclerView recyclerView;
    private DiscussionPostRecyclerViewAdaptor adaptor;
    private ArrayList<DiscussionPost> discussionPosts = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_discuss, container, false);
        binding.setLifecycleOwner(this);

        recyclerView = binding.fdDicusssionSection;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adaptor = new DiscussionPostRecyclerViewAdaptor(getContext(), discussionPosts);
        recyclerView.setAdapter(adaptor);

        initDiscussionBoard();


        return binding.getRoot();
    }

    private void initDiscussionBoard(){
        discussionPosts.add(new DiscussionPost(
                "https://i.redd.it/tpsnoz5bzo501.jpg", "Trondheim1",
                "Some title",
                "Some content"
                ));
        discussionPosts.add(new DiscussionPost(
                "https://i.redd.it/tpsnoz5bzo501.jpg", "Trondheim1",
                "Some title",
                "Some content"
                ));
        discussionPosts.add(new DiscussionPost(
                "https://i.redd.it/tpsnoz5bzo501.jpg", "Trondheim1",
                "Some title",
                "Some content"
                ));

    }
}
