package com.gruppo4.RingApplication.ringCommands;


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
     * @param type a type from RingtoneManager API
     * @return the Uri of the specified type tone
     */
    private static Uri getDefaultToneUri(int type) {
        return RingtoneManager.getDefaultUri(type);
    }

    /**
     * Get the tone of the specified type
     *
     * @param ctx the current application context
     * @return the requested tone
     */
    public static Ringtone getDefaultTone(Context ctx, int type) {
        return RingtoneManager.getRingtone(ctx, getDefaultToneUri(type));
    }

    /**
     * Get the default ringtone
     *
     * @param ctx the current application context
     * @return the actual ringtone
     */

    public static Ringtone getDefaultRingtone(Context ctx) {
        return getDefaultTone(ctx, RingtoneManager.TYPE_RINGTONE);
    }


    /**
     * Get the default alarm tone
     *
     * @param ctx the current application context
     * @return the actual alarm tone
     */
    public static Ringtone getDefaultAlarmTone(Context ctx) {
        return getDefaultTone(ctx, RingtoneManager.TYPE_ALARM);
    }


    /**
     * User can decide in which mode he wants to play the ringtone
     *
     * @param ringtone   the sound to be played
     * @param USAGE_CODE from AudioAttributes
     */
    static void playRingtone(@NonNull Ringtone ringtone, final int USAGE_CODE) {
        ringtone.setAudioAttributes(new AudioAttributes.Builder().setUsage(USAGE_CODE).build());
        ringtone.play();

    }

    /**
     * Plays a ringtone (overload di ringtonePlay(Ringtone ringtone, final int USAGE_CODE))
     *
     * @param ringtone the default ringtone
     */
    public static void playRingtone(Ringtone ringtone) {
        playRingtone(ringtone, AudioAttributes.USAGE_ALARM);
    }

    /**
     * Stops the playing sound
     *
     * @param ringtone the playing sound (ringtone or alarm tone)
     */
    public static void stopRingtone(Ringtone ringtone) {
        ringtone.stop();
    }


}
