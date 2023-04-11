package com.example.findapotty.reminder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findapotty.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Adapter extends FirebaseRecyclerAdapter<ReminderMessage, Adapter.ViewHolder> {
    public Adapter(@NonNull FirebaseRecyclerOptions<ReminderMessage> options) {
        super(options);
    }

    @Override
    protected void
    onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull ReminderMessage model) {


        holder.labelTV.setText(model.getLabel());

        holder.dateTV.setText(model.getDate());

        holder.timeTV.setText(model.getTime());

        DatabaseReference dbr = FirebaseDatabase.getInstance("https://findapotty-main.firebaseio.com/")
                .getReference().child("reminders");
        final String myKey = getRef(position).getKey();//you can name the myKey whatever you want.
        holder.deleteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbr.child(myKey).removeValue();
            }
        });
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

