package com.gruppo4.SMSApp;

/**
 * The MainActivity's scope is to test AudioUtilityManager's methods.
 * @author Francesco Bau'
 */

import com.gruppo4.audioUtility.AudioUtilityManager;

import android.media.AudioManager;
import android.os.Bundle;
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
        // -> Sets DEFAULT Volume for the current Stream
        // -> TEST the method getVolume(Context,String).
        // -> TEST the method setVolume(Context,String,int).
        findViewById(R.id.button_defaultVolume).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sets up the volume at default percentage.
                AudioUtilityManager.setVolume(MainActivity.this, stream, DEFAULT_APP_VOLUME);
                int defaultVolume = AudioUtilityManager.getVolume(MainActivity.this, stream);
                Toast.makeText(MainActivity.this, "DEFAULT " + stream + " VOLUME: " + defaultVolume + " %.", Toast.LENGTH_SHORT).show();
            }
        });

        // Action performed by clicking button1
        // -> Sets MAXIMUM Volume for the current Stream
        // -> TEST the method setVolumeToMax(Context,String).
        findViewById(R.id.button_maxVolume).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioUtilityManager.setVolumeToMax(MainActivity.this, stream);
                int maxVolume = AudioUtilityManager.getVolume(MainActivity.this, stream);
                Toast.makeText(MainActivity.this, "MAXIMUM " + stream + " VOLUME: " + maxVolume + " %.", Toast.LENGTH_SHORT).show();
            }
        });

        // Action performed by clicking button2
        // -> Sets MINIMUM Volume for the current Stream.
        // -> TEST the method setVolumeToMin(Context,String).
        findViewById(R.id.button_minVolume).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioUtilityManager.setVolumeToMin(MainActivity.this, stream);
                int minVolume = AudioUtilityManager.getVolume(MainActivity.this, stream);
                Toast.makeText(MainActivity.this, "MINIMUM " + stream + " VOLUME: " + minVolume + " %.", Toast.LENGTH_SHORT).show();
            }
        });

        // Action performed by clicking button3
        // -> SINGLE Vibration
        // -> TEST the "Vibrator". (One-shot)
        findViewById(R.id.button_singleVibrate).setOnClickListener(new View.OnClickListener() {
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
        // -> MULTIPLE Vibration
        // -> TEST the "Vibrator" (Multiple)
        findViewById(R.id.button_multipleVibrate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioUtilityManager.multipleVibrate(MainActivity.this);
                Toast.makeText(MainActivity.this, "MULTIPLE VIBRATION IS IN PROGRESS...", Toast.LENGTH_SHORT).show();
            }
        });

        // Action performad by clicking button5
        // -> STOP Vibration.
        findViewById(R.id.button_stopVibrate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioUtilityManager.stopVibrate(MainActivity.this);
                Toast.makeText(MainActivity.this, "STOPPING VIBRATION...", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
