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
    ArrayList<Song> songsList;
    Song currentSong;
    MediaPlayer mediaPlayer = MusicPlayer.getInstance();
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
        // title mmoves
        titleTv.setSelected(true);
        // get object info from previous page
        Bundle bundle = getArguments();
        songsList = (ArrayList<Song>) bundle.getSerializable("song_data");
        // Music player info
        setViewsInMusicPlayer();
        //player song
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer!=null){
                    //seekbar is moving with song
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    // time is changing with song
                    currentTimeTv.setText(convertToMMSS(mediaPlayer.getCurrentPosition()+""));
                    totalTimeTv.setText(convertToMMSS(String.valueOf(
                            Integer.parseInt(currentSong.getDuration())
                            - Integer.parseInt(String.valueOf(mediaPlayer.getCurrentPosition()))
                            )));
                    // icons changed basd on if song is playing or not
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
        // seekbar that follows the prgress of song
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
    //set all music siaply info
    void setViewsInMusicPlayer(){
        currentSong = songsList.get(MusicPlayer.currentIndex);
        titleTv.setText(currentSong.getTitle());
        totalTimeTv.setText(convertToMMSS(currentSong.getDuration()));
        pausePlay.setOnClickListener(v-> pausePlay());
        nextBtn.setOnClickListener(v-> playNextSong());
        previousBtn.setOnClickListener(v-> playPreviousSong());

        playMusic();
    }

    //make the song play
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
    // if forward button is clicke go to next song
    private void playNextSong(){

        if(MusicPlayer.currentIndex== songsList.size()-1)
            return;
        MusicPlayer.currentIndex +=1;
        mediaPlayer.reset();
        setViewsInMusicPlayer();

    }
    // if prev button is clicked go to previous song
    private void playPreviousSong(){
        if(MusicPlayer.currentIndex== 0)
            return;
        MusicPlayer.currentIndex -=1;
        mediaPlayer.reset();
        setViewsInMusicPlayer();
    }
    // if pause button is click sotp music player

    private void pausePlay(){
        if(mediaPlayer.isPlaying())
            mediaPlayer.pause();
        else
            mediaPlayer.start();
    }
    // format song length info so its readble to users
    public static String convertToMMSS(String duration){
        Long millis = Long.parseLong(duration);
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }
}
