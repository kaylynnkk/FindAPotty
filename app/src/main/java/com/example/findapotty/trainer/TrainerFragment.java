package com.example.findapotty.trainer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.findapotty.R;

import java.util.ArrayList;
import java.util.List;


public class TrainerFragment extends Fragment {
    private View rootView;
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater , @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflate the recycler layout for this fragment
        rootView = inflater.inflate(R.layout.trainer_recyclerview, container, false);

        // Create list of trainers
        List<Trainer> trainerList = new ArrayList<>();
        trainerList.add(new Trainer(R.drawable.lightbulb, "Offer plenty of fluids", "Make sure your child drinks enough water and other fluids to keep them hydrated."));
        trainerList.add(new Trainer(R.drawable.lightbulb, "Praise your child for their progress", "Celebrate every milestone and progress your child makes in potty training."));
        trainerList.add(new Trainer(R.drawable.lightbulb, "Be consistent", "Establish a consistent routine for using the potty and stick to it. Encourage your child to use the potty at regular intervals, such as after meals and before bedtime."));
        trainerList.add(new Trainer(R.drawable.lightbulb, "Be patient", "Potty training can take time and there will likely be setbacks along the way. Be patient and don't get discouraged if progress is slow."));
        trainerList.add(new Trainer(R.drawable.lightbulb, "Teach proper hygiene", "Teach your child how to wipe properly and wash their hands after using the potty."));
        trainerList.add(new Trainer(R.drawable.lightbulb, "Consider using training pants", "Training pants can be a helpful transition from diapers to underwear, as they allow your child to feel wetness without the mess of an accident."));

        // Create a new traineradapter with the list of trainers
        TrainerAdapter trainerAdapter = new TrainerAdapter(trainerList);

        // Get a reference to the recycler view and set the adapter
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerview);
        recyclerView.setAdapter(trainerAdapter);

        // Set the layout manager for the recyclerview
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return rootView;
    }
}