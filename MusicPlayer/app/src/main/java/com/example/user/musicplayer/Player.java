package com.example.user.musicplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.util.ArrayList;

public class Player extends AppCompatActivity implements View.OnClickListener{

    static MediaPlayer mp;
    ArrayList mySongs;
    int position;
    Uri u;

    Button btPlay,btFF,btFB,btNext,btPrev;
    SeekBar sb;
    Thread updateSeekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        btPlay=(Button)findViewById(R.id.btnPlay);
        btFF=(Button)findViewById(R.id.btnFastForward);
        btFB=(Button)findViewById(R.id.btnFastBackward);
        btNext=(Button)findViewById(R.id.btnNext);
        btPrev=(Button)findViewById(R.id.btnPrev);

        btPlay.setOnClickListener(this);
        btFF.setOnClickListener(this);
        btFB.setOnClickListener(this);
        btNext.setOnClickListener(this);
        btPrev.setOnClickListener(this);

        sb = (SeekBar)findViewById(R.id.seekBar);
        updateSeekbar=new Thread(){
            @Override
            public void run() {

                int totalDuration = mp.getDuration();
                int currentPosition=0;

                while (currentPosition<totalDuration){

                    try {
                        sleep(500);
                        currentPosition=mp.getCurrentPosition(); //update currentPosition after half second later
                        sb.setProgress(currentPosition);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //
            }
        };

        if(mp !=null){
            mp.stop();
            mp.release();
        }

        Intent i = getIntent();
        Bundle b = i.getExtras(); //all songs as bundle
        mySongs =(ArrayList) b.getParcelableArrayList("songList");
        position = b.getInt("pos",0); //

        u = Uri.parse(mySongs.get(position).toString()); //mysongs.get(position) is a file of songs
        mp=MediaPlayer.create(getApplicationContext(),u);
        mp.start();

        sb.setMax(mp.getDuration());
        updateSeekbar.start(); //seekbar will start after start the song

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                mp.seekTo(seekBar.getProgress());
            }
        });


    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch(id){

            case R.id.btnPlay:
                if(mp.isPlaying()){

                    mp.pause();
                }
                else {
                    mp.start();
                }
                break;

            case R.id.btnFastForward:

                mp.seekTo(mp.getCurrentPosition()+5000);
                break;

            case R.id.btnFastBackward:

                mp.seekTo(mp.getCurrentPosition()-5000);

            case  R.id.btnNext:
                mp.stop();
                mp.release();
                position=(position+1)%mySongs.size();
                u = Uri.parse(mySongs.get(position).toString()); //mysongs.get(position) is a file of songs
                mp=MediaPlayer.create(getApplicationContext(),u);
                mp.start();
                sb.setMax(mp.getDuration());
                break;

            case R.id.btnPrev:
                mp.stop();
                mp.release();
                position= (position-1<0) ?mySongs.size()-1:position-1;

              /*  if(position-1<0){
                    position=mySongs.size()-1;
                }
                else {
                    position=position-1;
                }*/
                u = Uri.parse(mySongs.get(position).toString()); //mysongs.get(position) is a file of songs
                mp=MediaPlayer.create(getApplicationContext(),u);
                mp.start();
                sb.setMax(mp.getDuration());
                break;

        }

    }
}
