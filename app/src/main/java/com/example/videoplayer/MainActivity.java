package com.example.videoplayer;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener{

    private VideoView videoView;
    private Button btnPlay;
    private Button playMusic,pauseMusic;
    private SeekBar seekBar;
    private SeekBar seekBarForowadMusic;
    private MediaPlayer mPlayer;
    private AudioManager audioManager;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView=(VideoView)findViewById(R.id.videoView);
        btnPlay=(Button)findViewById(R.id.btnPlay);

        playMusic=(Button)findViewById(R.id.playMusic);
        pauseMusic=(Button)findViewById(R.id.pauseMusic);
        seekBar=(SeekBar)findViewById(R.id.seekBarVolume);
        seekBarForowadMusic=(SeekBar)findViewById(R.id.seekBarForowad);

        mPlayer=MediaPlayer.create(getApplicationContext(),R.raw.music);


        btnPlay.setOnClickListener(this);
        playMusic.setOnClickListener(this);
        pauseMusic.setOnClickListener(this);

        initControls();
       seekBarForowadMusic.setOnSeekBarChangeListener(this);
       mPlayer.setOnCompletionListener(this);


    }

    private void initControls()
    {

            audioManager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
            seekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            seekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser){
                        Toast.makeText(MainActivity.this, Integer.toString(progress), Toast.LENGTH_SHORT).show();
                    }
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

    }

    @Override
    public void onClick(View btnView) {

        switch(btnView.getId()){

            case R.id.btnPlay:
                String path="android.resource://"+getPackageName() +"/" + R.raw.city;
                MediaController controller=new MediaController(this);
                controller.setAnchorView(videoView);
                controller.setMediaPlayer(videoView);
                Uri uri =Uri.parse(path);
                videoView.setMediaController(controller);
                videoView.setVideoURI(uri);

                videoView.start();
                break;

            case R.id.playMusic:

                mPlayer.start();
                seekBarForowadMusic.setMax(mPlayer.getDuration());
                forwardMusic();


                break;

            case R.id.pauseMusic:
              mPlayer.pause();
              timer.cancel();


              break;

        }



    }
    private void forwardMusic(){
        timer=new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run()
            {
                seekBarForowadMusic.setProgress(mPlayer.getCurrentPosition());

            }
        },0,1000);

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
           mPlayer.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mPlayer.pause();

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mPlayer.start();

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        timer.cancel();
        Toast.makeText(this, "Music is Ended", Toast.LENGTH_SHORT).show();

    }
}
