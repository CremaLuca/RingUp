package com.gruppo4.ringtonePlayer;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.annotation.NonNull;

/**
 * @author Alessandra Tonin
 * <p>
 * CODE REVIEW FOR VELLUDO AND TURCATO
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
     * get the tone of the specified type
     *
     * @param ctx  the current application context
     * @param type an integer that represents the type of tone
     * @return the requested tone
     */
    private static Ringtone getDefaultTone(Context ctx, int type) {
        return RingtoneManager.getRingtone(ctx, getDefaultToneUri(type));
    }

    /**
     * get the default ringtone
     *
     * @param ctx the current application context
     * @return the actual ringtone
     */

    public static Ringtone getDefaultRingtone(Context ctx) {
        return getDefaultTone(ctx, RingtoneManager.TYPE_RINGTONE);
    }


    /**
     * get the default alarm tone
     *
     * @param ctx the current application context
     * @return the actual alarm tone
     */

    public static Ringtone getDefaultAlarmTone(Context ctx) {
        return getDefaultTone(ctx, RingtoneManager.TYPE_ALARM);
    }


    /**
     * user can decide in which mode to play the ringtone
     *
     * @param ringtone   the sound to be played
     * @param USAGE_CODE from AudioAttributes
     */
    public static void playRingtone(@NonNull Ringtone ringtone, final int USAGE_CODE) {
        ringtone.setAudioAttributes(new AudioAttributes.Builder().setUsage(USAGE_CODE).build());
        ringtone.play();
    }

    /**
     * plays a ringtone (overload of ringtonePlay(Ringtone ringtone, final int USAGE_CODE))
     *
     * @param ringtone the default ringtone
     */
    public static void playRingtone(Ringtone ringtone) {
        playRingtone(ringtone, AudioAttributes.USAGE_ALARM);
    }

    /**
     * stops the playing sound
     *
     * @param ringtone the playing sound (ringtone or alarm tone)
     */
    public static void stopRingtone(Ringtone ringtone) {
        ringtone.stop();
    }


}
