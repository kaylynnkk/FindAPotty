package com.example.findapotty.developer;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findapotty.R;

// Hold the views for each item in the Recycler View
public class DevViewHolder extends RecyclerView.ViewHolder {
    public ImageView devImage;
    public TextView devName;
    public TextView devSchool;
    public TextView devEmail;
    public TextView devQuote;

    public DevViewHolder(@NonNull View itemView) {
        super(itemView);

        // Get references to the views in the ViewHolder
        devImage = itemView.findViewById(R.id.dev_image);
        devName = itemView.findViewById(R.id.dev_name);
        devSchool = itemView.findViewById(R.id.dev_school);
        devEmail = itemView.findViewById(R.id.dev_email);
        devQuote = itemView.findViewById(R.id.dev_quote);
    }

    // Bind the data in dev using methods in developer class
    public void bind(Developer dev) {
        devImage.setImageResource(dev.getImage());
        devName.setText(dev.getName());
        devSchool.setText(dev.getSchool());
        devEmail.setText(dev.getEmail());
        devQuote.setText(dev.getQuote());
    }
}
