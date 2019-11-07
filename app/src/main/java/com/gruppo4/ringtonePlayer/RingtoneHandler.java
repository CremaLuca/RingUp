package com.gruppo4.ringtonePlayer;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

public class RingtoneHandler {

    /**
    * get the user's default ringtone
    * @param ctx, the current application context
    * @return the default ringtone
     */
    public static Ringtone getDefaultRingtone(Context ctx){
        Uri alarmTone = RingtoneManager.getActualDefaultRingtoneUri(ctx, RingtoneManager.TYPE_RINGTONE);
        Ringtone ringtone = RingtoneManager.getRingtone(ctx, alarmTone);
        return ringtone;
    }

    /**
    * plays the default ringtone
    * @param ringtone, the default ringtone
     */
    public static void ringtonePlay(Ringtone ringtone){
        ringtone.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build());
        if(ringtone!=null)
            ringtone.play();
    }

    /**
     * stops the playing ringtone
     * @param ringtone, the default ringtone
     */
    public static void ringtoneStop(Ringtone ringtone){
        ringtone.stop();
    }
    





}
