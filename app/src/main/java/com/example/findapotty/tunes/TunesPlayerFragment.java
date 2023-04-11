package com.example.findapotty.tunes;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.findapotty.R;
import com.example.findapotty.databinding.FragmentTunesplayerBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class TunesPlayerFragment extends Fragment {
    TextView titleTv,currentTimeTv,totalTimeTv;
    SeekBar seekBar;
    ImageView pausePlay,nextBtn,previousBtn,musicIcon;
    Song songOption = new Song("https://firebasestorage.googleapis.com/v0/b/findapotty." +
            "appspot.com/o/songs%2FMorgan%20Freeman%20Reads%20Everyone%20Poops.mp3?alt=media&" +
            "token=c34dd9e5-91e3-485e-81b9-837bf78d774b",
            "Morgan Freeman Reads Everyone Poops",
            "89443");
    ArrayList<Song> songsList = new ArrayList<Song>();
    Song currentSong;
    MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();
    int x=0;
    private FragmentTunesplayerBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTunesplayerBinding.inflate(inflater, container, false);
        titleTv = binding.songTitle;
        currentTimeTv = binding.currentTime;
        totalTimeTv = binding.totalTime;
        seekBar = binding.seekBar;
        pausePlay = binding.playOPause;
        nextBtn = binding.next;
        previousBtn = binding.prev;
        musicIcon = binding.musicIcon;

        titleTv.setSelected(true);

        //songsList = (ArrayList<Song>) getActivity().getIntent().getSerializableExtra("LIST");
        songsList.add(songOption);
        setResourcesWithMusic();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer!=null){
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    currentTimeTv.setText(convertToMMSS(mediaPlayer.getCurrentPosition()+""));

                    if(mediaPlayer.isPlaying()){
                        pausePlay.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
                        musicIcon.setRotation(x++);
                    }else{
                        pausePlay.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
                        musicIcon.setRotation(0);
                    }

                }
                new Handler().postDelayed(this,100);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer!=null && fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        return binding.getRoot();
    }

    void setResourcesWithMusic(){
        currentSong = songsList.get(MyMediaPlayer.currentIndex);

        titleTv.setText(currentSong.getTitle());

        totalTimeTv.setText(convertToMMSS(currentSong.getDuration()));

        pausePlay.setOnClickListener(v-> pausePlay());
        nextBtn.setOnClickListener(v-> playNextSong());
        previousBtn.setOnClickListener(v-> playPreviousSong());

        playMusic();


    }


    private void playMusic(){

        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(currentSong.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void playNextSong(){

        if(MyMediaPlayer.currentIndex== songsList.size()-1)
            return;
        MyMediaPlayer.currentIndex +=1;
        mediaPlayer.reset();
        setResourcesWithMusic();

    }

    private void playPreviousSong(){
        if(MyMediaPlayer.currentIndex== 0)
            return;
        MyMediaPlayer.currentIndex -=1;
        mediaPlayer.reset();
        setResourcesWithMusic();
    }

    private void pausePlay(){
        if(mediaPlayer.isPlaying())
            mediaPlayer.pause();
        else
            mediaPlayer.start();
    }


    public static String convertToMMSS(String duration){
        Long millis = Long.parseLong(duration);
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }
}
