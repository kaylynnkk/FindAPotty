package com.example.findapotty.reminder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findapotty.R;
import com.example.findapotty.search.restroompage.RestroomReview;
import com.example.findapotty.search.restroompage.RestroomReviewRecyclerViewAdaptor;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ReminderRecyclerViewAdaptor extends RecyclerView.Adapter<ReminderRecyclerViewAdaptor.ViewHolder> {

    ArrayList<ReminderMessage> reminders;
    Context context;

    public ReminderRecyclerViewAdaptor(Context context, ArrayList<ReminderMessage> reminders) {
        this.context = context;
        this.reminders = reminders;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ReminderMessage reminderObj = reminders.get(position);

        holder.labelTV.setText(reminderObj.getLabel()+", ");

        holder.dateTV.setText(reminderObj.getDate());

        holder.timeTV.setText(reminderObj.getTime());
/*
        DatabaseReference dbr = FirebaseDatabase.getInstance("https://findapotty-main.firebaseio.com/")
                .getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("reminders");

 */
        DatabaseReference dbr = FirebaseDatabase.getInstance("https://findapotty-main.firebaseio.com/")
                .getReference().child("reminders");
        holder.deleteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbr.child(reminderObj.getReminderId()).removeValue();
            }
        });
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reminder_single_item, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView labelTV, dateTV, timeTV;
        ImageView deleteIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            labelTV = (TextView) itemView.findViewById(R.id.label);
            dateTV = (TextView) itemView.findViewById(R.id.date);
            timeTV = (TextView) itemView.findViewById(R.id.time);
            deleteIV = (ImageView) itemView.findViewById(R.id.remove);
        }
    }
}

