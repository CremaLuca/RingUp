package com.gruppo4.SMSApp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gruppo4.audioUtility.AudioUtilityManager;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Sets up the volume at 50%.
        // HARDWIRING
        AudioUtilityManager.setRingtoneVolume(this,50);
        Toast.makeText(this, "RINGTONE AUDIO IS SET TO 50 %.", Toast.LENGTH_SHORT).show();

        // TESTING the method getCurrentVolume(Context)
        int currentVolume = AudioUtilityManager.getCurrentVolume(this);
        Toast.makeText(this, "CURRENT RINGTONE VOLUME: "+currentVolume+" %.", Toast.LENGTH_SHORT).show();
    }


}
