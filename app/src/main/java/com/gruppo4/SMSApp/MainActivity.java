package com.gruppo4.SMSApp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import audioUtility.AudioUtilityManager;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Sets up the volume at 50%
        AudioUtilityManager.setupVolumeManager(this);

        // TESTING the method getCurrentVolume(Context)
        AudioUtilityManager.getCurrentVolume(this);
    }


}
