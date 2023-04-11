package com.example.findapotty.trainer;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findapotty.R;

// Hold the views for each item in the Recycler View
public class TrainerViewHolder extends RecyclerView.ViewHolder {
    public ImageView trainerImage;
    public TextView trainerTitle, trainerDescription;

    public TrainerViewHolder(@NonNull View itemView) {
        super(itemView);

        // Get references to the views in the ViewHolder
        trainerImage = itemView.findViewById(R.id.trainer_image);
        trainerTitle = itemView.findViewById(R.id.trainer_title);
        trainerDescription = itemView.findViewById(R.id.trainer_description);

    }

    // Bind the data in trainer using methods in developer class
    public void bind(Trainer trainer) {
        trainerImage.setImageResource(trainer.getImage());
        trainerTitle.setText(trainer.getTitle());
        trainerDescription.setText(trainer.getDescription());
    }
}
