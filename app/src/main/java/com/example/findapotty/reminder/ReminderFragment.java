package com.example.findapotty.reminder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.findapotty.MainActivity;
import com.example.findapotty.R;
import com.example.findapotty.databinding.FragmentDiaryBinding;
import com.example.findapotty.databinding.FragmentReminderBinding;
import com.example.findapotty.history.VisitedRestroomsRecyclerViewAdaptor;
import com.example.findapotty.tunes.SongListRecyclerAdapter;
import com.example.findapotty.tunes.TunesPlayerFragment;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReminderFragment extends Fragment {

    private FragmentReminderBinding binding;
    FloatingActionButton mCreateRem;
    private RecyclerView mRecyclerview;
    ReminderListAdapter reminderListAdapter;
    DatabaseReference dbr;

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
        /*dbr = FirebaseDatabase.getInstance("https://findapotty-main.firebaseio.com/")
                .getReference().child("reminders");

        Log.i("ProblemFOUND", "dbr is null");
        FirebaseRecyclerOptions<ReminderMessage> fbo
                = new FirebaseRecyclerOptions.Builder<ReminderMessage>()
                .setQuery(dbr, ReminderMessage.class)
                .build();
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        reminderListAdapter = new ReminderListAdapter(fbo);
        mRecyclerview.setAdapter(reminderListAdapter);//Binds the adapter with recyclerview


 */
        return binding.getRoot();


    }
    /*
    @Override
    public void onStart() {
        super.onStart();
        reminderListAdapter.startListening();
    }

    //
    @Override
    public void onStop() {
        super.onStop();
        reminderListAdapter.stopListening();
    }

     */
}