package com.example.findapotty.developer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findapotty.R;

import java.util.List;

// Adapter bind developer data to views
public class DevAdapter extends RecyclerView.Adapter<DevViewHolder> {
    private List<Developer> devList;

    public DevAdapter(List<Developer> devList) {
        this.devList = devList;
    }

    @NonNull
    @Override
    public DevViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       // Inflate fragment devpage
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_devpage, parent, false);
        // Create a new DevViewHolder object and return it
        return new DevViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DevViewHolder holder, int position) {
        Developer dev = devList.get(position);
        // Set the values for the views in the ViewHolder
        holder.bind(dev);
    }

    @Override
    public int getItemCount() {
        return devList.size();
    }
}
