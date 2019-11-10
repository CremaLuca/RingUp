package com.gruppo4.audioUtility;

/**
 * @author Francesco Bau'
 */

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;
import static android.content.Context.AUDIO_SERVICE;


public class AudioUtilityManager {

    public static final int MAX_PERCENTAGE = 100;
    public static final int MIN_PERCENTAGE = 0;

    /**
     *
     * @param context
     * @return The current Ringtone Volume (in percentage).
     */
    public static int getCurrentRingtoneVolume(Context context){

        int currentVolume = getAudioManager(context).getStreamVolume(AudioManager.STREAM_RING);

        int maxVolume = getMaxRingtoneVolume(context);
        return Math.round(MAX_PERCENTAGE*currentVolume/maxVolume);
    }

    /**
     *
     * @param context
     * @return The maximum Ringtone volume (real value).
     */
    private static int getMaxRingtoneVolume(Context context){
        return getAudioManager(context).getStreamMaxVolume(AudioManager.STREAM_RING);
    }

    /**
     *
     * @param context
     * @return The current AudioManager instance, determined by a certain Context.
     */
    public static AudioManager getAudioManager(Context context){
        return (AudioManager) context.getSystemService(AUDIO_SERVICE);
    }

    /**
     *
     * @param context,percentage
     * Sets up the Ringtone Volume, given a certain percentage.
     */
    public static void setRingtoneVolume(Context context, int percentage) throws IllegalArgumentException{

        if(percentage<MIN_PERCENTAGE)
            throw new IllegalArgumentException(" Your value is too low. Please insert a value between 0 and 100.");
        else if(percentage>MAX_PERCENTAGE) {
            throw new IllegalArgumentException(" Your value is too high. Please insert a value between 0 and 100.");
        }

        int maxVolume = getMaxRingtoneVolume(context);

        // Calculate the real value of the new volume
        int newVolume = maxVolume*percentage;
        newVolume = Math.round(newVolume/MAX_PERCENTAGE);

        // Set the ringer volume
        getAudioManager(context).setStreamVolume(
                AudioManager.STREAM_RING,
                newVolume,
                AudioManager.FLAG_SHOW_UI
        );

    }

    /**
     * @param context
     * Sets up the Ringtone Volume to its maximum value (in percentage).
     */
    public static void setMaxRingtoneVolume(Context context){
        setRingtoneVolume(context, MAX_PERCENTAGE);
    }

    /**
     * @param context
     * Sets up the Ringtone Volume to its minimum value (in percentage).
     */
    public static void setMinRingtoneVolume(Context context){
        setRingtoneVolume(context, MIN_PERCENTAGE);
    }


}
