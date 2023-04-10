package com.example.findapotty.tunes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

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
/*
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //navigate to another acitivty
                MyMediaPlayer.getInstance().reset();
                MyMediaPlayer.currentIndex = position;
                Intent intent = new Intent(context,MusicPlayerActivity.class);
                intent.putExtra("LIST",songsList);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

 */

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
                    MyMediaPlayer.currentIndex = getAdapterPosition();
                    // pass list of objects to next actvity and move to actviity
                    Intent intent = new Intent(context, MusicPlayerActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("LIST", songsList);
                    context.startActivity(intent);
                }
            });
        }
    }
}
/*
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView titleTV;
        ImageView iconIV;
        public ViewHolder(View itemView) {
            super(itemView);
            titleTV= itemView.findViewById(R.id.song_title);
            iconIV = itemView.findViewById(R.id.icon_pic);
        }

 */