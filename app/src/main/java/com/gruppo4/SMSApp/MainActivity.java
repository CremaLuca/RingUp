package com.gruppo4.SMSApp;

import android.content.Context;
import android.media.Ringtone;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.gruppo4.ringtonePlayer.RingtoneHandler;
/*
* @author Alessandra Tonin
 */
public class MainActivity extends AppCompatActivity {


    Ringtone ringtone = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context context = getApplicationContext();
        ringtone = RingtoneHandler.getDefaultRingtone(context);
    }

    /*
    * when button "ring!" clicked, plays the ringtone
    * @param the layout button
     */
    public void onRingClick(View view){
        RingtoneHandler.ringtonePlay(ringtone);
    }

    /*
     * when button "stop ringing!" clicked, stops the ringtone
     * @param the layout button
     */
    public void onStopClick(View view){
        RingtoneHandler.ringtoneStop(ringtone);
    }



}
