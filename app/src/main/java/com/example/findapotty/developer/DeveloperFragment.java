package com.example.findapotty.developer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.findapotty.R;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class DeveloperFragment extends Fragment{

    private View rootView;
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater , @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflate the devpage recycler layout for this fragment
        rootView = inflater.inflate(R.layout.devpage_recyclerview, container, false);

        // Create list of developers
        List<Developer> devList = new ArrayList<>();
        devList.add(new Developer(R.drawable.icon1, "Sara Hamidi", "CSULB", "SaraHamidi@example.com", "\"The best way to predict the future is to invent it.\""));
        devList.add(new Developer(R.drawable.icon2, "Melissa Gaines", "CSULB", "MelissaGaines@example.com", "\"The only way to do great work is to love what you do.\""));
        devList.add(new Developer(R.drawable.icon3, "Keyi Wang", "CSULB", "KeyiWang@example.com", "\"Don't watch the clock; do what it does. Keep going.\""));
        devList.add(new Developer(R.drawable.icon4, "Kaylynn Khem", "CSULB", "KaylynnKhem@example.com", "\"Success is not final, failure is not fatal: it is the courage to continue that counts.\""));

        // Create a new devadapter with the list of developers
        DevAdapter devAdapter = new DevAdapter(devList);

        // Get a reference to the recycler view and set the adapter
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(devAdapter);

        // Set the layout manager for the recyclerview
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }
}
