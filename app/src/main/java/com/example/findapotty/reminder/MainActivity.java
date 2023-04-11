package com.example.findapotty.reminder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findapotty.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
public class MainActivity extends AppCompatActivity {


    FloatingActionButton mCreateRem;
    private RecyclerView mRecyclerview;
    Adapter adapter;
    DatabaseReference dbr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder_activity_main);


        mRecyclerview = (RecyclerView) findViewById(R.id.recycler_view);
        mCreateRem = (FloatingActionButton) findViewById(R.id.create_reminder);
        /// got to neetx activtey
        mCreateRem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReminderActivity.class);
                startActivity(intent);
            }
        });

        dbr = FirebaseDatabase.getInstance("https://findapotty-main.firebaseio.com/")
                .getReference().child("reminders");
        FirebaseRecyclerOptions<ReminderMessage> fbo
                = new FirebaseRecyclerOptions.Builder<ReminderMessage>()
                .setQuery(dbr, ReminderMessage.class)
                .build();
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(fbo);
        mRecyclerview.setAdapter(adapter);//Binds the adapter with recyclerview

    }
    @Override protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    //
    @Override protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}