package com.example.findapotty.search.restroompage;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.findapotty.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;

/**
 * Created by User on 1/1/2018.
 */

public class RestroomReviewRecyclerViewAdaptor extends FirebaseRecyclerAdapter<RestroomReview,
        RestroomReviewRecyclerViewAdaptor.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
    Context context;

    public RestroomReviewRecyclerViewAdaptor(@NonNull FirebaseRecyclerOptions<RestroomReview> options) {
        super(options);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rr_pg_single_reivew, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull RestroomReview model) {
      /*  Glide.with(context)
                .asBitmap()
                .load(model.getAvatarUrl())
                .into(holder.avatar);

       */

        holder.username.setText(model.getUsername());
        holder.date.setText(model.getTimestamp());
        holder.rating.setRating(model.getRating());
        holder.comment.setText(model.getComment());
        //        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "onClick: clicked on: " + mImageNames.get(position));
//
//                Toast.makeText(mContext, mImageNames.get(position), Toast.LENGTH_SHORT).show();
//
//                Intent intent = new Intent(mContext, GalleryActivity.class);
//                intent.putExtra("image_url", mImages.get(position));
//                intent.putExtra("image_name", mImageNames.get(position));
//                mContext.startActivity(intent);
//            }
//        });
    }
/*
    @Override
    public int getItemCount() {
        return restroomReviews.size();
    }


 */

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView username, date, comment;
        RelativeLayout parentLayout;
        RatingBar rating;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.rr_rv_avatar);
            username = itemView.findViewById(R.id.rr_rv_username);
            parentLayout = itemView.findViewById(R.id.restroom_review_item);
            rating = itemView.findViewById(R.id.rr_rv_rating);
            date = itemView.findViewById(R.id.rr_rv_timestamp);
            comment = itemView.findViewById(R.id.rr_rv_review);

        }
    }
}














