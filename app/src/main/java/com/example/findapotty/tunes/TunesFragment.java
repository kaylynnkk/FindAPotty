package com.example.findapotty.tunes;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.findapotty.databinding.FragmentTunesBinding;

import java.util.ArrayList;

public class TunesFragment extends Fragment {
    RecyclerView recyclerView;
    TextView noMusicTextView;
    ArrayList<Song> songsList = new ArrayList<>();
    private FragmentTunesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTunesBinding.inflate(inflater, container, false);
        recyclerView = binding.recyclerView;
        noMusicTextView = binding.noSongsText;
        songsList.add(new Song("https://firebasestorage.googleapis.com/v0/b/findapotty." +
                "appspot.com/o/songs%2FMorgan%20Freeman%20Reads%20Everyone%20Poops.mp3?alt=media&" +
                "token=c34dd9e5-91e3-485e-81b9-837bf78d774b",
                "Morgan Freeman Reads Everyone Poops",
                "89443"));

        if(songsList.size()==0){
            noMusicTextView.setVisibility(View.VISIBLE);
        }else{
            //recyclerview
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(new SongListRecyclerAdapter(songsList,getContext()));
        }
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(recyclerView!=null){
            recyclerView.setAdapter(new SongListRecyclerAdapter(songsList,getContext()));
        }
    }
}