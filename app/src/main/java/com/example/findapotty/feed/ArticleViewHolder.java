package com.example.findapotty.feed;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findapotty.R;

// Hold the views for each item in the Recycler View
public class ArticleViewHolder extends RecyclerView.ViewHolder {
    public ImageView image;
    public TextView title,author, description;

    public ArticleViewHolder(@NonNull View itemView) {
        super(itemView);

        // Get references to the views in the ViewHolder
        title = itemView.findViewById(R.id.title);
        author = itemView.findViewById(R.id.author);
        description = itemView.findViewById(R.id.description);
        image = itemView.findViewById(R.id.image);

    }

    // Bind the data in dev using methods in developer class
    public void bind(Article article) {
        image.setImageResource(article.getImage());
        title.setText(article.getTitle());
        author.setText(article.getAuthor());
        description.setText(article.getDescription());
    }
}

