package com.gruppo4.SMSApp;

import android.content.Context;
import android.media.Ringtone;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gruppo4.ringtonePlayer.RingtoneHandler;

/**
 * @author Alessandra Tonin
 */

public class MainActivity extends AppCompatActivity {


    Ringtone ringtone = null;
    Ringtone alarmtone = null;

    /**
     * when app opens, get the current alarm tone and ringtone
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context context = getApplicationContext();
        ringtone = RingtoneHandler.getDefaultRingtone(context);
        alarmtone = RingtoneHandler.getDefaultAlarmTone(context);

    }

    /**
     * when button "ringtone" clicked, plays the ringtone if the alarm isn't ringing
     *
     * @param view, the layout button
     */
    public void onRingClick(View view) {

        if (!(alarmtone.isPlaying()))
            RingtoneHandler.ringtonePlay(ringtone);
        else
            Toast.makeText(this, "The alarm tone is already playing, please stop it and retry!", Toast.LENGTH_SHORT).show();

    }

    /**
     * when button "alarm" clicked, plays the alarm tone if the ringtone isn't ringing
     *
     * @param view, the layout button
     */
    public void onAlarmClick(View view) {
        if (!(ringtone.isPlaying()))
            RingtoneHandler.ringtonePlay(alarmtone);
        else
            Toast.makeText(this, "The ringtone is already playing, please stop it and retry!", Toast.LENGTH_SHORT).show();
    }

    /**
     * when button "stop ringing!" clicked, stops the sound that's playing
     *
     * @param view, the layout button
     */
    public void onStopClick(View view) {
        if (ringtone.isPlaying())
            RingtoneHandler.ringtoneStop(ringtone);

        if (alarmtone.isPlaying())
            RingtoneHandler.ringtoneStop(alarmtone);
    }


}
