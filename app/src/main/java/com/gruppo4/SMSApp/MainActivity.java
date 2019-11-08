package com.gruppo4.SMSApp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gruppo4.audioUtility.AudioUtilityManager;


public class MainActivity extends AppCompatActivity {

    //Default ringtone volume (%) for the app
    private static final int DEFAULT_APP_VOLUME = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Sets up the volume at a certain percentage.
        AudioUtilityManager.setRingtoneVolume(this, DEFAULT_APP_VOLUME);

        // TESTING the method getCurrentRingtoneVolume(Context)
        int currentVolume = AudioUtilityManager.getCurrentRingtoneVolume(this);
        Toast.makeText(this, "CURRENT RINGTONE VOLUME: "+currentVolume+" %.", Toast.LENGTH_SHORT).show();

        // TESTING the method getMaxRingtoneVolume(Context)
        int maxVolume = AudioUtilityManager.getMaxRingtoneVolume(this);
        Toast.makeText(this, "MAXIMUM RINGTONE VOLUME: "+maxVolume, Toast.LENGTH_SHORT).show();

        // TESTING the method setMaxRingtoneVolume(Context), after waiting 5 seconds.
        Log.d("[TEST]","WAITING 5 SECONDS...");
        try {
            // HARDWIRING...
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("[TEST]","RESUMING...");
        AudioUtilityManager.setMaxRingtoneVolume(this);
        currentVolume = AudioUtilityManager.getCurrentRingtoneVolume(this);
        Toast.makeText(this, "NEW CURRENT RINGTONE VOLUME: "+currentVolume+" %.", Toast.LENGTH_SHORT).show();


    }


}
