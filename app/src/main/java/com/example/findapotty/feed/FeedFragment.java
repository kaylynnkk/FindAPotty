package com.example.findapotty.feed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findapotty.R;

import java.util.List;

public class FeedFragment extends Fragment {
    private RecyclerView articlesRecyclerView;
    private ArticleAdapter articleAdapter;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //feed page to be displayed after user pressing the "Feed" button on the homepage
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);

        //locate the articles section to be displayed by its id "articles section" on the fragment feed class
        //adapter class is to be used
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        articlesRecyclerView = view.findViewById(R.id.article_section);
        articlesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        articleAdapter = new ArticleAdapter(getArticles());
        articlesRecyclerView.setAdapter(articleAdapter);

        return rootView;
    }

    //Retrieve data for each article
    private List<Article> getArticles() {

        return null;
    }
}
