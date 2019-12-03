package com.gruppo4.RingApplication.structure;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

/**
 * Class that interfaces with the RingtoneManager class of the Android library
 *
 * @author Alessandra Tonin, Luca Crema, Alberto Ursino
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
     * @param context of the application
     * @param type    of the wanted tone
     * @return the requested tone
     */
    public Ringtone getDefaultTone(Context context, int type) {
        return RingtoneManager.getRingtone(context, getDefaultToneUri(type));
    }

    /**
     * @param context of the application
     * @return the actual ringtone
     */
    public Ringtone getDefaultRingtone(Context context) {
        if (defaultRingtone == null)
            defaultRingtone = getDefaultTone(context, RingtoneManager.TYPE_RINGTONE);
        return defaultRingtone;
    }

    /**
     * @param context of the application
     * @return the actual alarm tone
     */
    public Ringtone getDefaultAlarmTone(Context context) {
        return getDefaultTone(context, RingtoneManager.TYPE_ALARM);
    }

    /**
     * Plays a ringtone (overload of ringtonePlay(Ringtone ringtone, final int USAGE_CODE))
     *
     * @param ringtone set as default
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void playRingtone(Ringtone ringtone) {
        playRingtone(ringtone, AudioAttributes.USAGE_ALARM);
    }

    /**
     * Stops the playing sound
     *
     * @param ringtone that is playing (ringtone or alarm tone)
     */
    public void stopRingtone(Ringtone ringtone) {
        ringtone.stop();
    }

    /**
     * User can decide in which mode he wants to play the ringtone
     *
     * @param ringtone   to be played
     * @param USAGE_CODE from AudioAttributes
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void playRingtone(@NonNull Ringtone ringtone, final int USAGE_CODE) {
        ringtone.setAudioAttributes(new AudioAttributes.Builder().setUsage(USAGE_CODE).build());
        ringtone.play();

    }

    /**
     * @param type taken from RingtoneManager API
     * @return the Uri of the specified type tone
     */
    private Uri getDefaultToneUri(int type) {
        return RingtoneManager.getDefaultUri(type);
    }
}
