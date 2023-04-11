package com.example.findapotty.feed;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findapotty.R;
import com.example.findapotty.feed.Article;

import java.util.List;

// Adapter bind developer data to views
public class ArticleAdapter extends RecyclerView.Adapter<ArticleViewHolder> {
    private List<Article> articleList;

    public ArticleAdapter(List<Article> articleList) {
        this.articleList = articleList;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate fragment devpage
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_feed, parent, false);
        // Create a new DevViewHolder object and return it
        return new ArticleViewHolder(itemView);
    }

    // Assigning values to the views in the recycler view based on position
    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = articleList.get(position);
        // Set the values for the views in the ViewHolder
        holder.bind(article);
    }

    // Number of items to be displayed
    @Override
    public int getItemCount() {
        return articleList.size();
    }
}
