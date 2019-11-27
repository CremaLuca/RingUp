package com.gruppo4.RingApplication.structure;


import android.content.Context;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.annotation.NonNull;

/**
 * @author Alessandra Tonin, Luca Crema
 */

public class RingtoneHandler {

    /**
     * defaultRingtone instantiated in getDefaultTone
     */
    private static Ringtone defaultRingtone = null;

    /**
     * Instance of the class that is instantiated in getInstance method
     */
    private static RingtoneHandler instance = null;

    /**
     * Private constructor
     */
    private RingtoneHandler() {
    }

    /**
     * @return the RingtoneHandler instance
     */
    public static RingtoneHandler getInstance() {
        if (instance == null)
            instance = new RingtoneHandler();
        return instance;
    }

    /**
     * @param type taken from RingtoneManager API
     * @return the Uri of the specified type tone
     */
    private Uri getDefaultToneUri(int type) {
        return RingtoneManager.getDefaultUri(type);
    }

    /**
     * Get the tone of the specified type
     *
     * @param ctx the current application context
     * @return the requested tone
     */
    public Ringtone getDefaultTone(Context ctx, int type) {
        return RingtoneManager.getRingtone(ctx, getDefaultToneUri(type));
    }

    /**
     * Get the default ringtone
     *
     * @param ctx the current application context
     * @return the actual ringtone
     */
    public Ringtone getDefaultRingtone(Context ctx) {
        if (defaultRingtone == null)
            defaultRingtone = getDefaultTone(ctx, RingtoneManager.TYPE_RINGTONE);
        return defaultRingtone;
    }

    /**
     * Get the default alarm tone
     *
     * @param ctx the current application context
     * @return the actual alarm tone
     */
    public Ringtone getDefaultAlarmTone(Context ctx) {
        return getDefaultTone(ctx, RingtoneManager.TYPE_ALARM);
    }

    /**
     * Plays a ringtone (overload di ringtonePlay(Ringtone ringtone, final int USAGE_CODE))
     *
     * @param ringtone the default ringtone
     */
    public void playRingtone(Ringtone ringtone) {
        playRingtone(ringtone, AudioAttributes.USAGE_ALARM);
    }

    /**
     * Stops the playing sound
     *
     * @param ringtone the playing sound (ringtone or alarm tone)
     */
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
