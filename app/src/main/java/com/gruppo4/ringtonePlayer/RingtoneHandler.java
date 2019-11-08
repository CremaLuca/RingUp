package com.gruppo4.ringtonePlayer;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

/**
 * @author Alessandra Tonin
 */

public class RingtoneHandler {

    /**
     * get the user's ringtone
     *
     * @param ctx, the current application context
     * @return the ringtone
     */
    public static Ringtone getDefaultRingtone(Context ctx) {
        Uri ringtoneId = RingtoneManager.getActualDefaultRingtoneUri(ctx, RingtoneManager.TYPE_RINGTONE);
        Ringtone ringtone = RingtoneManager.getRingtone(ctx, ringtoneId);
        return ringtone;
    }

    /**
     * get the user's alarm tone
     *
     * @param ctx, the current application context
     * @return the alarm tone
     */
    public static Ringtone getDefaultAlarmTone(Context ctx) {
        Uri alarmToneId = RingtoneManager.getActualDefaultRingtoneUri(ctx, RingtoneManager.TYPE_ALARM);
        Ringtone alarmTone = RingtoneManager.getRingtone(ctx, alarmToneId);
        return alarmTone;
    }

    /**
     * user can decide in which mode he wants to play the ringtone
     *
     * @param ringtone   @NotNull
     * @param USAGE_CODE from AudioAttributes
     */
    static void ringtonePlay(Ringtone ringtone, final int USAGE_CODE) {
        ringtone.setAudioAttributes(new AudioAttributes.Builder().setUsage(USAGE_CODE).build());
        ringtone.play();
    }

    /**
     * plays a ringtone (overload di ringtonePlay(Ringtone ringtone, final int USAGE_CODE))
     *
     * @param ringtone, the default ringtone
     */
    public static void ringtonePlay(Ringtone ringtone) {
        ringtone.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build());
        ringtone.play();
    }

    /**
     * stops the playing sound
     *
     * @param ringtone, the playing sound (ringtone or alarm tone)
     */
    public static void ringtoneStop(Ringtone ringtone) {
        ringtone.stop();
    }


}
