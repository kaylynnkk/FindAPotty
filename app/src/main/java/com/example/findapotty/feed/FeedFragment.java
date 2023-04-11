package com.example.findapotty.feed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.findapotty.R;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class FeedFragment extends Fragment{

    private View rootView;
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater , @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflate the devpage recycler layout for this fragment
        rootView = inflater.inflate(R.layout.devpage_recyclerview, container, false);

        // Create list of developers
        List<Article> articleList = new ArrayList<>();
        articleList.add(new Article("Michelle Lewis","Wind and solar achieve a record high of 12% of global electricity in 2022","Wind and solar reached a record 12% of global electricity in 2022, up from 10% in 2021, according to a report launched today by energy think tank Ember – and experts predict that power sector emissions have peaked.",R.drawable.icon1));

        // Create a new articleadapter with the list of developers
        ArticleAdapter articleAdapter = new ArticleAdapter(articleList);

        // Get a reference to the recycler view and set the adapter
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(articleAdapter);

        // Set the layout manager for the recyclerview
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }
}
