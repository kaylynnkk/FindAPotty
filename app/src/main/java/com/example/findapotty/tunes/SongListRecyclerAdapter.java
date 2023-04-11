package com.example.findapotty.tunes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findapotty.MainActivity;
import com.example.findapotty.R;

import java.util.ArrayList;

public class SongListRecyclerAdapter extends RecyclerView.Adapter<SongListRecyclerAdapter.ViewHolder>{

    ArrayList<Song> songsList;
    Context context;

    public SongListRecyclerAdapter(ArrayList<Song> songsList, Context context) {
        this.songsList = songsList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tunes_single_song_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.titleTextView.setText(songsList.get(position).getTitle());

        if(MyMediaPlayer.currentIndex==position){
            holder.titleTextView.setTextColor(Color.parseColor("#FF0000"));
        }else{
            holder.titleTextView.setTextColor(Color.parseColor("#000000"));
        }

    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView titleTextView;
        ImageView iconImageView;
        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.music_title_text);
            iconImageView = itemView.findViewById(R.id.icon_view);
            // when one of the items in recycler list ia clicked
            // go to another activity

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // stops plays mediaplayer and starts song form begin
                    MyMediaPlayer.getInstance().reset();
                    MyMediaPlayer.currentIndex = getBindingAdapterPosition();
                    // pass list of objects to next actvity and move to actviity
                    ((MainActivity)context).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.tunes1, new TunesPlayerFragment())
                            .addToBackStack(null)
                            .commit();
                }
            });


        }
    }
}
