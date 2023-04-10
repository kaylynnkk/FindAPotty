package com.example.findapotty.music;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView noMusicTextView;
    ArrayList<Song> songsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tunes_song_recycler);

        recyclerView = findViewById(R.id.recycler_view);
        noMusicTextView = findViewById(R.id.no_songs_text);

        songsList.add(new Song("https://firebasestorage.googleapis.com/v0/b/findapotty." +
                "appspot.com/o/songs%2FMorgan%20Freeman%20Reads%20Everyone%20Poops.mp3?alt=media&" +
                "token=c34dd9e5-91e3-485e-81b9-837bf78d774b",
                "Morgan Freeman Reads Everyone Poops",
                "89443"));

        if(songsList.size()==0){
            noMusicTextView.setVisibility(View.VISIBLE);
        }else{
            //recyclerview
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new SongListRecyclerAdapter(songsList,getApplicationContext()));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(recyclerView!=null){
            recyclerView.setAdapter(new SongListRecyclerAdapter(songsList,getApplicationContext()));
        }
    }
}