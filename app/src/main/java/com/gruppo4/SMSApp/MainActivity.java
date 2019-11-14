package com.gruppo4.SMSApp;

/**
 *@author Francesco Bau'
 */

import android.media.AudioManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gruppo4.audioUtility.AudioUtilityManager;


public class MainActivity extends AppCompatActivity {


    // Default app volume, expressed in percentage
    private static final int DEFAULT_APP_VOLUME = 50;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Action performed by clicking button0
        // -> TEST the method getVolume(Context,String).
        // -> TEST the method setVolume(Context,String,int).
        findViewById(R.id.button0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sets up the volume at default percentage.
                AudioUtilityManager.setVolume(MainActivity.this, AudioUtilityManager.ALARM, DEFAULT_APP_VOLUME);
                int defaultVolume = AudioUtilityManager.getVolume(MainActivity.this, AudioUtilityManager.ALARM);
                Toast.makeText(MainActivity.this, "DEFAULT ALARM VOLUME: "+defaultVolume+" %.", Toast.LENGTH_SHORT).show();
            }
        });

        // Action performed by clicking button1
        // -> TEST the method setMaxVolume(Context,String).
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioUtilityManager.setMaxVolume(MainActivity.this, AudioUtilityManager.ALARM);
                int maxVolume = AudioUtilityManager.getVolume(MainActivity.this, AudioUtilityManager.ALARM);
                Toast.makeText(MainActivity.this, "MAXIMUM ALARM VOLUME: "+maxVolume+" %.", Toast.LENGTH_SHORT).show();
            }
        });

        // Action performed by clicking button2
        // -> TEST the method setMinVolume(Context,String).
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioUtilityManager.setMinVolume(MainActivity.this,AudioUtilityManager.ALARM);
                int minVolume = AudioUtilityManager.getVolume(MainActivity.this,AudioUtilityManager.ALARM);
                Toast.makeText(MainActivity.this, "MINIMUM ALARM VOLUME: "+minVolume+" %.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}

//TODO NEW LAYOUT - BUTTONS TO TEST UTILITY METHODS