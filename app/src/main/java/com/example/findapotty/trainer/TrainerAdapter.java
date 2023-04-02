package com.example.findapotty.trainer;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findapotty.R;


import java.util.List;

// Adapter bind trainer data to views
public class TrainerAdapter extends RecyclerView.Adapter<TrainerViewHolder> {
    private List<Trainer> trainerList;

    public TrainerAdapter(List<Trainer> trainerList) {
        this.trainerList = trainerList;
    }

    @NonNull
    @Override
    public TrainerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate fragment trainer
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_trainer, parent, false);
        // Create a new TrainerViewHolder object and return it
        return new TrainerViewHolder(itemView);
    }

    // Assigning values to the views in the recycler view based on position
    @Override
    public void onBindViewHolder(@NonNull TrainerViewHolder holder, int position) {
        Trainer trainer = trainerList.get(position);
        // Set the values for the views in the ViewHolder
        holder.bind(trainer);
    }

    // Number of items to be displayed
    @Override
    public int getItemCount() {
        return trainerList.size();
    }
}

