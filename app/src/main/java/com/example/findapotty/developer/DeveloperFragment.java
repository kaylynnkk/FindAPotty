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
        // inflate the feedback layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_devpage, container, false);

        // Create list of developers
        List<Developer> devList = new ArrayList<>();
        devList.add(new Developer(R.drawable.flowers, "John Doe", "ABC University", "john.doe@example.com", "Code is poetry."));
        devList.add(new Developer(R.drawable.flowers, "Jane Smith", "XYZ College", "jane.smith@example.com", "I love to code."));

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
