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
        articleList.add(new Article("thestreet.com","Cathie Wood Bets Big on a Video Game Company With an Exciting Future","The fund also cut its shares in a popular tech giant. “Out with the old and in with the new” seems to be Ark Investment Management’s strategy as of late. This makes complete sense--the fund is the project of well-known stock market pundit and investor Cathie …",R.drawable.icon1));
        articleList.add(new Article("Alex Misoyannis","BYD Dolphin electric-car arrival pushed back, due later this year alongside Seal sedan","Chinese electric-car giant BYD's next two models – the Dolphin and Seal – are due closer to the end of this year than previously forecast.",R.drawable.icon1));
        articleList.add(new Article("Natasha Lomas","UK's antitrust watchdog announces initial review of generative AI","The UK's antitrust watchdog has announced a review of AI foundational models, such as the tech which underpin OpenAI's ChatGPT and Microsoft's New Bing.",R.drawable.icon1));

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
