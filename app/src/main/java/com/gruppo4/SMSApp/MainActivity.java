package com.gruppo4.SMSApp;

import android.content.Context;
import android.media.Ringtone;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.gruppo4.ringtonePlayer.RingtoneHandler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /*
    * when button clicked, play the ringtone
    * @param the layout button
     */
    public void onRingClick(View view){

        Context context = getApplicationContext();
        Ringtone ringtone = RingtoneHandler.getDefaultRingtone(context);
        RingtoneHandler.ringtonePlay(ringtone);
    }

    /*
    public void onStopClick(View view){

        Context context = getApplicationContext();
        Ringtone ringtone = RingtoneHandler.getDefaultRingtone(context);
        RingtoneHandler.ringtoneStop(ringtone);
    }

     */

}
