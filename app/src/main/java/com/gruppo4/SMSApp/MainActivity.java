package com.gruppo4.SMSApp;

import android.os.Bundle;
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

        // TESTING the method getCurrentVolume(Context)
        int currentVolume = AudioUtilityManager.getCurrentVolume(this);
        Toast.makeText(this, "CURRENT RINGTONE VOLUME: "+currentVolume+" %.", Toast.LENGTH_SHORT).show();

        // TESTING the method getMaxVolume(Context)
        int maxVolume = AudioUtilityManager.getMaxVolume(this);
        Toast.makeText(this, "MAXIMUM RINGTONE VOLUME: "+maxVolume, Toast.LENGTH_SHORT).show();
    }


}
