package com.gruppo4.SMSApp;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
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
        ringtone = RingtoneHandler.getDefaultTone(context, RingtoneManager.TYPE_RINGTONE);
        alarmtone = RingtoneHandler.getDefaultTone(context, RingtoneManager.TYPE_ALARM);

    }

    /**
     * when button "ringtone" clicked, plays the ringtone if the alarm isn't ringing
     *
     * @param view, the layout button
     */
    public void onRingClick(View view) {

        if (!(alarmtone.isPlaying()))
            RingtoneHandler.playRingtone(ringtone);
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
            RingtoneHandler.playRingtone(alarmtone);
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
            RingtoneHandler.stopRingtone(ringtone);

        if (alarmtone.isPlaying())
            RingtoneHandler.stopRingtone(alarmtone);
    }


}
