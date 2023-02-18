package com.example.findapotty.feed;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.findapotty.R;

import java.util.List;


// Adapter to be used for inflating the article layout for each article and displaying article's title, author, description, and image
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    // Retrieves info from article class and put into an array
    private List<Article> articles;

    public ArticleAdapter(List<Article> articles) {
        this.articles = articles;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_feed, parent, false);
        return new ArticleViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Article article = articles.get(position);
        holder.article_title.setText(article.getTitle());
        holder.author.setText(article.getAuthor());
        holder.description.setText(article.getDescription());
        //holder.image.setImageBitmap(article.getUrlToImage());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return articles.size();
    }

    public static class ArticleViewHolder extends RecyclerView.ViewHolder {

        private TextView article_title;
        private TextView author;
        private TextView description;
        private ImageView image;

        //locate id location for title, author, description ,and image
        public ArticleViewHolder(View itemView) {
            super(itemView);
            article_title = itemView.findViewById(R.id.article_title);
            author = itemView.findViewById(R.id.author);
            description = itemView.findViewById(R.id.description);
            image = itemView.findViewById(R.id.article_photo);
        }
    }
}
