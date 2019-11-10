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
        // -> TEST the method getCurrentAlarmVolume(Context).
        findViewById(R.id.button0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sets up the volume at default percentage.
                AudioUtilityManager.setAlarmVolume(MainActivity.this, DEFAULT_APP_VOLUME);
                int defaultVolume = AudioUtilityManager.getCurrentAlarmVolume(MainActivity.this);
                Toast.makeText(MainActivity.this, "DEFAULT ALARM VOLUME: "+defaultVolume+" %.", Toast.LENGTH_SHORT).show();
            }
        });

        // Action performed by clicking button1
        // -> TEST the method setMaxAlarmVolume(Context).
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioUtilityManager.setMaxAlarmVolume(MainActivity.this);
                int maxVolume = AudioUtilityManager.getCurrentAlarmVolume(MainActivity.this);
                Toast.makeText(MainActivity.this, "MAXIMUM ALARM VOLUME: "+maxVolume+" %.", Toast.LENGTH_SHORT).show();
            }
        });

        // Action performed by clicking button2
        // -> TEST the method setMinAlarmVolume(Context).
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioUtilityManager.setMinAlarmVolume(MainActivity.this);
                int minVolume = AudioUtilityManager.getCurrentAlarmVolume(MainActivity.this);
                Toast.makeText(MainActivity.this, "MINIMUM ALARM VOLUME: "+minVolume+" %.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
//TODO TESTING
