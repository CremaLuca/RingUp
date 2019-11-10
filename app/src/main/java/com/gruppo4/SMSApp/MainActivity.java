package com.gruppo4.SMSApp;

/**
 *@author Francesco Bau'
 */

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gruppo4.audioUtility.AudioUtilityManager;


public class MainActivity extends AppCompatActivity {

    private static final int TEST_ZERO = 0;
    private static final int TEST_ONE = 1;
    private static final int TEST_TWO = 2;

    // Default app volume, expressed in percentage
    private static final int DEFAULT_APP_VOLUME = 50;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Action performed by clicking button0
        // -> TEST the method getCurrentRingtoneVolume(Context).
        findViewById(R.id.button0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sets up the volume at default percentage.
                AudioUtilityManager.setRingtoneVolume(MainActivity.this, DEFAULT_APP_VOLUME);
                int defaultVolume = AudioUtilityManager.getCurrentRingtoneVolume(MainActivity.this);
                Toast.makeText(MainActivity.this, "DEFAULT RINGTONE VOLUME: "+defaultVolume+" %.", Toast.LENGTH_SHORT).show();
            }
        });

        // Action performed by clicking button1
        // -> TEST the method setMaxRingtoneVolume(Context).
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioUtilityManager.setMaxRingtoneVolume(MainActivity.this);
                int maxVolume = AudioUtilityManager.getCurrentRingtoneVolume(MainActivity.this);
                Toast.makeText(MainActivity.this, "MAXIMUM RINGTONE VOLUME: "+maxVolume+" %.", Toast.LENGTH_SHORT).show();
            }
        });

        // Action performed by clicking button2
        // -> TEST the method setMinRingtoneVolume(Context).
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioUtilityManager.setMinRingtoneVolume(MainActivity.this);
                int minVolume = AudioUtilityManager.getCurrentRingtoneVolume(MainActivity.this);
                Toast.makeText(MainActivity.this, "MINIMUM RINGTONE VOLUME: "+minVolume+" %.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
//TODO TESTING
