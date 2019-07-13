package com.example.imagetotext;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;

public class DrumPadActivity extends AppCompatActivity {

    private SoundPool soundPool;
    private int sound1;
    private int sound2;
    private int sound3;
    private int sound4;
    private int sound5;
    private int sound6;
    private int sound7;
    private int sound8;
    private int sound9;
    private int sound00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.drum_pad );
        soundPool = new SoundPool( 2, AudioManager.STREAM_MUSIC, 0 );
        sound1 = soundPool.load(getApplicationContext(), R.raw.sound1,1);
        sound2 = soundPool.load(getApplicationContext(), R.raw.sound2,1);
        sound3 = soundPool.load(getApplicationContext(), R.raw.sound3,1);
        sound4 = soundPool.load(getApplicationContext(), R.raw.sound4,1);
        sound5 = soundPool.load(getApplicationContext(), R.raw.sound5,1);
        sound6 = soundPool.load(getApplicationContext(), R.raw.sound6,1);
        sound7 = soundPool.load(getApplicationContext(), R.raw.sound7,1);
        sound8 = soundPool.load(getApplicationContext(), R.raw.sound8,1);
        sound9 = soundPool.load(getApplicationContext(), R.raw.sound9,1);
        sound00 = soundPool.load(getApplicationContext(), R.raw.sound00,1);

    }

    public void playSound1 (View v) {
        soundPool.play( sound1,1.0f,1.0f,0,0,10f );
    }

    public void playSound2 (View v) {
        soundPool.play( sound2,1.0f,1.0f,0,0,10f );
    }

    public void playSound3 (View v) {
        soundPool.play( sound3,1.0f,1.0f,0,0,10f );
    }

    public void playSound4 (View v) {
        soundPool.play( sound4,1.0f,1.0f,0,0,10f );
    }

    public void playSound5 (View v) {
        soundPool.play( sound5,1.0f,1.0f,0,0,10f );
    }

    public void playSound6 (View v) {
        soundPool.play( sound6,1.0f,1.0f,0,0,10f );
    }

    public void playSound7 (View v) {
        soundPool.play( sound7,1.0f,1.0f,0,0,10f );
    }

    public void playSound8 (View v) {
        soundPool.play( sound8,1.0f,1.0f,0,0,10f );
    }

    public void playSound9 (View v) {
        soundPool.play( sound9,1.0f,1.0f,0,0,10f );
    }

}
