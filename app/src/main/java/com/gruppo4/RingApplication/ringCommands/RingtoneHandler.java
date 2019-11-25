package com.gruppo4.RingApplication.ringCommands;


import android.content.Context;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.gruppo4.RingApplication.ringCommands.Interfaces.RingtoneHandlerInterface;

/**
 * @author Alessandra Tonin, Luca Crema
 */

public class RingtoneHandler implements RingtoneHandlerInterface {

    private static Ringtone defaultRingtone;

    private static RingtoneHandler ringtoneHandler = new RingtoneHandler();

    /**
     * Private constructor
     */
    private RingtoneHandler() {
    }

    /**
     * @return the singleton
     */
    public static RingtoneHandler getInstance() {
        return ringtoneHandler;
    }

    /**
     * @param type taken from RingtoneManager API
     * @return the Uri of the specified type tone
     */
    private Uri getDefaultToneUri(int type) {
        return RingtoneManager.getDefaultUri(type);
    }

    @Override
    public Ringtone getDefaultTone(Context ctx, int type) {
        return RingtoneManager.getRingtone(ctx, getDefaultToneUri(type));
    }

    @Override
    public Ringtone getDefaultRingtone(Context ctx) {
        if (defaultRingtone == null)
            defaultRingtone = getDefaultTone(ctx, RingtoneManager.TYPE_RINGTONE);
        return defaultRingtone;
    }

    @Override
    public Ringtone getDefaultAlarmTone(Context ctx) {
        return getDefaultTone(ctx, RingtoneManager.TYPE_ALARM);
    }

    @Override
    public void playRingtone(Ringtone ringtone) {
        playRingtone(ringtone, AudioAttributes.USAGE_ALARM);
    }

    @Override
    public void stopRingtone(Ringtone ringtone) {
        ringtone.stop();
    }

    /**
     * User can decide in which mode he wants to play the ringtone
     *
     * @param ringtone   the sound to be played
     * @param USAGE_CODE from AudioAttributes
     */
    private void playRingtone(@NonNull Ringtone ringtone, final int USAGE_CODE) {
        ringtone.setAudioAttributes(new AudioAttributes.Builder().setUsage(USAGE_CODE).build());
        ringtone.play();

    }
}
