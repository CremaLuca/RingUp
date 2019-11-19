package com.gruppo4.SMSApp;

/**
 * @author Francesco Bau'
 */

import com.gruppo4.audioUtility.AudioUtilityManager;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {


    // Default app volume, expressed in percentage
    private static final int DEFAULT_APP_VOLUME = 50;
    // The selected Stream.
    private static AudioUtilityManager.AUMStream stream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Default choice: Stream ALARM
        stream = AudioUtilityManager.AUMStream.ALARM;

        // Action performed by clicking button0
        // -> TEST the method getVolume(Context,String).
        // -> TEST the method setVolume(Context,String,int).
        findViewById(R.id.button0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sets up the volume at default percentage.
                AudioUtilityManager.setVolume(MainActivity.this, stream, DEFAULT_APP_VOLUME);
                int defaultVolume = AudioUtilityManager.getVolume(MainActivity.this, stream);
                Toast.makeText(MainActivity.this, "DEFAULT " + stream + " VOLUME: " + defaultVolume + " %.", Toast.LENGTH_SHORT).show();
            }
        });

        // Action performed by clicking button1
        // -> TEST the method setMaxVolume(Context,String).
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioUtilityManager.setMaxVolume(MainActivity.this, stream);
                int maxVolume = AudioUtilityManager.getVolume(MainActivity.this, stream);
                Toast.makeText(MainActivity.this, "MAXIMUM " + stream + " VOLUME: " + maxVolume + " %.", Toast.LENGTH_SHORT).show();
            }
        });

        // Action performed by clicking button2
        // -> TEST the method setMinVolume(Context,String).
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioUtilityManager.setMinVolume(MainActivity.this, stream);
                int minVolume = AudioUtilityManager.getVolume(MainActivity.this, stream);
                Toast.makeText(MainActivity.this, "MINIMUM " + stream + " VOLUME: " + minVolume + " %.", Toast.LENGTH_SHORT).show();
            }
        });

        // Action performed by clicking button3
        // -> TEST the "Vibrator". (One-shot)
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioUtilityManager.singleVibrate(MainActivity.this);
                Toast.makeText(MainActivity.this, "SINGLE VIBRATION IS IN PROGRESS...", Toast.LENGTH_SHORT).show();
            }
        });

        // Action performed by clicking alarmButton
        // -> SELECT ALARM Stream
        findViewById(R.id.alarmButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stream = AudioUtilityManager.AUMStream.ALARM;
            }
        });

        // Action performed by clicking musicButton
        // -> SELECT MUSIC Stream
        findViewById(R.id.musicButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stream = AudioUtilityManager.AUMStream.MUSIC;
            }
        });

        // Action performad by clicking ringButton
        // -> SELECT RING Stream
        findViewById(R.id.ringButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stream = AudioUtilityManager.AUMStream.RING;
            }
        });

        // Action performed by clicking vibrateButton
        // -> Turn on VIBRATE Mode
        findViewById(R.id.vibrateButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
                audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                Toast.makeText(MainActivity.this, "ENABLED ONLY-VIBRATE MODE", Toast.LENGTH_SHORT).show();
            }
        });

        // Action performed by clicking button4
        // -> TEST the "Vibrator" (Multiple)
        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioUtilityManager.multipleVibrate(MainActivity.this);
                Toast.makeText(MainActivity.this, "MULTIPLE VIBRATION IS IN PROGRESS...", Toast.LENGTH_SHORT).show();
            }
        });

        // Action performad by clicking button5
        // -> STOP VIBRATOR.
        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.cancel();
                Toast.makeText(MainActivity.this, "STOPPING VIBRATION...", Toast.LENGTH_SHORT).show();
            }
        });

    }

}

//TODO USAGE OF 1 RINGTONE (?)
//TODO Specify action performed by each button
//TODO Identification of the buttons
