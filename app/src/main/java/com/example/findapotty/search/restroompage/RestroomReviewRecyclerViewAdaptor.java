package com.example.findapotty.search.restroompage;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.findapotty.R;

import java.util.ArrayList;

/**
 * Created by User on 1/1/2018.
 */

public class RestroomReviewRecyclerViewAdaptor extends RecyclerView.Adapter<RestroomReviewRecyclerViewAdaptor.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
    private ArrayList<RestroomReview> restroomReviews;
    Context context;

    public RestroomReviewRecyclerViewAdaptor(Context context, ArrayList<RestroomReview> restroomReviews) {
        this.context = context;
        this.restroomReviews = restroomReviews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rr_pg_single_reivew, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        Glide.with(context)
                .asBitmap()
                .load(restroomReviews.get(position).getAvatarUrl())
                .into(holder.avatar);

        holder.username.setText(restroomReviews.get(position).getUsername());

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

    @Override
    public int getItemCount() {
        return restroomReviews.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView username;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.rr_rv_avatar);
            username = itemView.findViewById(R.id.rr_rv_username);
            parentLayout = itemView.findViewById(R.id.restroom_review_item);
        }
    }
}














