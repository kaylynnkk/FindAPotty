package com.example.findapotty.search.restroompage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findapotty.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by User on 1/1/2018.
 */
 // Adapter to populate recycler view usinf firebase data
public class RestroomReviewRecyclerViewAdaptor extends RecyclerView.Adapter<RestroomReviewRecyclerViewAdaptor.ViewHolder>{

    ArrayList<RestroomReview> reviewsList = new ArrayList<>();
    Context context;
    boolean previouslyClicked = false;
    Integer currHelpVal;

    public RestroomReviewRecyclerViewAdaptor(Context context, ArrayList<RestroomReview> reviewsList) {
        this.context = context;
        this.reviewsList = reviewsList;
    }
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rr_pg_single_reivew, parent, false);
        return new ViewHolder(view);
    }


    public void onBindViewHolder(ViewHolder holder, int position) {
      /*  Glide.with(context)
                .asBitmap()
                .load(model.getAvatarUrl())
                .into(holder.avatar);e

       */
        RestroomReview reviewObj = reviewsList.get(position);

        holder.username.setText(reviewObj.getUsername());
        holder.date.setText(reviewObj.getTimestamp());
        holder.rating.setRating(reviewObj.getRating());
        holder.ratingNum.setText(""+reviewObj.getRating());
        holder.comment.setText(reviewObj.getComment());
        holder.helpfulnessTV.setText("Helpfulness ("+reviewObj.getHelpfulness()+")");
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
        DatabaseReference dbr = FirebaseDatabase.getInstance("https://findapotty-main.firebaseio.com/")
                .getReference("Reminders/"+ reviewObj.getRestroomId());
        String key = reviewObj.getReviewId();
        holder.helpfulnessBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(previouslyClicked){
                    holder.helpfulnessBT.setImageResource(R.drawable.baseline_thumb_up_off_alt_24);
                    currHelpVal = reviewObj.getHelpfulness()-1;
                    reviewObj.setHelpfulness(currHelpVal);
                    holder.helpfulnessTV.setText("Helpfulness ("+currHelpVal+")");
                    dbr.child(key).child("helpfulness").setValue(currHelpVal);
                    previouslyClicked = false;


                }
                else{
                    holder.helpfulnessBT.setImageResource(R.drawable.baseline_thumb_up_alt_24);
                    currHelpVal = reviewObj.getHelpfulness()+1;
                    reviewObj.setHelpfulness(currHelpVal);
                    holder.helpfulnessTV.setText("Helpfulness ("+currHelpVal+")");
                    dbr.child(key).child("helpfulness").setValue(currHelpVal);
                    previouslyClicked = true;


                }
            }
        });
    }

    public int getItemCount()
    {
        return reviewsList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView username, date, comment, ratingNum, helpfulnessTV;
        RelativeLayout parentLayout;
        RatingBar rating;
        ImageView helpfulnessBT;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.rr_rv_avatar);
            username = itemView.findViewById(R.id.rr_rv_username);
            parentLayout = itemView.findViewById(R.id.restroom_review_item);
            rating = itemView.findViewById(R.id.rr_rv_rating);
            ratingNum = itemView.findViewById(R.id.rating_text);
            date = itemView.findViewById(R.id.rr_rv_timestamp);
            comment = itemView.findViewById(R.id.rr_rv_review);
            helpfulnessBT = itemView.findViewById(R.id.rr_rv_helpfulness);
            helpfulnessTV = itemView.findViewById(R.id.helpful_text);


        }
    }
}














