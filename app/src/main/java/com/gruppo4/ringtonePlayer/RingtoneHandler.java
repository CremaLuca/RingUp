package com.gruppo4.ringtonePlayer;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import org.jetbrains.annotations.NotNull;

/**
 * @author Alessandra Tonin, with a lot of useful tips from Luca Crema
 */

public class RingtoneHandler {


    /**
     * @param type a type from RingtoneManager API
     * @return the Uri of the specified type tone
     */
    private static Uri getToneUri(int type) {
        return RingtoneManager.getDefaultUri(type);
    }

    /**
     * get the tone of the specified type
     *
     * @param ctx, the current application context
     * @return the requested tone
     */
    public static Ringtone getDefaultTone(Context ctx, int type) {
        return RingtoneManager.getRingtone(ctx, getToneUri(type));
    }

    /**
     * user can decide in which mode he wants to play the ringtone
     *
     * @param ringtone   the sound to be played
     * @param USAGE_CODE from AudioAttributes
     */
    static void playRingtone(@NotNull Ringtone ringtone, final int USAGE_CODE) {
        ringtone.setAudioAttributes(new AudioAttributes.Builder().setUsage(USAGE_CODE).build());
        ringtone.play();
    }

    /**
     * plays a ringtone (overload di ringtonePlay(Ringtone ringtone, final int USAGE_CODE))
     *
     * @param ringtone, the default ringtone
     */
    public static void playRingtone(Ringtone ringtone) {
        playRingtone(ringtone, AudioAttributes.USAGE_ALARM);
    }

    /**
     * stops the playing sound
     *
     * @param ringtone, the playing sound (ringtone or alarm tone)
     */
    public static void stopRingtone(Ringtone ringtone) {
        ringtone.stop();
    }


}
