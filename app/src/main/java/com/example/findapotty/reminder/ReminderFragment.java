package com.example.findapotty.reminder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findapotty.R;
import com.example.findapotty.databinding.FragmentReminderBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReminderFragment extends Fragment {

    private FragmentReminderBinding binding;
    FloatingActionButton mCreateRem;
    private RecyclerView mRecyclerview;
    ReminderRecyclerViewAdaptor reminderRecyclerViewAdaptor;
    DatabaseReference dbr;
    ArrayList<ReminderMessage> remindersList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentReminderBinding.inflate(inflater, container, false);
       mRecyclerview = binding.recyclerView;
        mCreateRem = binding.createReminder;
        mCreateRem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.reminder1, new ReminderBuilderFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        populateReminders();

        return binding.getRoot();


    }
    private void populateReminders() {
        dbr = FirebaseDatabase.getInstance("https://findapotty-main.firebaseio.com/")
                .getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("reminders");

        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                remindersList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    remindersList.add(postSnapshot.getValue(ReminderMessage.class));

                }

                if (dbr != null) {
                    // use firebas eui to populate recycler straigther form databse
                    mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
                    reminderRecyclerViewAdaptor = new ReminderRecyclerViewAdaptor(getContext(), remindersList);
                    mRecyclerview.setAdapter(reminderRecyclerViewAdaptor);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}